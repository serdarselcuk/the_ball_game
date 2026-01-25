package com.allfreeapps.theballgame.ui.composables

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Markers
import com.allfreeapps.theballgame.utils.getRadialGradientBrush
import com.allfreeapps.theballgame.utils.toBallColor

private const val JUMP_DURATION: Int = 200
private const val BALL_CREATION_LATENCY: Int = 300
private const val SMALLER_SIZE = 0.1f
private const val EXPANDING_RATE = 1.2f
private val DEFAULT_SHADOW_ELEVATION = 8.dp
private val BALL_JUMP_HEIGHT = (-5).dp

@Composable
fun AnimatedBall(
    cellSize: Int,
    colorValue: Int,
    isBallSelected: Boolean,
    removeTheBall: (Int) -> Unit,
    gameSpeed: Int
) {
    val marker = Markers.get(colorValue) // 10 => marked for shrink , 20 => marked for expand
    fun onAnimationComplete(marker: Markers) =
        if (marker != Markers.BALL_NOT_MARKED) removeTheBall(marker.value) else null
    val color = colorValue % 10 // last digit is the color code (removing marker)
    val isCurrentlyABall = color != Constants.NO_BALL
    Log.d("AnimatedBall", "isCurrentlyABall: $isCurrentlyABall, color: $color")
    if (isCurrentlyABall) { // New ball
        Ball(
            colorValue = color,
            targetSize = when (marker) {
                Markers.BALL_SHRINKING -> SMALLER_SIZE
                Markers.BALL_EXPANSION -> cellSize.toFloat() * EXPANDING_RATE
                else -> cellSize.toFloat()
            }.dp,
            isBallSelected = isBallSelected,
            gameSpeed = gameSpeed,
            onAnimationComplete = { onAnimationComplete(marker) }
        )
    }
}

@Composable
fun Ball(
    colorValue: Int,
    targetSize: Dp,
    isBallSelected: Boolean = false,
    onAnimationComplete: () -> Unit? = {},
    gameSpeed: Int = 75
){
    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        animationSpec = tween(
            durationMillis = BALL_CREATION_LATENCY - gameSpeed,
            easing = LinearEasing
        ),
        finishedListener = { onAnimationComplete() }
    )

    // 1. Create the infinite transition
    val infiniteTransition = rememberInfiniteTransition(label = "BallJump")

    // 2. Define the animated value
    val jumpAnimationState by infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = if (isBallSelected) BALL_JUMP_HEIGHT else 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(JUMP_DURATION, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "JumpHeight"
    )

    val radialGradientBrush = LocalDensity.current.getRadialGradientBrush(
        animatedSize = targetSize,
        baseColor = colorValue.toBallColor()
    )

    Box(
        modifier = Modifier
            .offset(y = jumpAnimationState)
            .size(animatedSize)
            .shadow(
                elevation = DEFAULT_SHADOW_ELEVATION,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(brush = radialGradientBrush, shape = CircleShape)
    )
}


@Preview(showBackground = true)
@Composable
fun ShrinkingBallPreview() {

    Ball(
        colorValue = 22,
        isBallSelected = false,
        targetSize = 1.dp,
        onAnimationComplete = { },
        gameSpeed = 75
    )
}


@Preview(showBackground = true)
@Composable
fun ExpandingBallPreview() {
    Ball(
        colorValue = 32,
        isBallSelected = false,
        targetSize = 80.dp,
        onAnimationComplete = { },
        gameSpeed = 50
    )
}

@Preview(showBackground = true)
@Composable
fun NewBallPreview() {

    Ball(
        colorValue = 2,
        isBallSelected = true,
        targetSize = 50.dp,
        onAnimationComplete = { },
        gameSpeed = 50
    )
}