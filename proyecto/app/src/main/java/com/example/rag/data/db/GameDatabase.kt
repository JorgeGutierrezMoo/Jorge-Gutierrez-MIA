package com.example.rag.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rag.rag.EmbeddingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader

@Database(entities = [GameEntity::class], version = 5, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "games_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(GameDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class GameDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.gameDao(), context)
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.gameDao()
                        if (dao.getCount() < 450) {
                            populateInitialData(dao, context)
                        }
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: GameDao, context: Context) {
            val entities = mutableListOf<GameEntity>()
            val assetFiles = listOf("games.json", "games_1.json", "games_2.json", "games_3.json", "games_4.json")

            for (fileName in assetFiles) {
                try {
                    val inputStream = context.assets.open(fileName)
                    val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
                    val jsonArray = JSONArray(jsonString)

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("id")
                        val title = obj.getString("title")
                        val genre = obj.getString("genre")
                        val platform = obj.getString("platform")
                        val releaseDate = obj.getString("releaseDate")
                        val description = obj.getString("description")
                        val lore = obj.getString("lore")

                        val textToEmbed = "$title $genre $description $lore"
                        val embedding = EmbeddingModel.embed(textToEmbed)
                        val embeddingJson = JSONArray(embedding).toString()

                        entities.add(
                            GameEntity(
                                id = id,
                                title = title,
                                genre = genre,
                                platform = platform,
                                releaseDate = releaseDate,
                                description = description,
                                lore = lore,
                                embeddingJson = embeddingJson
                            )
                        )
                    }
                } catch (_: Exception) {}
            }

            if (entities.isNotEmpty()) {
                dao.deleteAll()
                dao.insertGames(entities)
            }
        }
    }
}
