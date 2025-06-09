package com.allfreeapps.theballgame.utils

import com.allfreeapps.theballgame.BuildConfig

class Constants {
    companion object {
        const val NO_BALL = 0
        const val GRID_SIZE = BuildConfig.GRID_SIZE
        const val BALL_LIMIT_TO_REMOVE = BuildConfig.BALL_LIMIT_TO_REMOVE
        const val DATABASE_NAME = "scores"
        const val MAX_BALL_COUNT: Int = BuildConfig.GRID_SIZE * BuildConfig.GRID_SIZE
    }
}