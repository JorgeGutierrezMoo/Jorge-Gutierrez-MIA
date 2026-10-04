package com.example.rag.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getCount(): Int
}
