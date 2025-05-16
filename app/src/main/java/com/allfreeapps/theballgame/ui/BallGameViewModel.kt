package com.allfreeapps.theballgame.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.allfreeapps.theballgame.ui.model.Direction
import com.allfreeapps.theballgame.ui.model.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue

class BallGameViewModel : ViewModel() {
    private val _ballList = MutableStateFlow(Array(gridSize * gridSize) { 0 })
    val ballList: StateFlow<Array<Int>> = _ballList

    private val _totalBallCount = MutableStateFlow(0)
    val totalBallCount: StateFlow<Int> = _totalBallCount

    private val _selectedBall = MutableStateFlow<Int?>(null)
    val selectedBall: StateFlow<Int?> = _selectedBall

    private val _setToRemove = MutableStateFlow<MutableSet<Int>>(mutableSetOf())
    val setToRemove: StateFlow<Set<Int>> = _setToRemove

    private val _state = MutableStateFlow<GameState>(GameState.GameNotStarted)
    private val state: StateFlow<GameState> = _state

    fun startGame() {
        if (totalBallCount.value == 0) add3Ball()
    }

    fun restartGame() {
        _state.value = GameState.GameNotStarted
        _ballList.value = Array(81) { 0 }
        _totalBallCount.value = 0
        setEmptyBoard()
        resetRemovalSet()
        selectTheBall(null)
        startGame()
    }

    fun setEmptyBoard() {
        _state.value = GameState.GameNotStarted
        _ballList.value = Array(81) { 0 }
        _totalBallCount.value = 0
        setEmptyBoard()
        resetRemovalSet()
        selectTheBall(null)
    }

    fun resetRemovalSet(){
        _setToRemove.value = mutableSetOf()
    }

    companion object {
        const val gridSize = 9
        const val ballLimitToRemove = 5

        // Define possible movements (row_offset, column_offset)
        val movements = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )

        val directionsToSearchSets = listOf(
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN_RIGHT,
            Direction.DOWN_LEFT
        )
    }

    fun addBall(index: Int, colorCode: Int): Boolean {
        if (totalBallCount.value == (gridSize * gridSize)) return false
        // Create a new array by copying the current one
        val newBallList = _ballList.value.copyOf()
        // Update the value in the new array
        newBallList[index] = colorCode
        // Emit the new array to the StateFlow
        _ballList.value = newBallList
        _totalBallCount.value += 1
        return true
    }

    private fun positionToIndex(row: Int, column: Int): Int {
        val index = (row * gridSize) + column
        Log.d("positionToIndex", "row: $row, column: $column to index: ${index}")
        return index
    }

    private fun indexToPosition(index: Int): Array<Int> {
        val column = (index) % gridSize
        val row = (index - column) / gridSize
        Log.d("indexToPosition", "index: $index to row: $row, column: $column")
        return arrayOf(row, column)
    }

    fun selectTheBall(index: Int?) {
        _selectedBall.value = index
    }

    fun getSelectedBall(): Int? {
        val ball = selectedBall.value?.let { ballList.value[it] }
        return if (ball == 0) null else ball
    }

    private fun removeBall(index: Int) {
//        TODO you can throw error if ball is not exists

        if (totalBallCount.value == 0) return
        if (ballList.value[index] == 0) return
        val copyOfBallList = _ballList.value.copyOf()
        copyOfBallList[index] = 0
        _ballList.value = copyOfBallList
        _totalBallCount.value -= 1
        Log.d("removeBall", "index: $index")
    }

    fun isSelectedBall(index: Int): Boolean {
        return selectedBall.value == index
        Log.d("isSelectedBall", "index: $index")

    }

    fun checkIfSelectedBallCanMove(destinationIndex: Int): List<Int>? {
        val selectedBallIndex = selectedBall.value
        val targetIndex = destinationIndex

        if (selectedBallIndex == null) {
            Log.d("checkIfSelectedBallCanMove", "SelectedBall index is null")
            return null
        }

        if (selectedBallIndex == targetIndex) {
            Log.d(
                "checkIfSelectedBallCanMove",
                "Ball is already on the target \nselectedBallIndex: $selectedBallIndex, targetIndex: $targetIndex"
            )
            return null
        }

        if (_ballList.value[targetIndex] != 0) {
            Log.d("checkIfSelectedBallCanMove", "no ball on the index $targetIndex")
            return null
        }

        // Perform Breadth-First Search (BFS) to find a path
        val queue: Queue<Int> = LinkedList()
        val visited = BooleanArray(81) { false }
        val parentMap = mutableMapOf<Int, Int>() // To reconstruct the path

        queue.add(selectedBallIndex)
        visited[selectedBallIndex] = true

        while (queue.isNotEmpty()) {
            val currentIndex = queue.poll()


            // If we reached the target, reconstruct and return the path
            if (currentIndex == targetIndex) {
                return reconstructPath(parentMap, selectedBallIndex, targetIndex)
            }

            // Get valid neighbors (horizontal, vertical, diagonal)
            val neighbors = getNeighbors(currentIndex)

            for (neighborIndex in neighbors) {
                // Check if the neighbor is within bounds, not visited, and is an empty square
                if (neighborIndex >= 0 && neighborIndex < 81 && !visited[neighborIndex] && _ballList.value[neighborIndex] == 0) {
                    visited[neighborIndex] = true
                    parentMap[neighborIndex] =
                        currentIndex // Store the parent for path reconstruction
                    queue.add(neighborIndex)
                }
            }
        }

        // If the queue is empty and the target was not reached, no path exists.
        return null
    }
//
//    private fun removeTheSeriesIfThereAre(): Boolean {
//        val ballsToRemove = findColorSeries(ballList.value.toList())
//        if (ballsToRemove.isEmpty()) return false
//
//        ballsToRemove.forEach { ballIndex ->
//            removeBall(ballIndex)
//        }
//        return true
//    }

    suspend fun findColorSeries(): Boolean {
        var result = false
        coroutineScope {
            _state.value = GameState.SearchingForSeries
            // Iterate through each potential starting cell using row and column
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    val startColor = ballList.value[positionToIndex(r, c)]

                    // Skip empty cells or invalid colors as starting points
                    if (startColor <= 0) continue

                    for (dir in directionsToSearchSets) {
//                         if down-left is the direction there should be enough space for the series
                        if (dir == Direction.DOWN_LEFT && ((c + r < 3) || (r + c > 12))) continue
//                         if down-right is the direction there should be enough space for the series
                        if (dir == Direction.DOWN_RIGHT && ((c - r > 5) || (r - c < 5))) continue

                        val currentSeries = mutableListOf<Int>()
                        currentSeries.add(positionToIndex(r, c)) // Start with the current cell

                        // Explore in the current direction
                        for (k in 1 until gridSize) { // Max possible extension length
                            val nextR = r + k * dir.rowOffset
                            val nextC = c + k * dir.colOffset
                            if (nextR > gridSize - 1
                                || nextC > gridSize - 1
                                || nextR < 0
                                || nextC < 0
                            )
                                continue

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

                        // If the series is long enough, add all its cells to the result set
                        if (currentSeries.size >= ballLimitToRemove) {
                            _setToRemove.value.addAll(currentSeries)
                            result = true
                        }
                    }
                }
            }
        }
        return result
    }

    suspend fun moveTheBall(path: List<Int>): Boolean {
//        todo throw error when color is 0 or null
        val color = getSelectedBall() ?: return false
        CoroutineScope(Dispatchers.IO).launch {
            path.forEach { index ->
                val selectedBallIndex = selectedBall.value
                if (index != selectedBallIndex) {
                    selectedBallIndex?.let {
                        addBall(index, color)
                        removeBall(it)
                    }
                    selectTheBall(index)
                    delay(50)
                }
            }
            selectTheBall(null)
        }.join()
        return false
    }

    /**
     * Gets the indices of the valid neighbors (horizontal, vertical, diagonal) for a given index.
     */
    private fun getNeighbors(index: Int): List<Int> {
        val neighbors = mutableListOf<Int>()
        val (row, column) = indexToPosition(index)
        for (direction in movements) {
            val neighborRow = row + direction.rowOffset
            val neighborColumn = column + direction.colOffset

            // Check if the neighbor is within the 9x9 grid bounds
            if (
                (neighborRow in 0..<gridSize)
                &&
                (neighborColumn in 0..<gridSize)
            ) {
                neighbors.add(positionToIndex(neighborRow, neighborColumn))
            }
        }

        return neighbors
    }


    /**
     * Reconstructs the path from the start index to the target index using the parent map.
     */
    private fun reconstructPath(
        parentMap: Map<Int, Int>,
        startIndex: Int,
        targetIndex: Int
    ): List<Int> {
        val path = mutableListOf<Int>()
        var currentIndex = targetIndex

        while (currentIndex != startIndex) {
            path.add(0, currentIndex) // Add to the beginning to get the path in order
            currentIndex =
                parentMap[currentIndex] ?: break // Should not be null if target was reached
        }
        path.add(0, startIndex) // Add the start index

        return path
    }

    fun add3Ball() {
        for (i in 0 until 3) {
            addBall(randomBall(), randomColor())
        }
    }

    private fun randomColor(): Int {
        return (1..6).random()
    }

    private fun randomBall(): Int {
        if (_totalBallCount.value == 81) return -1
        var randomNumber = (0..(81 - totalBallCount.value)).random()
        for ((index, each) in ballList.value.withIndex()) {
            if (each != 0) continue
            if (--randomNumber == 0) {
                return index
            }
        }
        return randomBall()
    }

    fun removeAllSeries() {
        setToRemove.value.forEach {
            removeBall(it)
        }
        resetRemovalSet()
    }

    fun getRemovables(): Set<Int> {
        return setToRemove.value
    }

}