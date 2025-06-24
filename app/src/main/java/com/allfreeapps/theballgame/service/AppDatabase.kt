package com.allfreeapps.theballgame.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.service.dao.ScoreDao
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Converters

@Database(entities = [Score::class], version = 3)
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
//                    .addMigrations()
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

//Migration strategies
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE Score_1 (
                id INTEGER PRIMARY KEY NOT NULL,
                first_name TEXT,
                last_name TEXT, 
                score INTEGER NOT NULL,
                date DATETIME
            )
        """.trimIndent())

        // 2. Copy data from the old 'Score' table to 'Score_new'.
        //    Since we are "removing and re-adding" lastName, we select it from the old table.
        //    If the 'new capability' meant a different data type that required transformation,
        //    you'd handle that transformation here in the SELECT statement.
        //    For just making it nullable, a direct copy is fine.
        db.execSQL("""
            INSERT INTO Score_1 (id, first_name, last_name, score, date)
            SELECT id, first_name, last_name, score , date FROM scores
        """.trimIndent())

        // 3. Drop the old 'Score' table
        db.execSQL("DROP TABLE scores")
        // 4. Rename the new 'Score_new' table to 'Score'
        db.execSQL("ALTER TABLE Score_1 RENAME TO scores")
    }
}
