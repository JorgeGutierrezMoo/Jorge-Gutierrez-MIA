package com.example.rag.data

data class GameDocument(
    val id: String,
    val title: String,
    val genre: String,
    val platform: String,
    val releaseDate: String,
    val description: String,
    val lore: String,
    val embedding: List<Float> = emptyList()
)

data class RagResponse(
    val query: String,
    val retrievedSources: List<GameDocument>,
    val similarityScores: Map<String, Float>,
    val answer: String
)
