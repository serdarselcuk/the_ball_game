package com.allfreeapps.theballgame.utils

import androidx.compose.ui.graphics.Color
import com.allfreeapps.theballgame.ui.theme.ScoreLine

fun Int.toBallColor(): Color = ScoreLine[this]