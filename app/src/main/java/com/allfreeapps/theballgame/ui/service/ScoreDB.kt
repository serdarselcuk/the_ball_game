package com.allfreeapps.theballgame.ui.service

import android.content.Context
import androidx.room.Room
import com.allfreeapps.theballgame.ui.service.dao.ScoreDao

class ScoreDB(private val applicationContext: Context) {
    private lateinit var db: ScoreDataBase
    private lateinit var connection: ScoreDao

    fun connect(): ScoreDao {
        db = Room.databaseBuilder(
            applicationContext,
            ScoreDataBase::class.java, "Score_DB"
        ).build()
        connection = db.scoreDao()
        return connection
    }

    fun close(){
        db.close()
    }
}