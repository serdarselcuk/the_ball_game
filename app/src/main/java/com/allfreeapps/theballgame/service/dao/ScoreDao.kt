package com.allfreeapps.theballgame.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.allfreeapps.theballgame.model.entities.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores ORDER BY score DESC")
    fun getAll(): Flow<List<Score>>

    @Insert
    suspend fun insertAll(vararg score: Score)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Defines conflict strategy
    suspend fun insert(score: Score)
}