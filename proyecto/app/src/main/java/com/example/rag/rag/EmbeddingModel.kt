package com.example.rag.rag

import java.util.Locale
import kotlin.math.sqrt

object EmbeddingModel {
    fun embed(text: String): List<Float> {
        val lowerText = text.lowercase(Locale.ROOT)
        // 64-dimensional sparse semantic vector space
        val vector = MutableList(64) { 0.0f }
        
        val semanticTokens = listOf(
            "action", "rpg", "open world", "sci-fi", "fantasy", "multiplayer",
            "survival", "platformer", "lore", "combat", "exploration", "dnd",
            "nintendo", "cyberpunk", "story", "sandbox", "zombie", "horror",
            "strategy", "roguelike", "card", "simulation", "racing", "fighting",
            "shooter", "stealth", "puzzle", "indie", "classic", "anime",
            "moba", "mmorpg", "crafting", "space", "vampire", "medieval",
            "magical", "dungeon", "post-apocalyptic", "zombies", "co-op",
            "tactical", "historical", "detective", "deckbuilder", "mecha",
            "looter", "isometric", "retro", "turn-based", "mystery",
            // Spanish gaming terms for robust multilingual support
            "accion", "mundo abierto", "fantasia", "supervivencia", "estrategia",
            "aventura", "terror", "zombis", "disparos", "rol"
        )

        var matchedAny = false
        for ((index, token) in semanticTokens.withIndex()) {
            if (lowerText.contains(token)) {
                vector[index % 64] += 2.0f
                matchedAny = true
            }
        }

        // If no semantic tokens match at all, keep vector zero (orthogonal) -> cosine similarity = 0.0
        if (!matchedAny) {
            return vector
        }

        // L2 Normalization for matched vectors
        val norm = sqrt(vector.sumOf { (it * it).toDouble() }).toFloat()
        if (norm == 0f) return vector
        return vector.map { it / norm }
    }

    fun cosineSimilarity(v1: List<Float>, v2: List<Float>): Float {
        if (v1.size != v2.size || v1.isEmpty()) return 0f
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        val denominator = sqrt(normA.toDouble()) * sqrt(normB.toDouble())
        if (denominator == 0.0) return 0f
        return (dotProduct / denominator).toFloat()
    }
}
