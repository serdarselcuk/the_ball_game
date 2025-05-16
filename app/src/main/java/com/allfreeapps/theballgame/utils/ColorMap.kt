package com.allfreeapps.theballgame.utils

import androidx.compose.ui.graphics.Color
import com.allfreeapps.theballgame.ui.theme.Blue
import com.allfreeapps.theballgame.ui.theme.Cyan
import com.allfreeapps.theballgame.ui.theme.Green
import com.allfreeapps.theballgame.ui.theme.Magenta
import com.allfreeapps.theballgame.ui.theme.Red
import com.allfreeapps.theballgame.ui.theme.White
import com.allfreeapps.theballgame.ui.theme.Yellow

class ColorMap {

    val colorArray= arrayOf(
        White,
        Red,
        Blue,
        Green,
        Yellow,
        Magenta,
        Cyan
    )

    fun get(index: Int): Color{
        return colorArray[index]
    }
}