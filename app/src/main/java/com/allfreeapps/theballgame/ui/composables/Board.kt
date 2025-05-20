package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.utils.Constants.Companion.gridSize
import com.allfreeapps.theballgame.utils.convertToColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class Board(
    val viewModel: BallGameViewModel = BallGameViewModel(),
) {
    companion object {
        const val NO_BALL = 0
        val JUMP_HEIGHT = (-5).dp
        val JUMP_DURATION = 200

    }

    @Composable
    fun Layout(modifier: Modifier = Modifier) {
        val cellCount = gridSize * gridSize
        val configuration = LocalConfiguration.current
        val orientationIsPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidthDp = configuration.screenWidthDp.dp
        val screenHeightDp = configuration.screenHeightDp.dp
        val minSizeOfMainSq = if (orientationIsPortrait) screenWidthDp else screenHeightDp - 40.dp
        val cellSize = (minSizeOfMainSq / gridSize)
        Layout(
            content = { Ball(cellSize) },
            modifier = modifier
                .height(minSizeOfMainSq)
                .width(minSizeOfMainSq),
            measurePolicy = { measurables, constraints ->
                val roundedCellSize = cellSize.roundToPx()
                val cellConstraints = constraints.copy(
                    minWidth = roundedCellSize,
                    minHeight = roundedCellSize,
                    maxWidth = roundedCellSize,
                    maxHeight = roundedCellSize
                )

                val placeables = measurables.map { measurable ->
                    measurable.measure(cellConstraints)
                }

                val sizeOfTheRow = sqrt(cellCount.toDouble()).toInt()

                layout(constraints.maxWidth, constraints.maxHeight) {
                    var x = 0
                    var y = 0
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(x, y)
                        x += roundedCellSize
                        if (((index + 1) % sizeOfTheRow) == 0) {
                            x = 0
                            y += roundedCellSize
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun Ball(cellSize: Dp) { // Assuming this is where you draw individual balls
        val ballList by viewModel.ballList.collectAsState()
        val selectedBallIndex by viewModel.selectedBall.collectAsState()

        ballList.forEachIndexed { index, ballColorInt -> // Renamed 'ball' to 'ballColorInt' for clarity

            Box( // This is the cell Box
                modifier = Modifier
                    // .size(cellSize) // Ensure the cell has a defined size
                    .background(BackgroundColor)
                    .border(
                        BorderStroke(
                            width = 0.5.dp,
                            color = CellBoarderColor
                        )
                    )
                    .clickable {
                        if (ballColorInt == NO_BALL) {

                            if (selectedBallIndex != null) {

                                val path = viewModel.checkIfSelectedBallCanMove(index)
                                if (path != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        viewModel.moveTheBall(path)
                                        val seriesFound = async {
                                            viewModel.findColorSeries(
                                                listOf(index)
                                            )
                                        }
                                        if (seriesFound.await()) {
                                            viewModel.removeAllSeries()
                                        } else {
                                            viewModel.populateUpcomingBalls()
                                        }
                                        viewModel.deselectTheBall()
                                    }
                                }

                            }
                        } else {
                            if (index != selectedBallIndex) {
                                viewModel.selectTheBall(index)
                            } else {
                                viewModel.deselectTheBall()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (ballColorInt != NO_BALL) {
                    val isSelected = index == selectedBallIndex
                    val currentJumpOffsetDp: Dp = JUMP_HEIGHT * getAnimation(isSelected).value
                    val ballActualColor = ballColorInt.convertToColor()
                    val ballSize = (cellSize * 0.8f) // Convert to pixels
                    val radialGradientBrush =
                        getRadialGradientBrush(ballSize.value, ballActualColor)

                    Box(
                        modifier = Modifier
                            .offset(y = currentJumpOffsetDp)
                            .size(cellSize * 0.8f) // The actual visible ball
                            .shadow(
                                elevation = if (isSelected) 30.dp else 18.dp,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .background(brush = radialGradientBrush) // Use the brush here
                    )
                }
            }
        }
    }

    private fun getRadialGradientBrush(ballSize: Float, color: Color): Brush {

        // Create a slightly lighter color for the gradient center (highlight)
        // and a slightly darker for the edges.
        val centerColor = color.copy(
            red = (color.red * 1.2f).coerceAtMost(1f),
            green = (color.green * 1.2f).coerceAtMost(1f),
            blue = (color.blue * 1.2f).coerceAtMost(1f)
        )
        val edgeColor = color.copy(
            red = color.red * 0.8f,
            green = color.green * 0.8f,
            blue = color.blue * 0.8f
        )

        return Brush.radialGradient(
            colors = listOf(centerColor, color, edgeColor),
            // You can adjust the center and radius for different effects
            center = Offset(x = ballSize * 2f, y = ballSize * 0.8f), // Example: offset highlight
            radius = ballSize * 0.9f, // Example: control gradient spread
            tileMode = TileMode.Clamp
        )
    }

    @Composable
    private fun getAnimation(isBallSelected: Boolean): Animatable<Float, *> {
        val animatedJumpOffset = remember { Animatable(0f) }
        LaunchedEffect(key1 = isBallSelected) {
            if (isBallSelected) {
                // Start the infinite jumping animation
                animatedJumpOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = JUMP_DURATION, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            } else {
                // If not selected, or no ball, snap back to 0 offset
                animatedJumpOffset.snapTo(0f)
            }
        }
        return animatedJumpOffset
    }
}

@Composable
@Preview(
    showBackground = true,
    name = "board preview",
    device = "spec:width=3800dp,height=1800dp,dpi=240,orientation=portrait"
)
fun BoardPreview() {
    val mockViewModel = BallGameViewModel().apply {
        addBall(57, 5) // Add some balls for preview
        addBall(21, 3)
        addBall(35, 2)
        addBall(10, 1)
        addBall(45, 4)
        selectTheBall(21) // Optionally pre-select a ball for previewing the jump
    }
    // It's good practice to wrap previews that use animations or complex state
    // in a way that allows interaction or showcases the state.
    // For a simple preview of the board:
    Board(mockViewModel).Layout()

    // For a more interactive preview, you might need a local state
    // to simulate selection if your ViewModel isn't easily manipulated in Preview.
}