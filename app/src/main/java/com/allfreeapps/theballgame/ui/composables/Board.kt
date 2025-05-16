package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.utils.ColorMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Board {
    private val color = ColorMap()

    companion object{
        const val NO_BALL = 0
    }

    @Composable
    fun get(viewModel: BallGameViewModel){
        val ballList by viewModel.ballList.collectAsState()
        val selectedBallIndex by viewModel.selectedBall.collectAsState()
        val totalBallCount by viewModel.totalBallCount.collectAsState()

        ballList.forEachIndexed { indice, ball ->
            var backGroundColor = Color.White
            var boarderColor = Color.DarkGray
            if (ball != NO_BALL) {
                backGroundColor = color.get(ball)
                boarderColor =
                    if (viewModel.isSelectedBall(indice)) color.get(ball) else Color.DarkGray
            }
            Box(modifier = Modifier
                .background(backGroundColor)
                .border(
                    BorderStroke(
                        width = 0.5.dp,
                        color = boarderColor
                    )
                )
                .clickable {
                    if (ball == NO_BALL) {//empty cell
                        if (selectedBallIndex != null) { // if a ball is selected
                            val path = viewModel.checkIfSelectedBallCanMove(indice)
                            if (path != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    async { viewModel.moveTheBall(path) }.await()
                                    val seriesFound = async { viewModel.findColorSeries() }
                                    if (seriesFound.await()) {
                                        viewModel.removeAllSeries()
                                    } else {
                                        viewModel.add3Ball()
                                    }
                                    viewModel.selectTheBall(null)
                                }
                            }
                        }
                    } else {
                        if (indice != selectedBallIndex) {// if an unselected ball clicked
                            viewModel.selectTheBall(indice)
                        }
                    }
                }
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
fun BoardPreview(){
    val mockViewModel = BallGameViewModel().apply {
        addBall(57, 5)
        addBall(21, 3)
        addBall(35, 2)
    }
    Board().get(
        mockViewModel
    )
}