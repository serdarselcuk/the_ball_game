package com.allfreeapps.theballgame.navigation

sealed class Screen(val route: String) {
    object Settings : Screen("settings")
    object Welcome : Screen("welcome")
    object Game : Screen("game")
    object GameOver : Screen("gameOver")
    object Scores : Screen("scores")
}