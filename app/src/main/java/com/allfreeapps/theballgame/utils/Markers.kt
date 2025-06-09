package com.allfreeapps.theballgame.utils

enum class Markers(val value: Int) {
    BALL_NOT_MARKED(0),
    BALL_EXPANSION_MARKER(20),
    BALL_SHRINKING_MARKER(10);

    companion object {
        fun get(i: Int): Markers {
            return enumValues<Markers>().find { it.value == i*10 } ?: BALL_NOT_MARKED
        }
    }
}