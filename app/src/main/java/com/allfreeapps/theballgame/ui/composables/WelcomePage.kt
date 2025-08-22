package com.allfreeapps.theballgame.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.utils.toBallColor
import com.allfreeapps.theballgame.viewModels.WelcomeScreenViewModel


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    onStartButtonClicked: () -> Unit = {}
) {
    viewModel.setState(GameState.GAME_NOT_STARTED)
    val isMuted by viewModel.isMuted.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var buttonCreated by remember { mutableStateOf(false) }
        Header(
            Modifier
                .fillMaxWidth()
                .height(90.dp),
            listOf(
                {
                    MuteButton(
                        isMuted = isMuted,
                        onToggleMute = {
                            viewModel.changeSoundStatus()
                        }
                    )
                },
                {
                    SettingsButton(
                        Modifier.width(50.dp),
                        onClick = {
                            viewModel.playClickSound()
                            onSettingsClicked()
                        }
                    )
                }
            )
        )
        Spacer(modifier = Modifier.width(50.dp))

        // to draw a round button with text having same effect on the picture
        StartButton(
            restartButtonOnClick = onStartButtonClicked,
            initiateAnimatingForBoard = { buttonCreated = true }
        )

        WelcomingBoard(
            modifier = Modifier.fillMaxSize(),
            initiateAnimatingForBoard = buttonCreated
        )


    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    restartButtonOnClick: () -> Unit = {},
    initiateAnimatingForBoard: () -> Unit = {}
) {
    val showText = remember { mutableStateOf(false) }
    val targetSize = 90.dp

    AnimatedWelcomingBall(
        modifier = modifier,
        color = StartButtonBackgroundColor,
        targetSize = targetSize,
        content = {
            if (showText.value) {
                ButtonWithText(
                    Modifier
                        .padding(2.dp)
                        .width(targetSize)
                        .height(targetSize),
                    buttonText = stringResource(R.string.start_game),
                    onclick = restartButtonOnClick
                )
            }
        },
        animationCompleted = {
            showText.value = true
            initiateAnimatingForBoard()
        }
    )
}


@Composable
fun WelcomingBoard(
    modifier: Modifier,
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
    initiateAnimatingForBoard: Boolean = false
) {
    val ballList by viewModel.welcoming_ballList.collectAsState()

    if (initiateAnimatingForBoard) {
        Box(
            modifier = modifier,
            content = {
                ballList.forEachIndexed { index, ballData ->
                    key(ballData.hashCode()) {
                        AnimatedWelcomingBall(
                            modifier = Modifier.offset(
                                x = (ballData.position[0] * 0.5f).dp,
                                y = (ballData.position[1] * 0.5f).dp
                            ),
                            color = ballData.colorValue.toBallColor(),
                            gameSpeed = ballData.gameSpeed,
                            targetSize = ballData.targetSize.dp,
                            animationCompleted = {
                                viewModel.changeBall(index)
                            },
                            content = {}
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun AnimatedWelcomingBall(
    modifier: Modifier,
    color: Color,
    initialSize: Dp = 0.dp,
    targetSize: Dp,
    gameSpeed: Int = 1000,
    animationCompleted: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var animateNow by remember { mutableStateOf(false) }

    LaunchedEffect(initialSize, targetSize, gameSpeed) {
        animateNow = true
    }

    val animatedSize by animateDpAsState(
        targetValue = if (animateNow) targetSize else initialSize, // Animate from initialSize to targetSize
        animationSpec = tween(durationMillis = gameSpeed, easing = LinearOutSlowInEasing),
        finishedListener = {
            animationCompleted()
        }
    )

    Box(
        modifier
            .background(color, shape = CircleShape)
            .size(animatedSize),
        contentAlignment = Alignment.Center
    ) {
        if (animatedSize > 0.dp) {
            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val strokeWidth = 15.dp.toPx()
                drawArc(
                    color = Color.White,
                    startAngle = 210f,
                    sweepAngle = 30f,
                    useCenter = false,

                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = this.size.copy(
                        width = this.size.width - strokeWidth,
                        height = this.size.height - strokeWidth
                    ),
                    style = Stroke(width = strokeWidth / 1.5f, cap = StrokeCap.Round)
                )
            }
        }
        content()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Welcome Page",
    device = "spec:width=1200dp,height=2400dp,orientation=portrait"
)
@Composable
fun PreviewWelcomePagePortrait() {
    WelcomingBoard(
        modifier = Modifier.offset(50.dp, 50.dp),
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Welcome Page",
    device = "spec:width=800dp,height=2400dp,orientation=landscape"
)
@Composable
fun PreviewWelcomePageLandscape() {
//    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})
//    StartScreenBackground(Modifier.fillMaxWidth(), Configuration.ORIENTATION_LANDSCAPE)
}