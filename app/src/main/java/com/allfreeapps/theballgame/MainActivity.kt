package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.Board
import com.allfreeapps.theballgame.ui.composables.Buttons
import com.allfreeapps.theballgame.ui.composables.ScoreBoard
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.utils.Constants.Companion.gridSize
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BallGameViewModel>()
    private lateinit var board: Board
    private lateinit var buttons: Buttons
    private lateinit var scoreBoard: ScoreBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        board = Board(viewModel)
        buttons = Buttons()
        scoreBoard = ScoreBoard(viewModel)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainLayOut(
                        Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        board =  board ,
                        button =  buttons,
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
    viewModel: BallGameViewModel,
    board: Board,
    button: Buttons,
    scoreBoard: ScoreBoard
) {
    val cellCount = gridSize * gridSize
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val minSizeOfMainSq = min(screenWidthDp, screenHeightDp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {
        Row(
            modifier = Modifier.height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                ),
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold,
                color = HeaderTextColor,
                text = "THE COLOR GAME"
            )

            Spacer(
                Modifier.weight(1f)
            )

            button.restartButton(viewModel)

        }

        // Layout(() → Unit, Modifier = ..., MeasurePolicy) defined in androidx. compose. ui. layout
        Layout(
            content = { board.get() },
            modifier = Modifier.height(minSizeOfMainSq),
            measurePolicy = { measurables, constraints ->
                val cellSize = (minSizeOfMainSq / gridSize).roundToPx()
                val cellConstraints = constraints.copy(
                    minWidth = cellSize,
                    minHeight = cellSize,
                    maxWidth = cellSize,
                    maxHeight = cellSize
                )

                val placeables = measurables.map { measurable ->
                    measurable.measure(cellConstraints)
                }

                val sizeOfTheRow = sqrt(cellCount.toDouble()).toInt()

                layout(constraints.maxWidth, constraints.maxHeight) {
                    var x = 0
                    var y = 0
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(x, y)
                        x += cellSize
                        if (((index + 1) % sizeOfTheRow) == 0) {
                            x = 0
                            y += cellSize
                        }
                    }
                }
            }
        )

        scoreBoard.get()
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val mockViewModel = BallGameViewModel().apply {
        addBall(57, 5)
        addBall(21, 3)
        addBall(35, 2)
        addOldScores(
            com.allfreeapps.theballgame.ui.model.Scores(
                123,
                "player_1",
                1234,
                "2023-01-01"
            )
        )
    }

    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainLayOut(
                Modifier.padding(innerPadding),
                board =  Board(mockViewModel),
                button = Buttons() ,
                viewModel = mockViewModel,
                scoreBoard = ScoreBoard(mockViewModel)
            )
        }
    }
}
