package com.allfreeapps.theballgame.ui.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.allfreeapps.theballgame.ui.model.entities.Score
import com.allfreeapps.theballgame.ui.service.dao.ScoreDao

@Database(entities = [Score::class], version = 1)
abstract class ScoreDataBase: RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}