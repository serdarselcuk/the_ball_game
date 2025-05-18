package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer // Import Spacer
import androidx.compose.foundation.layout.padding // Optional: for spacing
import androidx.compose.foundation.layout.size // Import size
import androidx.compose.foundation.layout.width // Import width for Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment // Optional: for centering if needed
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    fun Build() {
        val upcomingBalls by viewModel.upcomingBalls.collectAsState()
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically // Optional: vertically center balls in the Row
            // modifier = Modifier.padding(8.dp) // Optional: add some padding around the Row
        ) {
            upcomingBalls.forEachIndexed { index, ballColorInt -> // Use forEachIndexed for keying Spacer
                Box(
                    Modifier
                        .size(24.dp) // <<<< ADD THIS: Define the size of each ball
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
}

@Preview(showBackground = true)
@Composable
fun FutureBallsPreview() {
    val mockViewModel = BallGameViewModel().apply {
      add3Ball() // Make sure this function correctly updates `upcomingBalls` StateFlow
    }

    TheBallGameTheme {
        FutureBalls(mockViewModel).Build()
    }
}