package com.allfreeapps.theballgame.ui.composables

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
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.LightGray

@Composable
fun MainLayout(
    modifier: Modifier,
    isMuted: Boolean = false,
    orientation: Int,
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
                        RestartButton(
                            modifier = Modifier
                                .padding(2.dp)
                                .width(90.dp)
                                .height(35.dp),
                            onclick = { restartButtonOnClick() })
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
                        scores = allScores,
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.7f),
                                scores = allScores,
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
