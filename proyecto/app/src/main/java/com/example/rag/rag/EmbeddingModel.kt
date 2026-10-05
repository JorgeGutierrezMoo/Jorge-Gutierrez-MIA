package com.example.rag.rag

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder
import java.util.Locale
import kotlin.math.sqrt

object EmbeddingModel {
    private const val TAG = "EmbeddingModel"
    private var textEmbedder: TextEmbedder? = null
    private var isInitialized = false

    var lastUsedMediaPipe: Boolean = false
        private set

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true

        val modelNames = listOf("text_embedder.tflite", "universal_sentence_encoder.tflite")
        for (modelName in modelNames) {
            try {
                val baseOptions = BaseOptions.builder()
                    .setModelAssetPath(modelName)
                    .build()
                val options = TextEmbedder.TextEmbedderOptions.builder()
                    .setBaseOptions(baseOptions)
                    .build()
                textEmbedder = TextEmbedder.createFromOptions(context, options)
                Log.i(TAG, "MediaPipe TextEmbedder initialized successfully with $modelName")
                return
            } catch (e: Throwable) {
                textEmbedder = null
                Log.w(TAG, "MediaPipe TextEmbedder failed with $modelName: ${e.message}")
            }
        }
        Log.i(TAG, "MediaPipe TFLite model not found in assets, using pure Kotlin semantic fallback.")
    }

    fun embed(text: String): List<Float> {
        try {
            textEmbedder?.let { embedder ->
                val result = embedder.embed(text)
                val embeddingResult = result.embeddingResult()
                if (embeddingResult != null && embeddingResult.embeddings().isNotEmpty()) {
                    val floatEmb = embeddingResult.embeddings()[0].floatEmbedding()
                    val rawArray = floatEmb as? FloatArray ?: try {
                        floatEmb.javaClass.getMethod("floatArray").invoke(floatEmb) as? FloatArray
                    } catch (_: Throwable) {
                        null
                    }

                    if (rawArray != null) {
                        val floatList = mutableListOf<Float>()
                        for (f in rawArray) {
                            floatList.add(f)
                        }
                        if (floatList.isNotEmpty()) {
                            lastUsedMediaPipe = true
                            return floatList
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.d(TAG, "MediaPipe embed execution error: ${e.message}")
        }

        lastUsedMediaPipe = false
        return generateSemanticFallbackEmbedding(text)
    }

    private fun generateSemanticFallbackEmbedding(text: String): List<Float> {
        val lowerText = text.lowercase(Locale.ROOT)
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

        if (!matchedAny) return vector

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
