package com.example.rag.rag

import java.util.Locale
import kotlin.math.sqrt

object EmbeddingModel {
    // 16-dimensional semantic embedding space for games
    // Dimensions:
    // 0: action, 1: rpg, 2: open_world, 3: sci_fi, 4: fantasy, 5: multiplayer,
    // 6: survival, 7: platformer, 8: lore, 9: combat, 10: exploration, 11: dnd,
    // 12: nintendo, 13: cyberpunk, 14: story, 15: sandbox
    private val dimensionKeywords = listOf(
        listOf("action", "fight", "battle", "fast"),
        listOf("rpg", "role-playing", "character", "level", "stats"),
        listOf("open world", "vast", "world", "explore", "map"),
        listOf("sci-fi", "space", "future", "tech", "cyber"),
        listOf("fantasy", "magic", "sword", "dragon", "myth"),
        listOf("multiplayer", "co-op", "online", "friends"),
        listOf("survival", "craft", "resource", "gather", "build"),
        listOf("platformer", "jump", "mario", "obstacles"),
        listOf("lore", "history", "shards", "gods", "kingdom"),
        listOf("combat", "weapons", "boss", "action"),
        listOf("exploration", "discover", "secrets", "adventure"),
        listOf("dnd", "dungeons", "tabletop", "dice", "turn-based"),
        listOf("nintendo", "switch", "zelda", "mario", "console"),
        listOf("cyberpunk", "night city", "hack", "implants", "v"),
        listOf("story", "slumber", "memories", "quest", "journey"),
        listOf("sandbox", "blocks", "creativity", "mine", "open-ended")
    )

    fun embed(text: String): List<Float> {
        val lowerText = text.lowercase(Locale.ROOT)
        val vector = MutableList(dimensionKeywords.size) { 0.05f } // small baseline to avoid zero division

        for ((index, keywords) in dimensionKeywords.withIndex()) {
            for (kw in keywords) {
                if (lowerText.contains(kw)) {
                    vector[index] += 1.0f
                }
            }
        }

        // L2 Normalization
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
