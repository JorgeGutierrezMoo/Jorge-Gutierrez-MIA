package com.example.rag.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val genre: String,
    val platform: String,
    val releaseDate: String,
    val description: String,
    val lore: String,
    val embeddingJson: String // Serialized List<Float> as JSON string
)
