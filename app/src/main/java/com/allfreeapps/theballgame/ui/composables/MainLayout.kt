package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.LightGray
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun MainLayout(
    modifier: Modifier,
    viewModel: BallGameViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    gameOver: () -> Unit = {}
) {
    val isMuted by viewModel.isMuted.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation
    val score by viewModel.score.collectAsState()
    val upcomingBalls by viewModel.upcomingBalls.collectAsState()
    val allScores by viewModel.allScores.collectAsState()
    val topScore = if (allScores.isNotEmpty()) allScores[0].score else 1
    val orientationIsLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Log.d("MainLayout", "BoxWithConstraints: maxWidth=$maxWidth, maxHeight=$maxHeight")

        val state by viewModel.state.collectAsState()
        if (state == GameState.GAME_OVER) gameOver()
        val totalAvailableHeight = maxHeight
        val totalAvailableWidth = maxWidth

        Column(modifier = Modifier.fillMaxSize()) {
            val headerHeight = totalAvailableHeight * (
                    when (orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> 0.1f
                        else -> 0.05f
                    })

            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(HeaderBackGround),
                fontSize = (headerHeight * 0.45f).value,
                content = listOf(
                    {
                        if (orientationIsLandscape) {
                            ScoreBoard(Modifier, score)
                        }
                    },
                    {
                        MuteButton(
                            Modifier.padding(1.dp),
                            isMuted = isMuted,
                            onToggleMute = { viewModel.changeSoundStatus() }
                        )
                    },
                    {
                        SettingsButton(
                            modifier =Modifier
                                .width(50.dp)
                                .padding(1.dp),
                            onClick = {
                                viewModel.playClickSound()
                                onSettingsClicked()
                            }
                        )
                    },
                    {
                        ButtonWithText(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(90.dp)
                                .height(35.dp),
                            buttonText =
                            stringResource(
                                if (viewModel.state.value == GameState.GAME_NOT_STARTED) R.string.start_game
                                else R.string.restart_game
                            ),
                            onclick = {
                                viewModel.restartButtonOnClick()
                            }
                        )
                    },
                ),
                isLandscape = orientationIsLandscape
            )

            when (orientationIsLandscape) {
                false -> {

                    ComparableScoreLine(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGray),
                        maxSizeOfLine = totalAvailableWidth,
                        orientation = orientation,
                        score = score,
                        topScore = topScore
                    )
                    // future ball and score board will be in same row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ScoreBoard(
                            modifier = Modifier.width(totalAvailableWidth / 3f),
                            score = score
                        )
                        FutureBalls(
                            upcomingBalls = upcomingBalls,
                            modifier = Modifier
                        )
                    }

                    Board(
                        Modifier
                            .fillMaxWidth()
                            .height(totalAvailableWidth),
                        boardSize = (totalAvailableWidth),
                        onCellClick = { index ->
                            viewModel.onCellClick(index)
                        },
                        removeTheBall = { index ->
                            viewModel.removeBall(index)
                        }
                    )
                    ScoresTable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
                        scores = allScores,
                        onDeleteClicked = { id ->
                            viewModel.deleteScore(id)
                        }
                    )
                }

                true -> {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        ComparableScoreLine(
                            modifier = Modifier
                                .weight(0.01f)
                                .fillMaxHeight()
                                .background(LightGray),
                            maxSizeOfLine = totalAvailableHeight,
                            orientation = orientation,
                            score = score,
                            topScore = topScore
                        )
                        Row(
                            Modifier
                                .fillMaxHeight()
                                .weight(0.025f),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FutureBalls(
                                upcomingBalls,
                                Modifier,
                                true
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Board(
                            Modifier
                                .width(totalAvailableHeight * 0.9f)
                                .fillMaxHeight(),
                            boardSize = (totalAvailableHeight * 0.9f ),

                            onCellClick = { index ->
                                viewModel.onCellClick(
                                    index
                                )
                            },
                            removeTheBall = { index ->
                                viewModel.removeBall(index)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(

                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(Modifier.height(4.dp))
                            ScoresTable(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.7f),
                                scores = allScores,
                                onDeleteClicked = {id->
                                    viewModel.deleteScore(id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
