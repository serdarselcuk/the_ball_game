package com.allfreeapps.theballgame.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import com.allfreeapps.theballgame.ui.theme.GameColorScale

fun Int.toBallColor(): Color {
    val num = this % 10 // to remove markers from the ball color
    return GameColorScale[num]
}


/**
 * Creates a radial gradient brush for the ball's appearance.
 * This is a utility function, not a composable.
 */
fun getRadialGradientBrush(
    ballSizePx: Float,
    xRate: Float = 1.2f,
    yRate: Float = 1.2f,
    radius:Float = 2f,
    baseColor: Color)
        : Brush {

    val centerColor = baseColor.copy(
        red = (baseColor.red * 1.3f).coerceAtMost(1f),
        green = (baseColor.green * 1.3f).coerceAtMost(1f),
        blue = (baseColor.blue * 1.3f).coerceAtMost(1f)
    )
    val edgeColor = baseColor.copy(
        red = baseColor.red * 0.7f,
        green = baseColor.green * 0.7f,
        blue = baseColor.blue * 0.7f
    )

    return Brush.radialGradient(
        colors = listOf(centerColor, baseColor, edgeColor),
        center = Offset(
            x = ballSizePx * xRate,
            y = ballSizePx * yRate
        ),
        radius = ballSizePx * radius,
        tileMode = TileMode.Mirror
    )
}