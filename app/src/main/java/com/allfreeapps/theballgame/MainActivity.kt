package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.booleanResource
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BallGameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameBoard(
                        Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun GameBoard(modifier: Modifier, viewModel: BallGameViewModel) {
    val ballList by viewModel.ballList.collectAsState()
    val selectedBallIndex by viewModel.selectedBall.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val squareSize = min(screenHeightDp, screenWidthDp) / 9

    Layout(
        modifier = modifier,
        content = {
            ballList.forEachIndexed { indice, ball ->
                var backGroundColor = Color.White
                var boarderColor = Color.DarkGray
                if (ball != NO_BALL) {
                    backGroundColor = colorOf(ball)
                    boarderColor =
                        if (viewModel.isSelectedBall(indice)) colorOf(ball) else Color.DarkGray
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
                        if (ball != NO_BALL) {
                            if (indice != selectedBallIndex) {
                                viewModel.selectTheBall(indice)
                            }
                        } else {
                            if (selectedBallIndex != null)
                                viewModel.checkIfSelectedBallCanMove(indice)
                        }
                    }
                )
            }
        }
    ) { measurables, constraints ->
        val cellSize = squareSize.roundToPx()
        val cellConstraints = constraints.copy(
            minWidth = cellSize,
            minHeight = cellSize,
            maxWidth = cellSize,
            maxHeight = cellSize
        )
        val placeables = measurables.map { measurable ->
            measurable.measure(cellConstraints)
        }

        val sizeOfTheRow = sqrt(ballList.size.toDouble()).toInt()

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
}

fun colorOf(ball: Int): Color {
    return when (ball) {
        1 -> Color.Red
        2 -> Color.Blue
        3 -> Color.Green
        4 -> Color.Yellow
        5 -> Color.Magenta
        6 -> Color.Cyan
        else -> Color.White
    }
}

fun getBrush(index: Int, color: Color): Brush {
    return Brush.linearGradient()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val mockViewModel = BallGameViewModel().apply {
        addBall(57, 5)
        addBall(21, 3)
        addBall(35, 2)
    }

    TheBallGameTheme {
        GameBoard(Modifier.fillMaxSize(), mockViewModel)
    }
}

val NO_BALL = 0