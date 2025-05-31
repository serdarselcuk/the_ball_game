package com.allfreeapps.theballgame.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.ScoreLine // Assuming ScoreLine is a List<Color>
// It's better to import these directly if they are top-level constants in your theme package
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.toBallColor


private const val BALL_SIZE_RATIO = 0.8f // Ratio of ball size to cell size
private val DEFAULT_SHADOW_ELEVATION = 18.dp
private val SELECTED_SHADOW_ELEVATION = 50.dp
private val BALL_JUMP_HEIGHT = (-5).dp
private val JUMP_DURATION: Int = 200

/**
 * Converts an integer value (presumably an index or type) to a specific ball color.
 * Relies on the `ScoreLine` list of colors.
 */


@Composable
fun Board(
    modifier: Modifier = Modifier,
    maxSizeOfBoard: Dp,
    ballList: Array<Int>,
    selectedBallIndex: Int? = -1,
    onEmptyCellClick: (Int) -> Unit,
    onBallCellClick: (Int) -> Unit
) {
    // State collection hoisted to the top-level composable for the board


    val smallBoxSize = maxSizeOfBoard / Constants.GRID_SIZE

    Layout(
        modifier = modifier
            .height(maxSizeOfBoard)
            .width(maxSizeOfBoard),
        content = {
            // Generate a Cell composable for each item in the ballList
            ballList.forEachIndexed { index, ballColorValue ->
                Cell(
                    ballColorValue = ballColorValue,
                    cellSize = smallBoxSize,
                    isSelected = selectedBallIndex == index,
                    onCellClick = {
                        when (ballColorValue) {
                            Constants.NO_BALL -> onEmptyCellClick(index)
                            else -> onBallCellClick(index)
                        }
                    }
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            // Calculate cell size in pixels once
            val roundedCellSizePx = smallBoxSize.roundToPx()

            // Define constraints for each cell
            val cellConstraints = constraints.copy(
                minWidth = roundedCellSizePx,
                minHeight = roundedCellSizePx,
                maxWidth = roundedCellSizePx,
                maxHeight = roundedCellSizePx
            )

            // Measure each cell (measurable)
            val placeables = measurables.map { measurable ->
                measurable.measure(cellConstraints)
            }

            // Define the layout size for the entire board
            layout(constraints.maxWidth, constraints.maxHeight) {
                var currentX = 0
                var currentY = 0
                placeables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(currentX, currentY)
                    currentX += roundedCellSizePx
                    // Move to the next row if the end of a row is reached
                    if (((index + 1) % Constants.GRID_SIZE) == 0) {
                        currentX = 0
                        currentY += roundedCellSizePx
                    }
                }
            }
        }
    )
}

@Composable
private fun Cell(
    ballColorValue: Int,
    cellSize: Dp,
    isSelected: Boolean,
    onCellClick: () -> Unit,
    modifier: Modifier = Modifier // Allow passing external modifiers
) {
    Box(
        modifier = modifier // Apply external modifiers first
            .size(cellSize) // Ensure the Box takes up the full cell size
            .background(BackgroundColor) // Use imported theme color
            .border(
                BorderStroke(
                    width = 0.5.dp,
                    color = CellBoarderColor // Use imported theme color
                )
            )
            .clickable(onClick = onCellClick), // More idiomatic clickable
        contentAlignment = Alignment.Center
    ) {
        Ball(
            colorValue = ballColorValue,
            ballDisplaySize = cellSize * BALL_SIZE_RATIO, // Use constant for ratio
            isBallSelected = isSelected
        )
    }
}

@Composable
private fun Ball(
    colorValue: Int,
    ballDisplaySize: Dp,
    isBallSelected: Boolean
) {
    if (colorValue != Constants.NO_BALL) {
        val jumpAnimationState = rememberBallJumpAnimationState(isBallSelected = isBallSelected)
        val currentJumpOffsetDp: Dp = BALL_JUMP_HEIGHT * jumpAnimationState.value

        // Consider remembering the brush if ballDisplaySize.value or colorValue doesn't change frequently
        // However, getRadialGradientBrush is not a composable, so direct remember isn't applicable here
        // unless you make it one or pass remembered inputs. For now, it's recalculated on recomposition.
        val radialGradientBrush =  remember(ballDisplaySize.value, colorValue) {
            getRadialGradientBrush(ballDisplaySize.value, colorValue.toBallColor())
        }
        Box(
            modifier = Modifier
                .offset(y = currentJumpOffsetDp)
                .size(ballDisplaySize)
                .shadow(
                    elevation = if (isBallSelected) SELECTED_SHADOW_ELEVATION else DEFAULT_SHADOW_ELEVATION,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(brush = radialGradientBrush)
        )
    }
}

/**
 * Creates a radial gradient brush for the ball's appearance.
 * This is a utility function, not a composable.
 */
private fun getRadialGradientBrush(ballSizePx: Float, baseColor: Color): Brush {
    // Create a slightly lighter color for the gradient center (highlight)
    // and a slightly darker for the edges.
    val centerColor = baseColor.copy(
        red = (baseColor.red * 1.2f).coerceAtMost(1f),
        green = (baseColor.green * 1.2f).coerceAtMost(1f),
        blue = (baseColor.blue * 1.2f).coerceAtMost(1f)
    )
    val edgeColor = baseColor.copy(
        red = baseColor.red * 0.8f,
        green = baseColor.green * 0.8f,
        blue = baseColor.blue * 0.8f
    )

    return Brush.radialGradient(
        colors = listOf(centerColor, baseColor, edgeColor),
        center = Offset(
            x = ballSizePx * 0.3f,
            y = ballSizePx * 0.3f
        ), // Adjusted for a more centered highlight
        radius = ballSizePx * 0.8f, // Adjusted for gradient spread
        tileMode = TileMode.Clamp
    )
}



@Composable
private fun rememberBallJumpAnimationState(isBallSelected: Boolean): Animatable<Float, AnimationVector1D> {
    val animatedJumpOffset = remember { Animatable(0f) }

    // Use LaunchedEffect to react to changes in isBallSelected
    // This coroutine will (re)launch whenever isBallSelected changes.
    LaunchedEffect(key1 = isBallSelected) {
        if (isBallSelected) {
            // Start the infinite jumping animation
            // The animation will run until this LaunchedEffect is cancelled
            // (e.g., isBallSelected becomes false, or the composable leaves the composition)
            animatedJumpOffset.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = JUMP_DURATION, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            // If not selected, or if the selection changes from true to false,
            // snap the ball back to its original position (0f offset).
            // This also cancels any ongoing animation from the 'if (isBallSelected)' block.
            animatedJumpOffset.snapTo(0f)
        }
    }
    return animatedJumpOffset
}

//
//@Composable
//@Preview(
//    showBackground = true,
//    name = "board preview",
//    device = "spec:width=3800dp,height=1800dp,dpi=240,orientation=portrait"
//)
//
//fun BoardPreview() {
//    val mockViewModel = BallGameViewModel().apply {
//        addBall(57, 5) // Add some balls for preview
//        addBall(21, 3)
//        addBall(35, 2)
//        addBall(10, 1)
//        addBall(45, 4)
//        selectTheBall(21) // Optionally pre-select a ball for previewing the jump
//    }
//    // It's good practice to wrap previews that use animations or complex state
//    // in a way that allows interaction or showcases the state.
//    // For a simple preview of the board:
//    Board(mockViewModel).Layout()
//
//    // For a more interactive preview, you might need a local state
//    // to simulate selection if your ViewModel isn't easily manipulated in Preview.
//}