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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.LightGray
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun MainLayout(
    modifier: Modifier,
    isMuted: Boolean = false,
    orientation: Int,
    gameStatus: GameState?,
    ballList: Array<Int>,
    selectedBallIndex: Int?,
    score: Int,
    upcomingBalls: Array<Int>,
    allScores: List<Score>,
    changeSoundStatus: () -> Unit = {},
    restartButtonOnClick: () -> Unit = {},
    onCellClick: (Int) -> Unit = {},
    removeTheBall: (Int) -> Unit = {},
    onDeleteClicked: (Int?) -> Unit = {}
) {

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
                    .height(
                        totalAvailableHeight * (
                            when (orientation) {
                                Configuration.ORIENTATION_LANDSCAPE -> 0.1f
                                else -> 0.05f
                            })
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
                            onToggleMute = { changeSoundStatus() }
                        )
                    },
                    {
                        gameStatus?.let {
                            RestartButton(it,
                                onclick = { restartButtonOnClick() })
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
                            onCellClick(index)
                        },
                        removeTheBall = { index ->
                            removeTheBall(index)
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
                        allScores,
                        onDeleteClicked = { id ->
                            onDeleteClicked(id)
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
                            ballList = ballList,
                            selectedBallIndex = selectedBallIndex,
                            onCellClick = { index ->
                               onCellClick(index)
                            },
                            removeTheBall = { index ->
                                removeTheBall(index)
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
                                allScores,
                                onDeleteClicked = {id->
                                    onDeleteClicked(id)
                                }
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
//
//@Preview(
//    showBackground = true,
//    name = "MainLayout Landscape (Device Spec)",
//    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
//)
//@Composable
//fun PortraitPreview() {
//
//    TheBallGameTheme {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            MainLayout(mockViewModel(LocalContext.current), Modifier.padding(innerPadding))
//        }
//    }
//}
//
//@Preview(
//    showBackground = true,
//    name = "MainLayout Landscape (Device Spec)",
//    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
//)
//@Composable
//private fun LandscapePreview() {
//
//    TheBallGameTheme {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            MainLayout(mockViewModel(LocalContext.current), Modifier.padding(innerPadding))
//        }
//    }
//}
