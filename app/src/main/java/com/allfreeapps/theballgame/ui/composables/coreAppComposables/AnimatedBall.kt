package com.allfreeapps.theballgame.ui.composables.coreAppComposables

`import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.theme.reflectingSunLightColorOnTheBall
import com.allfreeapps.theballgame.utils.getRadialGradientBrush


@Composable
fun AnimatedWelcomingBall(
    modifier: Modifier,
    ballColor: Color = MaterialTheme.colorScheme.tertiary,
    initialSize: Dp = 0.dp,
    targetSize: Dp,
    gameSpeed: Int = 1000,
    animationCompleted: () -> Unit = {},
    textOnTheBall: String? = null
) {
    var animateNow by remember { mutableStateOf(false) }

    LaunchedEffect(initialSize, targetSize) {
        if (targetSize != initialSize) animateNow = true
    }

    val animatedSize by animateDpAsState(
        targetValue = if (animateNow) targetSize else initialSize, // Animate from initialSize to targetSize
        animationSpec = tween(durationMillis = gameSpeed, easing = LinearOutSlowInEasing),
        finishedListener = {
            animationCompleted()
        }
    )

    val ballBackGroundColor = reflectingSunLightColorOnTheBall

    val radialGradient = LocalDensity.current.getRadialGradientBrush(
        animatedSize = animatedSize,
        baseColor = ballColor
    )

    Box(
        modifier
            .background(
                brush = radialGradient,
                shape = CircleShape
            )
            .size(animatedSize),
        contentAlignment = Alignment.Center
    ) {
        if (animatedSize > 0.dp) {
            textOnTheBall?.let {
                GameText(
                    text = it
                        .replace(" ", "\n")
                        .removeSurrounding("[", "]"),
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val strokeWidth = (animatedSize / 30).toPx()
                drawArc(
                    color = ballBackGroundColor,
                    startAngle = 210f,
                    sweepAngle = 30f,
                    useCenter = false,

                    topLeft = Offset(strokeWidth, strokeWidth),
                    size = this.size.copy(
                        width = this.size.width - strokeWidth,
                        height = this.size.height - strokeWidth
                    ),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
    }
}