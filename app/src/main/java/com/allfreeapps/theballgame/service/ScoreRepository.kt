package com.allfreeapps.theballgame.service

import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.service.dao.ScoreDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreRepository @Inject constructor(
    private val scoreDao: ScoreDao
) {

    /**
     * Retrieves all scores as a Flow. Room handles running the query on a background thread
     * when you return a Flow from the DAO.
     * The first access to scoreDao (triggering AppDatabase.getInstance) is the main concern
     * for blocking, which is handled by how this repository's methods are called (e.g., insertScore).
     */
    fun getAllScoresFlow(): Flow<List<Score>> {

        return scoreDao.getAll()
    }

    /**
     * Inserts a new score into the database.
     * This operation is performed on the IO dispatcher to avoid blocking the main thread.
     *
     * @param score The Score object to insert.
     */
    suspend fun insertScore(score: Score) {
        withContext(Dispatchers.IO) {
            scoreDao.insert(score)
        }
    }

    /**
     * Deletes a score from the database.
     * This operation is performed on the IO dispatcher.
     *
     * @param id The Score object to delete.
     */
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

}