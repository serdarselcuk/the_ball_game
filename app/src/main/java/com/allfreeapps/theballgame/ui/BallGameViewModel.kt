package com.allfreeapps.theballgame.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BallGameViewModel : ViewModel() {
    private val _ballList = MutableStateFlow(Array(81) { 0 })
    val ballList: StateFlow<Array<Int>> = _ballList

    private val _selectedBall = MutableStateFlow<Int?>(null)
    val selectedBall: StateFlow<Int?> = _selectedBall

    fun addBall(index: Int, color: Int) {
        _ballList.value[index] = color
    }

    fun positionToIndex(row: Int, column: Int): Int {
        return row * 9 + column
    }

    fun indexToPosition(index: Int): Array<Int> {
        val column = index % 9
        val row = (index - column) / 9
        return arrayOf(row, column)
    }

    fun selectTheBall(index: Int) {
        selectedBall.value?.let {
            _selectedBall.value = index
        }
    }

    fun getSelectedBall(): Int? {
        return selectedBall.value
    }

    fun removeBall(index: Int) {
        _ballList.value[index] = 0
    }

    fun isSelectedBall(index: Int): Boolean {
        return selectedBall.value == index

    }

    fun checkIfSelectedBallCanMove(indice: Int) {
//        TODO() what to do
    }
}