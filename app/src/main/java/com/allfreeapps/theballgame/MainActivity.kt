package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.Board
import com.allfreeapps.theballgame.ui.composables.Buttons
import com.allfreeapps.theballgame.ui.composables.FutureBalls
import com.allfreeapps.theballgame.ui.composables.Header
import com.allfreeapps.theballgame.ui.composables.ScoreBoard
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.utils.Constants.Companion.gridSize
import com.allfreeapps.theballgame.utils.convertToColor
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BallGameViewModel>()
    private lateinit var board: Board
    private lateinit var buttons: Buttons
    private lateinit var header: Header
    private lateinit var scoreBoard: ScoreBoard
    private lateinit var futureBalls: FutureBalls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        board = Board(viewModel)
        buttons = Buttons()
        header = Header(viewModel, buttons)
        scoreBoard = ScoreBoard(viewModel)
        futureBalls = FutureBalls(viewModel)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainLayOut(
                        Modifier.padding(innerPadding),
                        board =  board ,
                        futureBalls = futureBalls,
                        header =  header,
                        scoreBoard = scoreBoard
                    )
                }
            }
        }
    }
}

@Composable
fun MainLayOut(
    modifier: Modifier,
    board: Board,
    futureBalls: FutureBalls,
    header: Header,
    scoreBoard: ScoreBoard
) {
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val mockViewModel = BallGameViewModel().apply {
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

    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainLayOut(
                Modifier.padding(innerPadding),
                board =  Board(mockViewModel),
                header = Header(mockViewModel, Buttons()),
                futureBalls = FutureBalls(mockViewModel),
                scoreBoard = ScoreBoard(mockViewModel)
            )
        }
    }
}
