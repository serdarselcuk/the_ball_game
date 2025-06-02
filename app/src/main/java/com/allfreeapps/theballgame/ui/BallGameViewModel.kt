package com.allfreeapps.theballgame.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.model.Direction
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.repository.ScoreRepository
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Constants.Companion.BALL_LIMIT_TO_REMOVE
import com.allfreeapps.theballgame.utils.Constants.Companion.GRID_SIZE
import com.allfreeapps.theballgame.utils.Constants.Companion.MAX_BALL_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

class BallGameViewModel( application: Application ) : AndroidViewModel(application)  {

    private val repository: ScoreRepository = ScoreRepository(application.applicationContext)

    companion object {
        const val TAG = "ViewModel"
        const val LAST_GRID_INDEX = GRID_SIZE - 1
        const val SERIE_LENGTH_ON_INDEXED_BOARD = BALL_LIMIT_TO_REMOVE - 1 //since starting with 0

        // Define possible movements (row_offset, column_offset)
        val movements = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )

        val directionPairsToSearch = listOf(
            Pair(Direction.UP, Direction.DOWN),
            Pair(Direction.LEFT, Direction.RIGHT),
            Pair(Direction.UP_LEFT, Direction.DOWN_RIGHT),
            Pair(Direction.UP_RIGHT, Direction.DOWN_LEFT)
        )
    }

    val allScores: StateFlow<List<Score>> = repository.getAllScoresFlow()
        .stateIn(
            scope = viewModelScope, // The coroutine scope for collection
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted

    private val _errorState: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorState: StateFlow<String?> = _errorState

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    // order represents the position in the board and the value stands for the color
    private val _ballList = MutableStateFlow(Array(MAX_BALL_COUNT) { 0 })
    val ballList: StateFlow<Array<Int>> = _ballList

    private val _totalBallCount = MutableStateFlow(0)
    val totalBallCount: StateFlow<Int> = _totalBallCount

    private val _selectedBall = MutableStateFlow<Int?>(null)
    val selectedBall: StateFlow<Int?> = _selectedBall

    private val _setToRemove = MutableStateFlow<MutableList<MutableSet<Int>>>(mutableListOf())
    val setToRemove: StateFlow<List<Set<Int>>> = _setToRemove

    private var _upcomingBalls = MutableStateFlow((arrayOf<Int>()))
    val upcomingBalls: StateFlow<Array<Int>> = _upcomingBalls

    private val _state = MutableStateFlow<GameState?>(null)
    val state: StateFlow<GameState?> = _state

    init {
       _state.value = GameState.GameNotStarted
    }

    private fun mute(){
        Log.d(TAG, "mute: ")
        _isMuted.value = true
    }

    private fun unMute(){
        Log.d(TAG, "unMute: ")
        _isMuted.value = false
    }

    fun startGame() {
        if (totalBallCount.value == 0) {
            add3Ball()
            populateUpcomingBalls()
            setState(GameState.UserTurn)
        }
    }

    private fun restartGame() {
        setEmptyBoard()
        startGame()
    }


    private fun setEmptyBoard() {
        setState(GameState.GameNotStarted)
        _ballList.value = Array(81) { 0 }
        _totalBallCount.value = 0
        resetScore()
        resetRemovalSet()
        deselectTheBall()
    }

    fun increaseScoreFor(ballCount: Int) {
        if (ballCount < 5) return
        val score = 5
        val additionalScore = (2 * (ballCount - 5))
        _score.value += (score + additionalScore)
    }

    fun combinedScore() {
        _score.value += setToRemove.value.size
    }

    fun setState(gameState: GameState){
        Log.d(TAG, "setState: $gameState")
        _state.value = gameState
    }

    fun resetScore() {
        Log.d(TAG, "resetScore: ${score.value}")
        _score.value = 0
    }

    fun resetRemovalSet() {
        Log.d(TAG, "resetRemovalSet: ${setToRemove.value}")
        _setToRemove.value = mutableListOf()
    }

    fun addBall(index: Int, colorCode: Int) {
        Log.d(TAG, "addBall: $index, $colorCode")
        if(totalBallCount.value == (MAX_BALL_COUNT)){
            finishTheGame()
            return
        }
        viewModelScope.launch {
            if (totalBallCount.value == (MAX_BALL_COUNT)) return@launch

            // Create a new array by copying the current one
            val newBallList = _ballList.value.copyOf()
            // Update the value in the new array
            newBallList[index] = colorCode
            // Emit the new array to the StateFlow
            _ballList.value = newBallList
            _totalBallCount.value += 1

        }
    }

    private fun positionToIndex(row: Int, column: Int): Int {
        val index = (row * GRID_SIZE) + column
        Log.d(TAG, "row: $row, column: $column to index: ${index}")
        return index
    }

    private fun indexToPosition(index: Int): Array<Int> {
        val column = (index) % GRID_SIZE
        val row = (index - column) / GRID_SIZE
        Log.d(TAG, "index: $index to row: $row, column: $column")
        return arrayOf(row, column)
    }

    fun selectTheBall(position: Int) {
        Log.d(TAG, "selectTheBall: $position")
        _selectedBall.value = position
    }

    fun deselectTheBall() {
        Log.d(TAG, "deselectTheBall")
        _selectedBall.value = null
    }

    private fun getSelectedBallColor(): Int? {
        return selectedBall.value?.let { ballList.value[it] }
    }

    private fun removeBall(index: Int) {
//        TODO you can throw error if ball is not exists
        viewModelScope.launch {
            val noBallInTheBoard = totalBallCount.value == 0
            val ballIsAlreadyRemoved = ballList.value[index] == 0
            if (noBallInTheBoard || ballIsAlreadyRemoved) return@launch

            val copyOfBallList = _ballList.value.copyOf()
            copyOfBallList[index] = 0
            _ballList.value = copyOfBallList
            _totalBallCount.value -= 1
            Log.d(TAG, "index: $index")
        }
    }

    private fun isSelectedBall(index: Int): Boolean {
        Log.d(TAG, "isSelectedBall: $index")
        return selectedBall.value == index
    }


    fun processEmptyCellClick(destinationIndex: Int ) {
        Log.d(TAG, "processEmptyCellClick: $destinationIndex")
        if (selectedBall.value == null) {
            Log.d(TAG,"No ball is selected at the moment")
        } //TODO Or handle as an error/log
        // This method now orchestrates the logic
        // It can launch a single coroutine to manage the sequence
        viewModelScope.launch { // This launch block uses Dispatchers.Main.immediate by default
            try {
                val path =
                    createThePathToMoveTheBall(destinationIndex) // Assuming this is quick or also a suspend fun managing its context

                if (path != null) {

                    moveTheBall(path)

                    val seriesFound = findColorSeries(listOf(destinationIndex))

                    if (seriesFound) {
                        removeAllSeries()
                    } else {
                        populateUpcomingBalls()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing empty cell click: ${e.message}")
                _errorState.value = "Failed to move ball: ${e.message}"
            } finally {
                deselectTheBall() // This might be a simple state update, not needing a specific dispatcher
            }
        }

    }

    private suspend fun createThePathToMoveTheBall(destinationIndex: Int): List<Int>? =
        withContext(Dispatchers.Default) {
            Log.d(TAG, "createThePathToMoveTheBall: $destinationIndex")
            val selectedBallIndex = selectedBall.value

            if (isMoveInvalid(selectedBallIndex, destinationIndex)) return@withContext null

            // Perform Breadth-First Search (BFS) to find a path
            val queue: Queue<Int> = LinkedList()
            val visited = BooleanArray(GRID_SIZE * GRID_SIZE) { false }
            val parentMap = mutableMapOf<Int, Int>() // To reconstruct the path

            selectedBallIndex?.let {
                queue.add(it)
                visited[it] = true
            } ?: return@withContext null // If selectedBallIndex is null, no path can be found


            while (queue.isNotEmpty()) {
                val currentIndex = queue.poll()

                // If we reached the target, reconstruct and return the path
                if (currentIndex == destinationIndex) {
                    return@withContext reconstructPath(
                        parentMap = parentMap,
                        startIndex = selectedBallIndex,
                        targetIndex = destinationIndex
                    )
                }

                // Get valid neighbors (horizontal, vertical, diagonal)
                val neighbors =
                    currentIndex?.let { getNeighbors(it) } // currentIndex is non-null here

                neighbors?.let {
                    for (neighborIndex in neighbors) {
                        // Check if the neighbor is within bounds, not visited, and is an empty square
                        if (neighborIndex in 0..80 && !visited[neighborIndex] && _ballList.value[neighborIndex] == 0) {
                            visited[neighborIndex] = true
                            parentMap[neighborIndex] =
                                currentIndex // Store the parent for path reconstruction
                            queue.add(neighborIndex)
                        }
                    }
                }
            }
            // If the queue is empty and the target was not reached, no path exists.
            return@withContext null
        }


    private fun isMoveInvalid(selectedBallIndex: Int?, destinationIndex: Int): Boolean {
        // 1. No ball selected
        if (selectedBallIndex == null) {
            Log.d(TAG, "Move invalid: No ball selected.")
            return true
        }

        // 2. Attempting to move to the same spot
        if (isSelectedBall(destinationIndex)) {
            Log.d(
                TAG,
                "Move invalid: Ball is already on the target. selectedBallIndex: $selectedBallIndex, destinationIndex: $destinationIndex"
            )
            return true
        }

        // 3. Destination is occupied by another ball
        // Assuming _ballList.value is safe to access and 0 means empty
        if (_ballList.value.getOrNull(destinationIndex) != 0) { // Added getOrNull for safety
            Log.d(
                TAG,
                "Move invalid: Destination index $destinationIndex is occupied."
            )
            return true
        }

        // If none of the above conditions are met, the move is valid (so isMoveInvalid is false)
        return false
    }

    private suspend fun findColorSeries(listOfChanges: List<Int>): Boolean = withContext(Dispatchers.Default) {
        Log.d(TAG, "findColorSeries: $listOfChanges")
        var result = false
        setState(GameState.SearchingForSeries)
        listOfChanges.forEach { ballPosition ->
            val startColor = ballList.value[ballPosition]

            // Skip empty cells or invalid colors as starting points
            if (startColor > 0) {
                val (row, column) = indexToPosition(ballPosition)

                for ((direction1, direction2) in directionPairsToSearch) {
                    //                  if down-left is the direction there should be enough space for the series
                    when (direction1) {
                        //                      if diagonal direction, there should be enough space for the series (5 ball not fitting to corners)
                        Direction.UP_RIGHT, Direction.DOWN_LEFT -> if (((column + row) < SERIE_LENGTH_ON_INDEXED_BOARD) || ((row + column) > ((2 * LAST_GRID_INDEX) - SERIE_LENGTH_ON_INDEXED_BOARD))) continue
                        Direction.DOWN_RIGHT, Direction.UP_LEFT -> if (((column - row) > SERIE_LENGTH_ON_INDEXED_BOARD) || ((row - column) > SERIE_LENGTH_ON_INDEXED_BOARD)) continue
                        else -> {}
                    }

                    val currentSeries = mutableSetOf<Int>()
                    currentSeries.add(
                        positionToIndex(
                            row, column
                        )
                    ) // Start with the current cell

                    // Explore in each direction
                    val direction1Series = async {
                        exploreTheDirection(startColor, row, column, direction1)
                    }

                    val direction2Series = async {
                        exploreTheDirection(startColor, row, column, direction2)
                    }

                    currentSeries.addAll(direction1Series.await())
                    currentSeries.addAll(direction2Series.await())

                    // If the series is long enough, add all its cells to the result set
                    if (currentSeries.size >= BALL_LIMIT_TO_REMOVE) {
                        _setToRemove.value.add(currentSeries)
                        result = true
                    }
                }
            }
        }

        return@withContext result
    }


    private suspend fun exploreTheDirection(
        startColor: Int,
        row: Int,
        column: Int,
        currentDirection: Direction
    ): Set<Int> = withContext(Dispatchers.Default) {
        val currentSeries = mutableSetOf<Int>()
        for (k in 1 until GRID_SIZE) { // Max possible extension length
            val nextR = row + k * currentDirection.rowOffset
            val nextC = column + k * currentDirection.colOffset
            if (positionIsOutOfBoard(nextR, nextC)) {
                continue
            }
            // Get the color at the next coordinates (includes bounds check)
            val nextColor = ballList.value[positionToIndex(nextR, nextC)]

            // Stop if out of bounds (getColor returns <= 0) or color mismatch
            if (nextColor == startColor) {
                // Coordinates are valid and color matches
                currentSeries.add(positionToIndex(nextR, nextC))
            } else {
                // Out of bounds OR color mismatch
                break
            }
        }
        return@withContext currentSeries

    }

    private fun positionIsOutOfBoard(nextR: Int, nextC: Int): Boolean {
        return nextR > GRID_SIZE - 1
                || nextC > GRID_SIZE - 1
                || nextR < 0
                || nextC < 0
    }

    suspend fun moveTheBall(path: List<Int>) {
//        todo throw error when color is 0 or null
        withContext(Dispatchers.IO) {
            val color = getSelectedBallColor() ?: 0
            if (color != 0) {
                path.forEach { ball ->
                    val selectedBallIndex = selectedBall.value
                    if (selectedBallIndex != null && !isSelectedBall(ball)) {
                        addBall(ball, color)
                        removeBall(selectedBallIndex)
                        selectTheBall(ball)
                        delay(Constants.BALL_MOVEMENT_LATENCY)
                    }
                }
            }
            deselectTheBall()
        }
    }

    /**
     * Gets the indices of the valid neighbors (horizontal, vertical, diagonal) for a given index.
     */
    private suspend fun getNeighbors(index: Int): List<Int> = withContext(Dispatchers.Default) {
        val neighbors = mutableListOf<Int>()
        val (row, column) = indexToPosition(index)
        for (direction in movements) {
            val neighborRow = row + direction.rowOffset
            val neighborColumn = column + direction.colOffset

            // Check if the neighbor is within the 9x9 grid bounds
            if (
                (neighborRow in 0..<GRID_SIZE)
                &&
                (neighborColumn in 0..<GRID_SIZE)
            ) {
                neighbors.add(positionToIndex(neighborRow, neighborColumn))
            }
        }

        return@withContext neighbors
    }


    /**
     * Reconstructs the path from the start index to the target index using the parent map.
     */
    private suspend fun reconstructPath(
        parentMap: Map<Int, Int>,
        startIndex: Int,
        targetIndex: Int
    ): List<Int> = withContext(Dispatchers.Default) {
        val path = mutableListOf<Int>()
        var currentIndex = targetIndex

        while (currentIndex != startIndex) {
            path.add(0, currentIndex) // Add to the beginning to get the path in order
            currentIndex =
                parentMap[currentIndex] ?: break // Should not be null if target was reached
        }
        path.add(0, startIndex) // Add the start index

        return@withContext path
    }

    fun add3Ball() {
        viewModelScope.launch {
            val ballArray = Array(3) { 0 }
            for (i in 0 until 3) {
                ballArray[i] = randomColor()
            }
            _upcomingBalls.value = ballArray
        }
    }

    private fun randomColor(): Int {
        return (1..6).random()
    }

    private suspend fun randomBall(): Int = withContext(Dispatchers.Default) {
        if (_totalBallCount.value == 81) return@withContext -1
        var randomNumber = (0..(81 - totalBallCount.value)).random()
        for ((index, each) in ballList.value.withIndex()) {
            if (each != 0) continue
            if (--randomNumber == 0) {
                return@withContext index
            }
        }
        return@withContext randomBall()
    }

    fun removeAllSeries() {

        getRemovables().forEach { set ->
            set.forEach {
                removeBall(it)
            }
            increaseScoreFor(set.size)
        }
        resetRemovalSet()
    }

    private fun getRemovables(): List<Set<Int>> {
        return setToRemove.value
    }

    private fun populateUpcomingBalls() {
        viewModelScope.launch {
            val upcomingBalls = upcomingBalls.value
            val ballPositions = mutableListOf<Int>()
            for (color in upcomingBalls) {
                val ballPosition = randomBall()
                if (ballPosition == -1){
                    finishTheGame()
                    break
                }
                addBall(ballPosition, color)
                ballPositions.add(ballPosition)
            }

            add3Ball()

            findColorSeries(ballPositions)
            removeAllSeries()
            finishTheGame()
        }
    }

    private fun finishTheGame() {
        if(totalBallCount.value == 81){
            setState(GameState.GameOver)
        }
    }

    fun resetGame() {
        setEmptyBoard()
    }

    fun saveScore(userName: String) {
        val score = score.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScore(
                Score(
                    id = null,
                    firstName = userName,
                    lastName = "",
                    score = score,
                    date = Date()
                )
            )
        }
    }

    fun processOnBallCellClick(index: Int) {
        if(isSelectedBall(index))  deselectTheBall()
        else  selectTheBall(index)
    }

    fun changeSoundStatus() {
        if (isMuted.value) unMute()
        else mute()
    }

    fun restartButtonOnClick() = run {
        if (state.value == GameState.GameNotStarted) startGame()
        else restartGame()
    }

    fun onCellClick(color: Int, index: Int) = run {
        //if  thereIsNoBall
        if ( color == Constants.NO_BALL) processEmptyCellClick(index)
        else processOnBallCellClick(index)
    }
}