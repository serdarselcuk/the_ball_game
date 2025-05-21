package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.utils.convertToColor

class FutureBalls(
    val viewModel: BallGameViewModel
) {

    @Composable
    fun Build(modifier: Modifier = Modifier) {
        val upcomingBalls by viewModel.upcomingBalls.collectAsState()
        val configuration = LocalConfiguration.current
        val orientation = configuration.orientation
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Row(
                    modifier.height(25.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically // Optional: vertically center balls in the Row
                ) {
                    CreateFutureBalls(upcomingBalls)
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier.width(25.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start // Optional: horizontally start balls in the Column
                ) {
                    CreateFutureBalls(upcomingBalls)
                }
            }

            else -> {}//nothing
        }
    }

    @Composable
    private fun CreateFutureBalls(upcomingBalls: Array<Int>) {
        upcomingBalls.forEachIndexed { index, ballColorInt -> // Use forEachIndexed for keying Spacer
            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape) // Clip first
                    .background(ballColorInt.convertToColor()) // Then background
                    .border(
                        BorderStroke(
                            width = 1.dp, // Increased width slightly for visibility
                            color = CellBoarderColor
                        ),
                        shape = CircleShape // Apply border to the circle shape
                    )
            )
            // Add a Spacer between balls, but not after the last one
            if (index < upcomingBalls.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
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
    val mockViewModel = BallGameViewModel(LocalContext.current).apply {
        add3Ball() // Make sure this function correctly updates `upcomingBalls` StateFlow
    }

    TheBallGameTheme {
        FutureBalls(mockViewModel).Build()
    }
}


@Preview(
    showBackground = true,
    name = "FutureBalls Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
fun FutureBallsPreviewLandscape() {
    val mockViewModel = BallGameViewModel(LocalContext.current).apply {
        add3Ball() // Make sure this function correctly updates `upcomingBalls` StateFlow
    }

    TheBallGameTheme {
        FutureBalls(mockViewModel).Build()
    }
}
