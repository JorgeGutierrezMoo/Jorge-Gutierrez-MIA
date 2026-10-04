package com.example.rag.rag

import com.example.rag.data.GameDocument
import java.util.Locale

class GameRetriever {
    fun retrieve(query: String, documents: List<GameDocument>, topK: Int = 2): List<GameDocument> {
        if (query.isBlank()) return documents.take(topK)

        val queryKeywords = query.lowercase(Locale.ROOT)
            .split(Regex("\\s+"))
            .filter { it.length > 2 }

        if (queryKeywords.isEmpty()) return documents.take(topK)

        val scoredDocs = documents.map { doc ->
            var score = 0
            val titleLower = doc.title.lowercase(Locale.ROOT)
            val genreLower = doc.genre.lowercase(Locale.ROOT)
            val descLower = doc.description.lowercase(Locale.ROOT)
            val loreLower = doc.lore.lowercase(Locale.ROOT)
            val platformLower = doc.platform.lowercase(Locale.ROOT)

            for (kw in queryKeywords) {
                if (titleLower.contains(kw)) score += 10
                if (genreLower.contains(kw)) score += 5
                if (platformLower.contains(kw)) score += 3
                if (descLower.contains(kw)) score += 2
                if (loreLower.contains(kw)) score += 2
            }
            doc to score
        }

        return scoredDocs
            .sortedByDescending { it.second }
            .filter { it.second > 0 }
            .take(topK)
            .map { it.first }
            .ifEmpty { documents.take(topK) }
    }
}
