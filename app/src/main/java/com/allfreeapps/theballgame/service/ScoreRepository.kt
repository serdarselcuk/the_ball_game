package com.allfreeapps.theballgame.repository // You can choose your preferred package structure

import android.content.Context
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.service.dao.ScoreDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ScoreRepository(context: Context) {

    // Lazily initialize the ScoreDao.
    // The AppDatabase.getInstance() call might block if it's the first time the DB is created.
    // By using by lazy, it's only initialized when first accessed.
    // Methods using scoreDao will ensure this access happens on a background thread if needed.
//    private val scoreDao: ScoreDao by lazy {
//        AppDatabase.getInstance(applicationContext).scoreDao()
//    }
    private val scoreDao: ScoreDao

    init {
        // Get the application instance and then its database property
        val application = context.applicationContext as MyApplication
        scoreDao = application.database.scoreDao() // Access the pre-warmed DB
    }

    /**
     * Retrieves all scores as a Flow. Room handles running the query on a background thread
     * when you return a Flow from the DAO.
     * The first access to scoreDao (triggering AppDatabase.getInstance) is the main concern
     * for blocking, which is handled by how this repository's methods are called (e.g., insertScore).
     */
    fun getAllScoresFlow(): Flow<List<Score>> {
        // Room's Flow<List<Score>> from DAO is already main-safe and observes on a background thread.
        // The critical part is that the *first* call to scoreDao (which initializes the DB if not yet done)
        // should ideally happen off the main thread. If a ViewModel collects this flow,
        // the collection itself might trigger the lazy initialization of scoreDao.
        // If that's a concern, you could have an explicit init block or ensure first write operations
        // (like insertScore) happen first and are correctly dispatched.
        return scoreDao.getAll() // Assuming this method exists in your ScoreDao
    }

    /**
     * Inserts a new score into the database.
     * This operation is performed on the IO dispatcher to avoid blocking the main thread.
     *
     * @param score The Score object to insert.
     */
    suspend fun insertScore(score: Score) {
        withContext(Dispatchers.IO) {
            // Accessing scoreDao here will trigger its lazy initialization if it hasn't happened yet.
            // Since we are on Dispatchers.IO, the potential blocking call to AppDatabase.getInstance().build()
            // will occur on a background thread.
            scoreDao.insert(score) // Assuming insert(score: Score) is a suspend fun in ScoreDao
        }
    }

    /**
     * Retrieves a specific score by its ID.
     * This operation is performed on the IO dispatcher.
     *
     * @param scoreId The ID of the score to retrieve.
     * @return The Score object if found, or null.
     */
//    suspend fun getScoreById(scoreId: Long): Score? {
//        return withContext(Dispatchers.IO) {
//            scoreDao.getScoreById(scoreId) // Assuming getScoreById(id: Long): Score? is in ScoreDao
//        }
//    }

//
//    /**
//     * Deletes a score from the database.
//     * This operation is performed on the IO dispatcher.
//     *
//     * @param score The Score object to delete.
//     */
    suspend fun deleteScore(id: Int) {
        withContext(Dispatchers.IO) {
            scoreDao.delete(id) // Assuming delete(score: Score) is a suspend fun in ScoreDao
        }
    }

    suspend fun deleteAllScores() {
        withContext(Dispatchers.IO) {
            scoreDao.deleteAll()
        }
    }
//
//    /**
//     * Deletes all scores from the database.
//     * This operation is performed on the IO dispatcher.
//     */
//    suspend fun deleteAllScores() {
//        withContext(Dispatchers.IO) {
//            scoreDao.deleteAllScores() // Assuming deleteAllScores() is a suspend fun in ScoreDao
//        }
//    }

}