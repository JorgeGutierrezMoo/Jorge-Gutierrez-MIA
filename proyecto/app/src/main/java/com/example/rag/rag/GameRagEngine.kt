package com.example.rag.rag

import com.example.rag.data.GameDocument
import com.example.rag.data.GameRepository
import com.example.rag.data.RagResponse

class GameRagEngine(
    private val repository: GameRepository = GameRepository(),
    private val retriever: VectorGameRetriever = VectorGameRetriever()
) {
    fun ask(query: String, topK: Int = 4): RagResponse {
        val allGames = repository.getAllGames()
        val retrievedResults = retriever.retrieve(query, allGames, topK = topK)

        val retrievedDocs = retrievedResults.map { it.document }
        val scoresMap = retrievedResults.associate { it.document.id to it.similarityScore }

        val answer = synthesizeAnswer(query, retrievedResults)

        return RagResponse(
            query = query,
            retrievedSources = if (answer.contains("cannot answer") || answer.contains("abstain")) emptyList() else retrievedDocs,
            similarityScores = scoresMap,
            answer = answer
        )
    }

    private fun synthesizeAnswer(query: String, results: List<RetrievedResult>): String {
        // Abstention / anti-hallucination check: if top similarity score is below 30%, abstain
        if (results.isEmpty() || results[0].similarityScore < 0.30f) {
            return "I cannot answer this question because it is not covered by the game knowledge base corpus. I must abstain rather than invent or hallucinate information."
        }

        val titles = results.joinToString(", ") { "${it.document.title} (${(it.similarityScore * 100).toInt()}% match)" }
        return "Based on vector embedding semantic retrieval for \"$query\", the top ${results.size} recommended games are: $titles. Inspect the retrieved text chunks in the source cards below."
    }
}
