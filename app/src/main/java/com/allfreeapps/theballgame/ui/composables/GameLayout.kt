package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.BuildConfig
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.LightGray
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun GameLayout(
    modifier: Modifier,
    viewModel: BallGameViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    gameOver: (int: Int) -> Unit = {}
) {
    val context = LocalContext.current
    val orientation = LocalContext.current.resources.configuration.orientation
    val isMuted by viewModel.isMuted.collectAsState()
    val score by viewModel.score.collectAsState()
    val upcomingBalls by viewModel.upcomingBalls.collectAsState()
    val allScores by viewModel.allScores.collectAsState()
    val orientationIsLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
    val scoresTableVisible = remember { mutableStateOf(false) }
    val weighOfScoreColumn = remember { mutableFloatStateOf(0.3f) }
    val weightOfSpace2 = remember { mutableFloatStateOf(2f) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Log.d("MainLayout", "BoxWithConstraints: maxWidth=$maxWidth, maxHeight=$maxHeight")

        LaunchedEffect(Unit) {
            viewModel.gameOverEvent.collect { finalScore ->
                gameOver(finalScore)
            }
        }
        val totalAvailableHeight = maxHeight
        val totalAvailableWidth = maxWidth

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            val showStartButtonOnTheHeader = remember(orientation, scoresTableVisible.value) {
                scoresTableVisible.value || orientationIsLandscape
            }

            val headerHeight = remember(orientation, scoresTableVisible.value) {
                totalAvailableHeight * (
                    when (orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> 0.1f
                        else -> {
                            if (scoresTableVisible.value) 0.05f
                            else 0.1f
                        }
                    }
                        )
            }

            val headerFontSize = remember(headerHeight, scoresTableVisible.value) {
                if (scoresTableVisible.value) headerHeight * 0.45f
                else headerHeight * 0.30f
            }

            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(HeaderBackGround),
                fontSize = headerFontSize.value,
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
                            onToggleMute = { if (BuildConfig.DEBUG) viewModel.sendLogs(context) else viewModel.changeSoundStatus() }
                        )
                    },
                    {
                        SettingsButton(
                            modifier = Modifier
                                .width(50.dp)
                                .padding(1.dp),
                            onClick = {
                                viewModel.playClickSound()
                                onSettingsClicked()
                            }
                        )
                    },

                    {
                        if (showStartButtonOnTheHeader) {
                            ButtonWithText(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(90.dp)
                                    .height(35.dp),
                                buttonText = stringResource(
                                    if (upcomingBalls.isEmpty()) R.string.start_game
                                    else R.string.restart_game
                                ),
                                onclick = {
                                    viewModel.restartButtonOnClick()
                                }
                            )
                        }
                    }

                ),
                isLandscape = orientationIsLandscape
            )

            Spacer(Modifier.weight(weightOfSpace2.floatValue * 0.1f))

            if (!showStartButtonOnTheHeader) {
                ButtonWithText(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(90.dp)
                        .height(90.dp)
                        .align(Alignment.CenterHorizontally),
                    buttonText = stringResource(
                        if (upcomingBalls.isEmpty()) R.string.start_game
                        else R.string.restart_game
                    ),
                    onclick = {
                        viewModel.restartButtonOnClick()
                    }
                )
            }

            Spacer(Modifier.weight(weightOfSpace2.floatValue * 0.2f))

            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {

                    ComparableScoreLine(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGray),
                        maxSizeOfLine = totalAvailableWidth,
                        orientation = orientation,
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

                }

                Configuration.ORIENTATION_LANDSCAPE -> {

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            Modifier
                                .fillMaxHeight()
                                .weight(0.1f),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.End
                        ) {
                            ComparableScoreLine(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(LightGray),
                                maxSizeOfLine = totalAvailableHeight,
                                orientation = orientation
                            )
                            FutureBalls(
                                modifier = Modifier
                                    .weight(0.3f),
                                upcomingBalls = upcomingBalls,
                                isLandscape = true
                            )
                        }
                        Board(
                            Modifier
                                .width(totalAvailableHeight * 0.9f)
                                .fillMaxHeight(),
                            boardSize = (totalAvailableHeight * 0.9f),

                            onCellClick = { index ->
                                viewModel.onCellClick(
                                    index
                                )
                            },
                            removeTheBall = { index ->
                                viewModel.removeBall(index)
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                }

                else -> {
                    Log.d("GameLayout", "Unknown orientation")
                }
            }

            Spacer(Modifier.weight(weightOfSpace2.floatValue))

            fun openScore(boolean: Boolean) = if (boolean) {
                scoresTableVisible.value = true
                weighOfScoreColumn.floatValue = 1.5f
                weightOfSpace2.floatValue = 0.1f
            } else {
                scoresTableVisible.value = false
                weighOfScoreColumn.floatValue = 0.3f
                weightOfSpace2.floatValue = 2f
            }

            Column(
                modifier = Modifier
                    .weight(weighOfScoreColumn.floatValue)
                    .fillMaxWidth()
                    .pointerInput(
                        Unit
                    ) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            openScore(dragAmount < -5)
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = { openScore(!scoresTableVisible.value) }
                        )
                        .align(Alignment.CenterHorizontally),
                    imageVector = if (scoresTableVisible.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = if (scoresTableVisible.value) "Swipe down to close" else "Pull up for scores",
                )
                Spacer(Modifier.height(4.dp))
                if (scoresTableVisible.value) {

                    ScoresTable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f),
                        scores = allScores,
                        onDeleteClicked = { id ->
                            viewModel.deleteScore(id)
                        }
                    )
                }
            }


        }
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.ORIENTATION_PORTRAIT
//)
//@Composable
//fun PreviewGameLayoutPortrait() {
//    val viewModel = BallGameViewModel(
//        settingsRepository = SettingsRepository(
//
//        ),
//        repository = TODO(),
//        vibrator = TODO(),
//        soundPlayerManager = TODO(),
//        appLogger = TODO()
//    )
//
//    GameLayout(
//        modifier = Modifier.fillMaxSize(),
//        viewModel = viewModel
//    )
//
//}
//
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.ORIENTATION_LANDSCAPE
//)
//@Composable
//fun PreviewGameLayoutLandscape() {
//    GameLayout(
//        modifier = Modifier.fillMaxSize()
//    )
//
//}



