package com.allfreeapps.theballgame.ui.model

sealed class GameState {
    object BallMoving : GameState()
    object SearchingForSeries : GameState()
    object UserTurn : GameState()
    object GameOver : GameState()
    object GameNotStarted : GameState()
}