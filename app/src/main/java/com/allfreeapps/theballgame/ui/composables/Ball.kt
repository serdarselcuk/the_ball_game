package com.allfreeapps.theballgame.ui.composables

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Markers
import com.allfreeapps.theballgame.utils.getRadialGradientBrush
import com.allfreeapps.theballgame.utils.toBallColor

private val DEFAULT_SHADOW_ELEVATION = 18.dp
private val SELECTED_SHADOW_ELEVATION = 25.dp
private val BALL_JUMP_HEIGHT = (-5).dp
const val BALL_CREATION_LATENCY: Int = 300
private const val JUMP_DURATION: Int = 200
private const val BALL_SIZE_RATIO = 0.8f
private const val initialBallSize = 5f
private const val expandingRate = 5f / 4f

@Composable
fun AnimatedBall(
    cellSize: Dp,
    colorValue: Int,
    isBallSelected: Boolean,
    removeTheBall: () -> Unit,
    gameSpeed: Int
) {
    val marker = Markers.get(colorValue) // 10 => marked for shrink , 20 => marked for expand
    val color = colorValue % 10 // last digit is the color code (removing marker)
    val isCurrentlyABall = color != Constants.NO_BALL
    Log.d("AnimatedBall", "isCurrentlyABall: $isCurrentlyABall, color: $color")
    if (isCurrentlyABall) { // New ball
        when (marker) {
            Markers.BALL_SHRINKING -> {// Ball removed
                Log.d("AnimatedBall", "Ball removed")
                Ball(
                    colorValue = color, // Should be invisible or a placeholder
                    initialSize = cellSize.value * BALL_SIZE_RATIO,
                    targetSize = 1f,
                    onAnimationComplete = removeTheBall,
                    gameSpeed = gameSpeed
                )
            }

            Markers.BALL_EXPANSION -> { // Ball expanded
                Log.d("AnimatedBall", "Ball expanded")
                Ball(
                    targetSize = cellSize.value * BALL_SIZE_RATIO * expandingRate,
                    initialSize = cellSize.value * BALL_SIZE_RATIO,
                    colorValue = color,
                    onAnimationComplete = removeTheBall,
                    gameSpeed = gameSpeed
                )
            }

            else -> {
                // New ball
                Log.d("AnimatedBall", "New ball")
                Ball(
                    colorValue = color,
                    initialSize = initialBallSize,
                    targetSize = cellSize.value * BALL_SIZE_RATIO,
                    isBallSelected = isBallSelected,
                    gameSpeed = gameSpeed
                )
            }
        }
    }
}

@Composable
fun Ball(
    colorValue: Int,
    initialSize: Float,
    targetSize: Float,
    isBallSelected: Boolean = false,
    onAnimationComplete: () -> Unit = {},
    gameSpeed: Int = 75
){
    val animatedSize = remember { Animatable(initialSize) }
    LaunchedEffect(targetSize, gameSpeed) {
        animatedSize.animateTo(
            targetValue = targetSize,
            animationSpec = tween(
                durationMillis = BALL_CREATION_LATENCY - (gameSpeed * 2),
                easing = LinearEasing
            )
        )
        onAnimationComplete() // Call after animation finishes
    }

    val jumpAnimationState = rememberBallJumpAnimationState(isBallSelected = isBallSelected)
    val currentJumpOffsetDp: Dp = BALL_JUMP_HEIGHT * jumpAnimationState.value

    val radialGradientBrush = remember(targetSize, colorValue) {
        getRadialGradientBrush(
            ballSizePx = targetSize,
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


@Composable
fun rememberBallJumpAnimationState(isBallSelected: Boolean): State<Float> {
    val animatedValue = remember { Animatable(0f) } // Initial state is 0f (baseline)

    // This spec is only used when isBallSelected is true
    val jumpAnimationSpec = remember(JUMP_DURATION) {
        infiniteRepeatable<Float>(
            animation = tween(durationMillis = JUMP_DURATION, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    }

    LaunchedEffect(isBallSelected) {
        if (isBallSelected) {

            // This handles the transition from unselected (at 0f) to selected.
            if (animatedValue.value != 0f && animatedValue.targetValue != 1f) {
                animatedValue.snapTo(0f)
            }
            // Animate to 1f for the jump
            animatedValue.animateTo(1f, jumpAnimationSpec)
        } else {
            // For unselected balls:
            // If it's currently running (i.e., was jumping), stop the animation.
            if (animatedValue.isRunning) {
                animatedValue.stop()
            }
            // Ensure it animates or snaps back to 0f (baseline, no jump).
            // This handles the transition from selected (jumping) to unselected.
            if (animatedValue.value != 0f) {
                // You can choose to animate or snap here.
                // Snapping is simpler if an animation isn't desired for deselection.
                animatedValue.snapTo(0f)
            }
        }
    }
    return animatedValue.asState()
}

@Preview(showBackground = true)
@Composable
fun ShrinkingBallPreview() {

    Ball(
        colorValue = 22,
        initialSize = 5f,
        isBallSelected = false,
        targetSize = 1f,
        onAnimationComplete = { },
        gameSpeed = 75
    )
}


@Preview(showBackground = true)
@Composable
fun ExpandingBallPreview() {
    Ball(
        colorValue = 32,
        initialSize = 5f,
        isBallSelected = false,
        targetSize = 8f,
        onAnimationComplete = { },
        gameSpeed = 50
    )
}

@Preview(showBackground = true)
@Composable
fun NewBallPreview() {

    Ball(
        colorValue = 2,
        initialSize = 1f,
        isBallSelected = true,
        targetSize = 5f,
        onAnimationComplete = { },
        gameSpeed = 50
    )
}