package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.AnimatedBallButton
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.AnimatedWelcomingBall
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
        var buttonCreationIsCompleted by remember { mutableStateOf(false) }
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

        // to draw a round button with text having same effect on the picture
        StartButton(
            modifier = Modifier,
            initiateAnimatingForBoard = { buttonCreationIsCompleted = true },
            onStartButtonClicked = {
                onStartButtonClicked()
            }
        )

        WelcomingBoard(
            modifier = Modifier.fillMaxSize(),
            initiateAnimatingForBoard = buttonCreationIsCompleted
        )


    }
}

@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    initiateAnimatingForBoard: () -> Unit = {},
    onStartButtonClicked: () -> Unit = {}
) {

    AnimatedBallButton(
        modifier = modifier,
        targetSize = 90.dp,
        animationCompleted = {
            initiateAnimatingForBoard()
        },
        textOnTheBall = stringResource(R.string.start_game),
        onClick = { onStartButtonClicked() }
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
                            ballColor = ballData.colorValue.toBallColor(),
                            gameSpeed = ballData.gameSpeed,
                            targetSize = ballData.targetSize.dp,
                            animationCompleted = {
                                viewModel.changeBall(index)
                            }
                        )
                    }
                }
            }
        )
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Welcome Page Portrait",
//    device = "spec:width=1200dp,height=2400dp,orientation=portrait"
//)
//@Composable
//fun PreviewWelcomePagePortrait() {
//    WelcomingBoard(
//        modifier = Modifier.offset(50.dp, 50.dp),
//    )
//}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Welcome Page Start button",
    device = "spec:width=800dp,height=2400dp,orientation=portrait"
)
@Composable
fun PreviewWelcomePageStartButton() {

}
