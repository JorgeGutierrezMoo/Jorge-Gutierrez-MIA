package com.example.rag.rag

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GemmaLlmManager(private val context: Context? = null) {
    private var llmInference: LlmInference? = null
    private var isInitialized = false

    fun init() {
        if (isInitialized || context == null) return
        isInitialized = true
        try {
            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath("/data/local/tmp/gemma-2b-it-gpu-int4.bin")
                .setMaxTokens(512)
                .setTopK(40)
                .setTemperature(0.7f)
                .build()
            llmInference = LlmInference.createFromOptions(context, options)
            Log.i("GemmaLlmManager", "Gemma 2B LlmInference initialized successfully.")
        } catch (e: Throwable) {
            llmInference = null
            Log.w("GemmaLlmManager", "Gemma 2B LlmInference not available: ${e.message}")
        }
    }

    suspend fun generateGroundedAnswer(query: String, retrievedChunks: String): String? = withContext(Dispatchers.IO) {
        val inference = llmInference ?: return@withContext null
        val prompt = """
            Eres un asistente experto de videojuegos. Responde a la pregunta del usuario en español basándote ÚNICAMENTE en los siguientes chunks de información de juegos:
            
            Información de Juegos:
            $retrievedChunks
            
            Pregunta del usuario: $query
            Respuesta:
        """.trimIndent()

        try {
            inference.generateResponse(prompt)
        } catch (e: Throwable) {
            Log.e("GemmaLlmManager", "Gemma inference failed: ${e.message}")
            null
        }
    }
}
