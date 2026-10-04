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
        val retrievedResults = retriever.retrieve(query, allGames, topK = 4)

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

        val titles = results.joinToString(", ") { "${it.document.title} (${(it.similarityScore * 100).toInt()}% match)" }
        return "Based on vector embedding semantic retrieval for \"$query\", the top 4 recommended games are: $titles. You can inspect the detailed knowledge source cards below for full overviews, genres, and lore."
    }
}
