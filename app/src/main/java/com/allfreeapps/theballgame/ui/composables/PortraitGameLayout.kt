package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.AnimatedBallButton
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun PortraitView(
    viewModel: BallGameViewModel,
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit
) {
    val isMuted by viewModel.isMuted.collectAsState()
    val score by viewModel.score.collectAsState()
    val upcomingBalls by viewModel.upcomingBalls.collectAsState()
    val gameState by viewModel.state.collectAsState()

    PortraitView(
        modifier = modifier,
        isMuted = isMuted,
        score = score,
        upcomingBalls = upcomingBalls,
        gameState = gameState,
        onSettingsClicked = onSettingsClicked,
        { viewModel.playClickSound() },
        { viewModel.changeSoundStatus() },
        { viewModel.restartButtonOnClick() }
    )

}

@Composable
fun PortraitView(
    modifier: Modifier = Modifier,
    isMuted: Boolean,
    score: Int,
    upcomingBalls: Array<Int>,
    gameState: GameState?,
    onSettingsClicked: () -> Unit,
    playClickSound: () -> Unit,
    changeSoundStatus: () -> Unit,
    restartButtonOnClick: () -> Unit
) {

    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth(),
            fontSize = 12F,
            content = listOf(
                {
                    MuteButton(
                        Modifier.padding(1.dp),
                        isMuted = isMuted,
                        onToggleMute = { changeSoundStatus() }
                    )
                },
                {
                    SettingsButton(
                        modifier = Modifier
                            .width(50.dp)
                            .padding(1.dp),
                        onClick = {
                            playClickSound()
                            onSettingsClicked()
                        }
                    )
                },
            ),
            isLandscape = false
        )

        AnimatedBallButton(
            modifier = Modifier,
            targetSize = 90.dp,
            textOnTheBall = stringResource(
                if (gameState == GameState.GAME_STARTED) R.string.restart_game
                else R.string.start_game
            ),
            onClick = {
                restartButtonOnClick()
            }
        )
        Spacer(Modifier
            .height(8.dp)
            .fillMaxWidth())
        ComparableScoreLine(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        )
        // future ball and score board will be in same row
        Spacer(Modifier
            .height(4.dp)
            .fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ScoreBoard(
                modifier = Modifier,
                score = score
            )
            FutureBalls(
                upcomingBalls = upcomingBalls,
                modifier = Modifier
            )
        }
        Spacer(Modifier
            .height(8.dp)
            .fillMaxWidth())
        Board(
            Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}

@Preview
@Composable
fun PortraitViewPreview() {
    remember { mutableStateOf(false) }
    PortraitView(
        modifier = Modifier.fillMaxSize(),
        isMuted = false,
        score = 157,
        upcomingBalls = Array(81) { 0 }.apply { this[2] = 1 },
        gameState = GameState.GAME_STARTED,
        onSettingsClicked = {},
        playClickSound = {},
        changeSoundStatus = {},
        restartButtonOnClick = {}
    )
}

