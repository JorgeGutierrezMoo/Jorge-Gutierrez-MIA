package com.example.rag.rag

import com.example.rag.data.GameDocument
import com.example.rag.data.GameRepository
import com.example.rag.data.RagResponse

class GameRagEngine(
    private val repository: GameRepository = GameRepository(),
    private val retriever: VectorGameRetriever = VectorGameRetriever(),
    private val gemmaManager: GemmaLlmManager? = null
) {
    suspend fun ask(query: String, topK: Int = 4): RagResponse {
        val allGames = repository.getAllGames()
        val retrievedResults = retriever.retrieve(query, allGames, topK = topK)

        val retrievedDocs = retrievedResults.map { it.document }
        val scoresMap = retrievedResults.associate { it.document.id to it.similarityScore }

        if (retrievedResults.isEmpty() || retrievedResults[0].similarityScore < 0.28f) {
            val abstentionMsg = "No puedo responder a esta pregunta porque no está cubierta en el corpus de la base de conocimientos de juegos. Debo abstenerme en lugar de inventar o alucinar información."
            return RagResponse(
                query = query,
                retrievedSources = emptyList(),
                similarityScores = scoresMap,
                answer = abstentionMsg
            )
        }

        val chunksText = retrievedResults.joinToString("\n\n") { res ->
            "Juego: ${res.document.title}\nGénero: ${res.document.genre}\nDescripción: ${res.document.description}\nLore: ${res.document.lore}"
        }

        val gemmaAnswer = gemmaManager?.generateGroundedAnswer(query, chunksText)
        val answer = gemmaAnswer ?: synthesizeAnswer(query, retrievedResults)

        return RagResponse(
            query = query,
            retrievedSources = retrievedDocs,
            similarityScores = scoresMap,
            answer = answer
        )
    }

    private fun synthesizeAnswer(query: String, results: List<RetrievedResult>): String {
        val titles = results.joinToString(", ") { "${it.document.title} (${(it.similarityScore * 100).toInt()}% de coincidencia)" }
        return "Basado en la búsqueda semántica vectorial para \"$query\", los ${results.size} juegos principales recomendados son: $titles. Puedes inspeccionar los chunks de texto recuperados en las tarjetas a continuación."
    }
}
