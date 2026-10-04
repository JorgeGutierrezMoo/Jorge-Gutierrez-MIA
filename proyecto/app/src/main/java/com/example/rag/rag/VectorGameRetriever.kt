package com.example.rag.rag

import com.example.rag.data.GameDocument
import java.util.Locale

data class RetrievedResult(
    val document: GameDocument,
    val similarityScore: Float // Normalized between 0.0 and 1.0 for percentage display
)

class VectorGameRetriever {
    fun retrieve(query: String, documents: List<GameDocument>, topK: Int = 4): List<RetrievedResult> {
        if (query.isBlank() || documents.isEmpty()) {
            return documents.shuffled().take(topK).map { RetrievedResult(it, 0.95f) }
        }

        val queryLower = query.lowercase(Locale.ROOT)
        val queryTokens = queryLower.split(Regex("\\s+")).filter { it.length > 1 }

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

            // 2. Lexical Token Boost (0.0 to 0.3 boost)
            var lexicalBoost = 0.0f
            if (titleLower.contains(queryLower)) lexicalBoost += 0.3f
            if (genreLower.contains(queryLower)) lexicalBoost += 0.2f

            for (token in queryTokens) {
                if (titleLower.contains(token)) lexicalBoost += 0.15f
                if (genreLower.contains(token)) lexicalBoost += 0.1f
                if (descLower.contains(token) || loreLower.contains(token)) lexicalBoost += 0.05f
            }

            // Combine into final normalized score between 0.0 and 1.0
            val finalScore = (cosine * 0.7f + lexicalBoost * 0.3f).coerceIn(0.15f, 0.99f)

            // Deterministic tie-breaker jitter
            val tieBreaker = (doc.id.hashCode() % 100) / 10000.0f

            RetrievedResult(doc, finalScore + tieBreaker)
        }

        return scoredDocs
            .sortedByDescending { it.similarityScore }
            .take(topK)
    }
}
