package com.allfreeapps.theballgame.ui.composables

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
import kotlinx.coroutines.delay

private val DEFAULT_SHADOW_ELEVATION = 18.dp
private val SELECTED_SHADOW_ELEVATION = 25.dp
private val BALL_JUMP_HEIGHT = (-5).dp
private const val JUMP_DURATION: Int = 200
const val BALL_CREATION_LATENCY: Int = 150
private const val DISAPPEARING_BALL_LATENCY = 150L
private const val BALL_SIZE_RATIO = 0.8f
private const val initialBallSize =  5f
private val expandingRate = 5f/4f

@Composable
fun AnimatedBall(
    cellSize: Dp,
    colorValue: Int,
    isBallSelected: Boolean,
    removeTheBall: () -> Unit
){
    val marker = Markers.get(colorValue) // 10 => marked for shrink , 20 => marked for expand
    val color = colorValue % 10 // last digit is the color code (removing marker)
    val isCurrentlyABall = color != Constants.NO_BALL

    if (isCurrentlyABall) { // New ball
        when(marker){
            Markers.BALL_SHRINKING -> {// Ball removed
                LaunchedEffect(Unit) {
                    delay(DISAPPEARING_BALL_LATENCY)
                    removeTheBall()
                }
                Ball(
                    colorValue = color, // Should be invisible or a placeholder
                    animatedSizeOfCreation = cellSize.value * BALL_SIZE_RATIO,
                    targetSize = 1f, // Shrink to nothing
                    isBallSelected = false
                )
            }

            Markers.BALL_EXPANSION -> { // Ball expanded
                LaunchedEffect(Unit) {
                    delay(DISAPPEARING_BALL_LATENCY) // Wait for animation to complete
                    removeTheBall()
                }
                ExplodingBall(
                    targetSize = cellSize.value * BALL_SIZE_RATIO * expandingRate,
                    initialSize = cellSize.value * BALL_SIZE_RATIO,
                    colorValue = color
                )
            }

            else -> {
                // New ball
                Ball(
                    colorValue = color,
                    animatedSizeOfCreation = initialBallSize,
                    targetSize = cellSize.value * BALL_SIZE_RATIO,
                    isBallSelected = isBallSelected
                )
            }
        }
    }
}

@Composable
fun Ball(
    colorValue: Int,
    animatedSizeOfCreation: Float,
    targetSize: Float,
    isBallSelected: Boolean
){
    val animatedSize = remember { Animatable(animatedSizeOfCreation) }
    LaunchedEffect(Unit) {
        animatedSize.animateTo(
            targetValue = targetSize,
            animationSpec = tween(durationMillis = BALL_CREATION_LATENCY, easing = LinearEasing)
        )
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
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(durationMillis =JUMP_DURATION, easing = LinearOutSlowInEasing),// how long will it take to jump, movement speed change
        repeatMode = RepeatMode.Reverse
    )
    val animatedValue = remember { Animatable(-10f) }

    LaunchedEffect(isBallSelected) {
        if (isBallSelected) {
            animatedValue.animateTo(1f, animationSpec) // the height of jump
        } else {
            animatedValue.snapTo(0f) // Stop and reset animation if not selected
        }
    }
    return animatedValue.asState()
}

@Composable
fun ExplodingBall(targetSize: Float, initialSize: Float, colorValue: Int) {
    val animatedSize = remember { Animatable(initialSize) }
    LaunchedEffect(Unit) {
        animatedSize.animateTo(
            targetValue = targetSize,
            animationSpec = tween(durationMillis = BALL_CREATION_LATENCY, easing = LinearEasing)
        )
    }

    val radialGradientBrush = remember(targetSize, colorValue) {
        getRadialGradientBrush(
            ballSizePx = targetSize,
            baseColor = colorValue.toBallColor()
        )
    }

    Box(
        modifier = Modifier
            .size(animatedSize.value.dp)
            .shadow(
                elevation = DEFAULT_SHADOW_ELEVATION, // No selection during explosion
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(brush = radialGradientBrush)
    )
}


@Preview(showBackground = true)
@Composable
fun BallPreview() {
//
//    Ball(
//        colorValue = 2,
//        animatedSizeOfCreation = 5.dp,
//        isBallSelected = true
//    )
}