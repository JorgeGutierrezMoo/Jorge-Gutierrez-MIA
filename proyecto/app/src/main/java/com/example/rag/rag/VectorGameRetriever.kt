package com.example.rag.rag

import com.example.rag.data.GameDocument
import java.text.Normalizer
import java.util.Locale

data class RetrievedResult(
    val document: GameDocument,
    val similarityScore: Float // True normalized score between 0.0 and 1.0
)

class VectorGameRetriever {

    private fun normalizeText(text: String): String {
        val lower = text.lowercase(Locale.ROOT)
        return Normalizer.normalize(lower, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }

    private fun expandSpanishQuery(query: String): String {
        val normalized = normalizeText(query)
        val synonyms = mapOf(
            "accion" to "action",
            "mundo abierto" to "open world",
            "fantasia" to "fantasy",
            "supervivencia" to "survival",
            "estrategia" to "strategy",
            "aventura" to "adventure",
            "terror" to "horror",
            "miedo" to "horror",
            "zombi" to "zombie",
            "zombis" to "zombies",
            "disparos" to "shooter",
            "tiros" to "shooter",
            "rol" to "rpg",
            "carreras" to "racing",
            "autos" to "racing",
            "coches" to "racing",
            "pelea" to "fighting",
            "lucha" to "fighting",
            "cartas" to "card deckbuilder",
            "espacio" to "space sci-fi",
            "ciencia ficcion" to "sci-fi",
            "construccion" to "crafting sandbox",
            "cooperativo" to "co-op",
            "multijugador" to "multiplayer"
        )

        var expanded = normalized
        for ((es, en) in synonyms) {
            if (normalized.contains(es)) {
                expanded += " $en"
            }
        }
        return expanded
    }

    fun retrieve(query: String, documents: List<GameDocument>, topK: Int = 4): List<RetrievedResult> {
        if (query.isBlank() || documents.isEmpty()) {
            return emptyList()
        }

        val expandedQuery = expandSpanishQuery(query)
        val queryLower = normalizeText(expandedQuery)
        val queryTokens = queryLower.split(Regex("\\s+")).filter { it.length > 2 }

        val scoredDocs = documents.map { doc ->
            val titleLower = normalizeText(doc.title)
            val genreLower = normalizeText(doc.genre)
            val descLower = normalizeText(doc.description)
            val loreLower = normalizeText(doc.lore)

            // 1. Semantic Embedding Cosine Similarity (0.0 to 1.0)
            val queryEmb = EmbeddingModel.embed(expandedQuery)
            val docEmb = if (doc.embedding.isNotEmpty()) {
                doc.embedding
            } else {
                EmbeddingModel.embed("$titleLower $genreLower $descLower $loreLower")
            }
            val cosine = EmbeddingModel.cosineSimilarity(queryEmb, docEmb)

            // 2. Lexical Token Matching
            var matchedTokensCount = 0
            for (token in queryTokens) {
                if (titleLower.contains(token) || genreLower.contains(token) || descLower.contains(token) || loreLower.contains(token)) {
                    matchedTokensCount++
                }
            }

            val tokenMatchRatio = if (queryTokens.isNotEmpty()) matchedTokensCount.toFloat() / queryTokens.size else 0f

            // 3. Strict relevance filtering
            val finalScore = if (queryTokens.isNotEmpty() && matchedTokensCount == 0 && cosine < 0.28f) {
                cosine * 0.1f
            } else {
                (cosine * 0.6f + tokenMatchRatio * 0.4f).coerceIn(0.0f, 1.0f)
            }

            // Deterministic tie-breaker jitter
            val tieBreaker = (doc.id.hashCode() % 100) / 10000.0f

            RetrievedResult(doc, finalScore + tieBreaker)
        }

        return scoredDocs
            .filter { it.similarityScore >= 0.28f }
            .sortedByDescending { it.similarityScore }
            .take(topK)
    }
}
