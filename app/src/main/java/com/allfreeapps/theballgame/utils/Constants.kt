package com.allfreeapps.theballgame.utils

import com.allfreeapps.theballgame.BuildConfig

class Constants {
    companion object {
        const val NO_BALL = 0
        const val WELCOME_SCREEN_GRID_SIZE = 3
        const val GRID_SIZE = BuildConfig.GRID_SIZE
        const val BALL_LIMIT_TO_REMOVE = BuildConfig.BALL_LIMIT_TO_REMOVE
        const val DATABASE_NAME = "scores"
        const val MAX_BALL_COUNT: Int = BuildConfig.GRID_SIZE * BuildConfig.GRID_SIZE
        const val MAX_BALLS_ON_WELCOME_SCREEN: Int = 5

    }
}