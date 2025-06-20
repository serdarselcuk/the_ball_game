package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.utils.getRadialGradientBrush
import com.allfreeapps.theballgame.utils.toBallColor


@Composable
    fun FutureBalls(
        upcomingBalls: Array<Int>,
        modifier: Modifier = Modifier,
        isLandscape: Boolean = false
    ) {

        when (isLandscape) {
            false -> {
                Row(
                    modifier.height(25.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically // Optional: vertically center balls in the Row
                ) {
                    CreateFutureBalls(upcomingBalls)
                }
            }

            true -> {
                Column(
                    modifier.width(25.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start // Optional: horizontally start balls in the Column
                ) {
                    CreateFutureBalls(upcomingBalls)
                }
            }
        }
    }

    @Composable
    private fun CreateFutureBalls(upcomingBalls: Array<Int>) {
        val ballDisplaySize = 24.dp
        upcomingBalls.forEachIndexed { index, ballColorInt -> // Use forEachIndexed for keying Spacer
            val radialGradientBrush =  remember(ballDisplaySize.value, ballColorInt) {
                getRadialGradientBrush(
                    ballSizePx = ballDisplaySize.value,
                    baseColor = ballColorInt.toBallColor()
                )
            }
            Box(
                Modifier
                    .size(ballDisplaySize)
                    .clip(CircleShape) // Clip first
                    .background(
                        brush = radialGradientBrush,
                        shape = CircleShape // Apply border to the circle shape
                    )
            )
            if (index < upcomingBalls.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

@Preview(
    showBackground = true,
    name = "FutureBalls Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun FutureBallsPreview() {

    val score = arrayOf(687)
    TheBallGameTheme {
        FutureBalls(score)
    }
}


@Preview(
    showBackground = true,
    name = "FutureBalls Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
fun FutureBallsPreviewLandscape() {

    val score = arrayOf(687)
    TheBallGameTheme {
        FutureBalls(score)
    }
}
