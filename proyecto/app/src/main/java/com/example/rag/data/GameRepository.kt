package com.example.rag.data

import android.content.Context
import com.example.rag.data.db.GameDatabase
import com.example.rag.rag.EmbeddingModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.json.JSONArray

class GameRepository(private val context: Context? = null) {

    fun getAllGames(): List<GameDocument> {
        val gamesList = mutableListOf<GameDocument>()

        if (context != null) {
            runBlocking(Dispatchers.IO) {
                try {
                    val db = GameDatabase.getDatabase(context)
                    val dao = db.gameDao()
                    
                    if (dao.getCount() < 200) {
                        GameDatabase.populateInitialData(dao, context)
                    }

                    val entities = dao.getAllGames()
                    for (entity in entities) {
                        val embeddingList = mutableListOf<Float>()
                        try {
                            val jsonArray = JSONArray(entity.embeddingJson)
                            for (i in 0 until jsonArray.length()) {
                                embeddingList.add(jsonArray.getDouble(i).toFloat())
                            }
                        } catch (_: Exception) {}

                        gamesList.add(
                            GameDocument(
                                id = entity.id,
                                title = entity.title,
                                genre = entity.genre,
                                platform = entity.platform,
                                releaseDate = entity.releaseDate,
                                description = entity.description,
                                lore = entity.lore,
                                embedding = embeddingList.ifEmpty { EmbeddingModel.embed("${entity.title} ${entity.genre} ${entity.description}") }
                            )
                        )
                    }
                } catch (_: Exception) {}
            }
        }

        if (gamesList.isEmpty()) {
            gamesList.add(
                GameDocument(
                    id = "1",
                    title = "Elden Ring",
                    genre = "Action RPG, Open World",
                    platform = "PC (Steam)",
                    releaseDate = "February 25, 2022",
                    description = "An action role-playing game developed by FromSoftware.",
                    lore = "Set in the Lands Between, the Elden Ring has been shattered.",
                    embedding = EmbeddingModel.embed("Elden Ring Action RPG Open World")
                )
            )
        }

        return gamesList
    }
}
