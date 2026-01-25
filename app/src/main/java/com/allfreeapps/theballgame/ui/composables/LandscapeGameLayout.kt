package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.AnimatedBallButton
import com.allfreeapps.theballgame.viewModels.BallGameViewModel


@Composable
fun LandscapeView(
    viewModel: BallGameViewModel,
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit
) {
    val isMuted by viewModel.isMuted.collectAsState()
    val score by viewModel.score.collectAsState()
    val upcomingBalls by viewModel.upcomingBalls.collectAsState()
    val gameState by viewModel.state.collectAsState()

    LandscapeView(
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
fun LandscapeView(
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


    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Header(
            modifier = Modifier
                .weight(0.3f),
            fontSize = 12f,
            content = listOf(
                {
                    ScoreBoard(Modifier, score)
                },
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
            isLandscape = true
        )

        Spacer(modifier = Modifier.weight(1f))

        ComparableScoreLine(
            modifier = Modifier
                .fillMaxHeight()
                .width(13.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )

        Board(
            Modifier
                .fillMaxSize(),
        )

        Spacer(Modifier.height(1.dp))

        FutureBalls(
            modifier = Modifier
                .weight(0.3f),
            upcomingBalls = upcomingBalls,
            isLandscape = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            Modifier.weight(0.5f),
        ) {
            AnimatedBallButton(
                modifier = Modifier
                    .padding(1.dp),
                targetSize = 90.dp,
                textOnTheBall = stringResource(if (gameState == GameState.GAME_STARTED) R.string.restart_game else R.string.start_game),
                onClick = {
                    restartButtonOnClick()
                }
            )
        }

    }


}
