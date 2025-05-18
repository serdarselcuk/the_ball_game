package com.allfreeapps.theballgame.ui.model

data class Scores(
val id: Int,
val playerName: String,
val score: Int,
val date: String
):Comparable<Scores> {
    override fun compareTo(other: Scores): Int {
        return other.score.compareTo(this.score)
    }
}
