package com.allfreeapps.theballgame.utils

import androidx.compose.ui.graphics.Color
import com.allfreeapps.theballgame.ui.theme.GameColorScale

fun Int.toBallColor(): Color = GameColorScale[this]