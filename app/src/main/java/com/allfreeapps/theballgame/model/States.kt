package com.allfreeapps.theballgame.ui.model

sealed class GameState {
    data object SearchingForSeries : GameState()
    data object UserTurn : GameState()
    data object GameOver : GameState()
    data object GameNotStarted : GameState()
}