package com.allfreeapps.theballgame.ui.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.allfreeapps.theballgame.ui.model.entities.Score

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score")
    fun getAll(): List<Score>

    @Insert
    fun insertAll(vararg scores: Score)
}