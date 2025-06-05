package com.allfreeapps.theballgame.ui.composables

// It's better to import these directly if they are top-level constants in your theme package
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.Black
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.getRadialGradientBrush
import com.allfreeapps.theballgame.utils.toBallColor


private const val BALL_SIZE_RATIO = 0.8f // Ratio of ball size to cell size
private val DEFAULT_SHADOW_ELEVATION = 18.dp
private val SELECTED_SHADOW_ELEVATION = 50.dp
private val BALL_JUMP_HEIGHT = (-5).dp
private val JUMP_DURATION: Int = 200
val BALL_CREATION_LATENCY: Int = 200

/**
 * Converts an integer value (presumably an index or type) to a specific ball color.
 * Relies on the `ScoreLine` list of colors.
 */


@Composable
fun Board(
    modifier: Modifier = Modifier,
    boardSize: Dp,
    ballList: Array<Int>,
    selectedBallIndex: Int? = -1,
    onCellClick: (Int) -> Unit,
) {

    val smallBoxSize = boardSize / Constants.GRID_SIZE

    Layout(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color.Gray
            ),
        content = {
            // Generate a Cell composable for each item in the ballList
            ballList.forEachIndexed { index, ballColorValue ->
                Cell(
                    ballColorValue = ballColorValue,
                    cellSize = smallBoxSize,
                    isSelected = selectedBallIndex == index,
                    onCellClick = { onCellClick(index) }
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            // Calculate cell size in pixels once
            val roundedCellSizePx = (smallBoxSize).roundToPx()

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
    modifier: Modifier = Modifier
) {
    val boarderBrush = getRadialGradientBrush(
        ballSizePx = cellSize.value,
        xRate = 1f,
        yRate = 1f,
        radius = 0.5f,
        baseColor = CellBoarderColor
    )

    Box(
        modifier = modifier
            .size(cellSize)
            .background(BackgroundColor)
            .border(
                BorderStroke(
                    width = 2.dp,
                    brush = boarderBrush
                )
            )
            .clickable(onClick = onCellClick),
        contentAlignment = Alignment.Center
    ) {
        Ball(
            colorValue = ballColorValue,
            ballDisplaySize = cellSize * BALL_SIZE_RATIO,
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
        val animatedSize = remember { Animatable(5f) }
        LaunchedEffect(Unit) {
            animatedSize.animateTo(
                targetValue = ballDisplaySize.value,
                animationSpec = tween(durationMillis = BALL_CREATION_LATENCY, easing = LinearEasing)
            )
        }
        val jumpAnimationState = rememberBallJumpAnimationState(isBallSelected = isBallSelected)
        val currentJumpOffsetDp: Dp = BALL_JUMP_HEIGHT * jumpAnimationState.value

        val radialGradientBrush = remember(ballDisplaySize.value, colorValue) {
            getRadialGradientBrush(
                ballSizePx = ballDisplaySize.value,
                baseColor = colorValue.toBallColor()
            )
        }
        Box(
            modifier = Modifier
                .offset(y = currentJumpOffsetDp)
                .size(animatedSize.value.dp)
                .shadow(
                    elevation = if (isBallSelected) SELECTED_SHADOW_ELEVATION else DEFAULT_SHADOW_ELEVATION,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(brush = radialGradientBrush)
        )
    }
}


@Composable
private fun rememberBallJumpAnimationState(isBallSelected: Boolean): Animatable<Float, AnimationVector1D> {
    val animatedJumpOffset = remember { Animatable(0f) }

    LaunchedEffect(key1 = isBallSelected) {
        if (isBallSelected) {
            animatedJumpOffset.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = JUMP_DURATION, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            animatedJumpOffset.snapTo(0f)
        }
    }
    return animatedJumpOffset
}

@Composable
@Preview(
    showBackground = true,
    name = "board preview",
    device = "spec:width=3800dp,height=1800dp,dpi=240,orientation=portrait"
)

fun BoardPreview() {
    Board(
        Modifier,
        800.dp,
        Array(81) { 0 }.apply {
            this@apply[2] = 1
            this@apply[3] = 2
            this@apply[4] = 3
            this@apply[5] = 4
            this@apply[6] = 5
            this@apply[7] = 6
        },
        3,
        {}
    )
}