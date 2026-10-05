package com.example.rag.rag

import com.example.rag.data.GameDocument
import java.util.Locale

data class RetrievedResult(
    val document: GameDocument,
    val similarityScore: Float // True normalized score between 0.0 and 1.0
)

class VectorGameRetriever {
    fun retrieve(query: String, documents: List<GameDocument>, topK: Int = 4): List<RetrievedResult> {
        if (query.isBlank() || documents.isEmpty()) {
            return emptyList()
        }

        val queryLower = query.lowercase(Locale.ROOT)
        val queryTokens = queryLower.split(Regex("\\s+")).filter { it.length > 2 } // ignore small stop words

        val scoredDocs = documents.map { doc ->
            val titleLower = doc.title.lowercase(Locale.ROOT)
            val genreLower = doc.genre.lowercase(Locale.ROOT)
            val descLower = doc.description.lowercase(Locale.ROOT)
            val loreLower = doc.lore.lowercase(Locale.ROOT)

            // 1. Semantic Embedding Cosine Similarity (0.0 to 1.0)
            val queryEmb = EmbeddingModel.embed(query)
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

            // 3. Strict relevance filtering: if no tokens match and cosine is low, score remains very low
            val finalScore = if (queryTokens.isNotEmpty() && matchedTokensCount == 0 && cosine < 0.30f) {
                cosine * 0.1f
            } else {
                (cosine * 0.6f + tokenMatchRatio * 0.4f).coerceIn(0.0f, 1.0f)
            }

            // Deterministic tie-breaker jitter
            val tieBreaker = (doc.id.hashCode() % 100) / 10000.0f

            RetrievedResult(doc, finalScore + tieBreaker)
        }

        return scoredDocs
            .filter { it.similarityScore >= 0.28f } // Strict threshold: filter out anything below 28% match
            .sortedByDescending { it.similarityScore }
            .take(topK)
    }
}
