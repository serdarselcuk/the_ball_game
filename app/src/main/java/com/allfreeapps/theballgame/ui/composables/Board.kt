package com.allfreeapps.theballgame.ui.composables

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.Board.Companion.JUMP_DURATION
import com.allfreeapps.theballgame.ui.composables.Board.Companion.NO_BALL
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.utils.ColorMap
import com.allfreeapps.theballgame.utils.Constants.Companion.gridSize
import com.allfreeapps.theballgame.utils.convertToColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class Board(
    val viewModel: BallGameViewModel = BallGameViewModel()
) {
    private val color = ColorMap()

    companion object {
        const val NO_BALL = 0
        val JUMP_HEIGHT = (-5).dp
        val JUMP_DURATION = 200

    }

    @Composable
    fun layout(){
        val cellCount = gridSize * gridSize
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val screenHeightDp = configuration.screenHeightDp.dp
        val minSizeOfMainSq = minOf(screenWidthDp, screenHeightDp)

        Layout(
            content = { get() },
            modifier = Modifier.height(minSizeOfMainSq),
            measurePolicy = { measurables, constraints ->
                val cellSize = (minSizeOfMainSq / gridSize).roundToPx()
                val cellConstraints = constraints.copy(
                    minWidth = cellSize,
                    minHeight = cellSize,
                    maxWidth = cellSize,
                    maxHeight = cellSize
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
                        x += cellSize
                        if (((index + 1) % sizeOfTheRow) == 0) {
                            x = 0
                            y += cellSize
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun get() {
        val ballList by viewModel.ballList.collectAsState()
        val selectedBallIndex by viewModel.selectedBall.collectAsState()

        ballList.forEachIndexed { indice, ball ->

            Box(
                modifier = Modifier
                    .background(BackgroundColor)
                    .border(
                        BorderStroke(
                            width = 0.5.dp,
                            color = CellBoarderColor
                        )
                    )
                    .clickable {
                        if (ball == NO_BALL) {
                            if (selectedBallIndex != null) {
                                val path = viewModel.checkIfSelectedBallCanMove(indice)
                                if (path != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        async { viewModel.moveTheBall(path) }.await()
                                        val seriesFound = async { viewModel.findColorSeries() }
                                        if (seriesFound.await()) {
                                            viewModel.removeAllSeries()
                                        } else {
                                            viewModel.populateUpcommingBalls()
                                        }
                                        viewModel.deselectTheBall() // Deselect after move
                                    }
                                }
                            }
                        } else {
                            if (indice != selectedBallIndex) {
                                viewModel.selectTheBall(indice)
                            } else {
                                viewModel.deselectTheBall()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (ball != NO_BALL) {
                    val currentJumpOffsetDp: Dp = JUMP_HEIGHT * getAnimation(selectedBallIndex == indice).value
                    Box(
                        modifier = Modifier
                            .offset(y = currentJumpOffsetDp) // Apply the jump offset here
                            .size(35.dp)
                            .shadow(
                                elevation = if (indice == selectedBallIndex) 8.dp else 4.dp, // More shadow when selected
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .background(ball.convertToColor())
                    )
                }
            }
        }
    }


    @Composable
    private fun getAnimation(isBallSelected:Boolean): Animatable<Float, *> {
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
@Preview(showBackground = true)
fun BoardPreview() {
    val mockViewModel = BallGameViewModel().apply {
        addBall(57, 5) // Add some balls for preview
        addBall(21, 3)
        addBall(35, 2)
        addBall(10, 1)
        addBall(45, 4)
        // selectTheBall(21) // Optionally pre-select a ball for previewing the jump
    }
    // It's good practice to wrap previews that use animations or complex state
    // in a way that allows interaction or showcases the state.
    // For a simple preview of the board:
    Board(mockViewModel).get()

    // For a more interactive preview, you might need a local state
    // to simulate selection if your ViewModel isn't easily manipulated in Preview.
}