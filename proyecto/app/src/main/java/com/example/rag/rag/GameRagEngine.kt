package com.example.rag.rag

import com.example.rag.data.GameDocument
import com.example.rag.data.GameRepository
import com.example.rag.data.RagResponse

class GameRagEngine(
    private val repository: GameRepository = GameRepository(),
    private val retriever: VectorGameRetriever = VectorGameRetriever()
) {
    fun ask(query: String): RagResponse {
        val allGames = repository.getAllGames()
        val retrievedResults = retriever.retrieve(query, allGames, topK = 2)

        val retrievedDocs = retrievedResults.map { it.document }
        val scoresMap = retrievedResults.associate { it.document.id to it.similarityScore }

        val answer = synthesizeAnswer(query, retrievedResults)

        return RagResponse(
            query = query,
            retrievedSources = retrievedDocs,
            similarityScores = scoresMap,
            answer = answer
        )
    }

    private fun synthesizeAnswer(query: String, results: List<RetrievedResult>): String {
        if (results.isEmpty()) {
            return "I couldn't find any relevant games matching your query."
        }

        val sb = StringBuilder()
        sb.append("Based on Room DB vector embedding semantic retrieval for \"$query\":\n\n")

        for ((index, res) in results.withIndex()) {
            val doc = res.document
            val confidencePercent = (res.similarityScore * 100).toInt()
            sb.append("${index + 1}. **${doc.title}** (Semantic Match: $confidencePercent%)\n")
            sb.append("   - **Genre**: ${doc.genre}\n")
            sb.append("   - **Platforms**: ${doc.platform}\n")
            sb.append("   - **Overview**: ${doc.description}\n")
            sb.append("   - **Lore**: ${doc.lore}\n\n")
        }

        sb.append("Retrieved and synthesized from SQLite Room Database vector storage.")
        return sb.toString()
    }
}
