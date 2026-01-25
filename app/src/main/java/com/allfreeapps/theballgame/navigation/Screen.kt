package com.allfreeapps.theballgame.navigation

sealed class Screen(val route: String) {
    object Settings : Screen(ScreenOf.SETTINGS)
    object Welcome : Screen(ScreenOf.WELCOME)
    object Game : Screen(ScreenOf.GAME)
    object GameOver : Screen("${ScreenOf.GAME_OVER}/{score}") {
        fun createRoute(score: Int) = "${ScreenOf.GAME_OVER}/$score"
    }

    object Scores : Screen(ScreenOf.SCORES)

}

interface ScreenOf {
    companion object {
        const val SETTINGS = "settings"
        const val WELCOME = "welcome"
        const val GAME = "game"
        const val GAME_OVER = "game_over"
        const val SCORES = "scores"
    }
}