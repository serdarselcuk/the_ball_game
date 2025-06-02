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
import com.allfreeapps.theballgame.utils.Constants

@Composable
fun MainLayout(
    viewModel: BallGameViewModel,
    modifier: Modifier,
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
    val sizeOfScreen = if (orientationIsLandscape)
        configuration.screenHeightDp
    else configuration.screenWidthDp



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    )
    {
        Header(
            Modifier
                .fillMaxWidth()
                .height(
                    (sizeOfScreen * if (orientationIsLandscape) 0.1
                    else 0.12).dp
                )
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
                        RestartButton(
                            it,
                            onclick = {
                                viewModel.restartButtonOnClick()
                            }
                        )
                    }
                },
            ),
                isLandscape = orientationIsLandscape
        )

//        BoxWithConstraints(modifier = Modifier
//            .fillMaxWidth()
//            .weight(1f)
//        ) {
//            // maxHeight and maxWidth are available here from the BoxWithConstraints
//            val availableHeight = maxHeight
//            val availableWidth = maxWidth

            when (orientationIsLandscape) {
                false -> {
                    ComparableScoreLine(
                        modifier = Modifier.background(LightGray),
                        maxSizeOfLine = sizeOfScreen,
                        orientation = orientation,
                        score = score,
                        topScore = topScore
                    )
                    Board(
                        Modifier,
                        (sizeOfScreen * 0.95).dp,
                        ballList,
                        selectedBallIndex,
                        onCellClick = { index ->
                            viewModel.onCellClick(
                                index = index,
                                color = ballList[index]
                            )
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ScoreBoard(
                            modifier = Modifier
                                .width((sizeOfScreen / 3).dp),
                            score = score
                        )
                        FutureBalls(
                            upcomingBalls = upcomingBalls,
                            modifier = Modifier // This is fine, FutureBalls will take its content size
                        )
                    }
                    // Corrected Modifier for ScoresTable:
                    ScoresTable(
                        Modifier // Use a new Modifier instance
                            .fillMaxWidth() // Take full width
                            .weight(1f), // Fill remaining vertical space in the Column
                        allScores
                    )
                }

                true -> { // Landscape Mode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Group these items to the left
                        ComparableScoreLine(
                            modifier = Modifier.background(LightGray),
                            maxSizeOfLine = sizeOfScreen,
                            orientation = orientation,
                            score = score,
                            topScore = topScore
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Board(
                            Modifier,
                            (sizeOfScreen * 0.85).dp,
                            ballList,
                            selectedBallIndex,
                            onCellClick = { index ->
                                viewModel.onCellClick(
                                    ballList[index],
                                    index
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            FutureBalls(
                                upcomingBalls,
                                Modifier,
                                true
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        ScoresTable(Modifier.fillMaxSize(), allScores)
                    }
                }
            }
//        }
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
