package com.allfreeapps.theballgame.utils

import androidx.compose.ui.graphics.Color
import com.allfreeapps.theballgame.ui.theme.ScoreLine
import com.allfreeapps.theballgame.ui.theme.White

class ColorMap {

    val colorArray = ScoreLine.copyOf()
}

fun Int.convertToColor(): Color {
    val colorMap = Array(7) { White }
    for ((indice, color) in ScoreLine.withIndex()) {
        colorMap[indice + 1] = color
    }
    return colorMap[this]
}

