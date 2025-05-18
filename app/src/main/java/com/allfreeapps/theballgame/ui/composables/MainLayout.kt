package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainLayout (
    val viewModel: BallGameViewModel
){
    private lateinit var board: Board
    private lateinit var buttons: Buttons
    private lateinit var header: Header
    private lateinit var scoreBoard: ScoreBoard
    private lateinit var futureBalls: FutureBalls


    @Composable
    fun Build(
        modifier: Modifier,
    ) {
        board = Board(viewModel)
        buttons = Buttons()
        header = Header(viewModel, buttons)
        scoreBoard = ScoreBoard(viewModel)
        futureBalls = FutureBalls(viewModel)
        val orientation = LocalConfiguration.current.orientation

        when(orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    header.build()
                    board.layout()
                    Spacer(Modifier.height(5.dp))
                    futureBalls.Build()
                    Spacer(Modifier.height(5.dp))
                    scoreBoard.get()
                }
            } else -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                header.build()

                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    board.layout()
                    Spacer(Modifier.height(5.dp))
                    futureBalls.Build()
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
        }
    }

}

private val mockViewModel = BallGameViewModel().apply {
    addBall(57, 1)
    addBall(21, 2)
    addBall(35, 3)
    addBall(36, 4)
    addBall(37, 5)
    addBall(39, 6)
    addOldScores(
        com.allfreeapps.theballgame.ui.model.Scores(
            123,
            "player_1",
            1234,
            "2023-01-01"
        )
    )
    addOldScores(
        com.allfreeapps.theballgame.ui.model.Scores(
            124,
            "player_2",
            1230,
            "2023-01-01"
        )
    )
    addOldScores(
        com.allfreeapps.theballgame.ui.model.Scores(
            125,
            "player_3",
            1236,
            "2023-01-01"
        )
    )
    selectTheBall(37)
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun portraitPreview() {
    val mainLayOut = MainLayout(mockViewModel)
    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            mainLayOut.Build( Modifier.padding(innerPadding))
        }
    }
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
private fun landscapePreview() {
    val mainLayOut = MainLayout(mockViewModel)
    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            mainLayOut.Build( Modifier.padding(innerPadding))
        }
    }
}
