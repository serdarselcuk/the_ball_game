package com.allfreeapps.theballgame.utils

enum class Markers(val value: Int) {
    BALL_NOT_MARKED(0),
    BALL_SHRINKING(1),
    BALL_EXPANSION(2);

    companion object {
        fun get(i: Int): Markers {
            return enumValues<Markers>().find { it.value == i/10 } ?: BALL_NOT_MARKED
        }

        fun markTheBall(marker: Markers, ballValue: Int): Int {
            return (marker.value*10) + ballValue
        }
    }
}