package com.allfreeapps.theballgame.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.BuildConfig

class Constants {
    companion object {
        val JUMP_HEIGHT: Dp = (-5).dp
        const val BALL_MOVEMENT_LATENCY: Long = 35
        const val NO_BALL = 0
        const val GRID_SIZE = BuildConfig.GRID_SIZE
        const val BALL_LIMIT_TO_REMOVE = BuildConfig.BALL_LIMIT_TO_REMOVE
        const val DATABASE_NAME = "score_database"
        const val MAX_BALL_COUNT: Int = BuildConfig.GRID_SIZE * BuildConfig.GRID_SIZE
    }
}