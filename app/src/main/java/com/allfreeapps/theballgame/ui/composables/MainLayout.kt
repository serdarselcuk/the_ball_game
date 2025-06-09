package com.allfreeapps.theballgame.ui.composables

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.LightGray
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

@Composable
fun MainLayout(
    viewModel: BallGameViewModel,
    modifier: Modifier, // From Scaffold
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val gameStatus by viewModel.state.collectAsState()
    val ballList by viewModel.ballList.collectAsState()
    val selectedBallIndex by viewModel.selectedBall.collectAsState()
    val score by viewModel.score.collectAsState()
    val upcomingBalls by viewModel.upcomingBalls.collectAsState()
    val allScores by viewModel.allScores.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val topScore = if (allScores.isNotEmpty()) allScores[0].score else 1

    val orientationIsLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE


    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        val totalAvailableHeight = maxHeight
        val totalAvailableWidth = maxWidth

        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                Modifier
                    .fillMaxWidth()
                    .height(totalAvailableHeight * if (orientationIsLandscape) 0.1f else 0.05f)
                    .background(HeaderBackGround),
                content = listOf(
                    {
                        if (orientationIsLandscape) {
                            ScoreBoard(Modifier, score)
                        }
                    },
                    {
                        MuteButton(
                            Modifier.padding(2.dp),
                            isMuted,
                            onToggleMute = { viewModel.changeSoundStatus() }
                        )
                    },
                    {
                        gameStatus?.let {
                            RestartButton(it,
                                onclick = { viewModel.restartButtonOnClick() })
                        }
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
                    Board(
                        Modifier
                            .fillMaxWidth()
                            .height(totalAvailableWidth),
                        boardSize = (totalAvailableWidth),
                        ballList = ballList,
                        selectedBallIndex = selectedBallIndex,
                        onCellClick = { index ->
                            viewModel.onCellClick(
                                ballList[index],
                                index
                            )
                        },
                        removeTheBall = { index ->
                            viewModel.removeBall(index)
                        }
                    )
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
                    ScoresTable(
                        Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
                        allScores
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
                            ballList = ballList,
                            selectedBallIndex = selectedBallIndex,
                            onCellClick = { index ->
                                viewModel.onCellClick(
                                    ballList[index],
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
                                Modifier
                                    .fillMaxWidth()
                                    .weight(0.7f),
                                allScores
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun mockViewModel(applicationContext: Context): BallGameViewModel {
    return BallGameViewModel(applicationContext.applicationContext as MyApplication).apply {
        setState(GameState.UserTurn)

        addBall(57, 1)
        addBall(21, 2)
        addBall(35, 3)
        addBall(36, 4)
        addBall(37, 5)
        addBall(39, 6)

        selectTheBall(37)
        increaseScoreFor(5)
        add3Ball()
        startGame()

    }
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun PortraitPreview() {

    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainLayout(mockViewModel(LocalContext.current), Modifier.padding(innerPadding))
        }
    }
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
private fun LandscapePreview() {

    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainLayout(mockViewModel(LocalContext.current), Modifier.padding(innerPadding))
        }
    }
}
