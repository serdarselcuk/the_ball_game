package com.allfreeapps.theballgame.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.service.dao.ScoreDao
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Converters

@Database(entities = [Score::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile // Ensures visibility of changes to other threads
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { // synchronized block for thread safety
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    // .fallbackToDestructiveMigration() // Optional: Handles migrations by destroying and recreating the DB
                    // .addMigrations(MIGRATION_1_2) // Optional: For custom migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
