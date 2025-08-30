package com.allfreeapps.theballgame.model

import androidx.compose.ui.unit.IntOffset
import com.allfreeapps.theballgame.R

interface LearningMessages {
    companion object {
        val BEFORE_START = mutableMapOf(
            R.string.first_message to IntOffset(50, 210),
            R.string.second_message to IntOffset(50, 450),
            R.string.third_message to IntOffset(50, 450),
            R.string.forth_message to IntOffset(50, 450)
        )
        val AFTER_START = mutableMapOf(
            R.string.sixth_message to IntOffset(250, 250),
            R.string.seventh_message to IntOffset(250, 250),
            R.string.eight_message to IntOffset(350, 350)
        )
    }
}