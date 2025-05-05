package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ballMap = mapOf(
            Coordinate(2, 3) to Color.Red
        )
        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameBoard(
                        Modifier.padding(innerPadding),
                        ballMap
                    )
                }
            }
        }
    }
}

@Composable
fun GameBoard(modifier: Modifier, ballList: Map<Coordinate, Color>) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val squareSize = min(screenHeightDp, screenWidthDp) / 9

    Layout(
        content = {
            for (row in 0 until 9) {
                for (column in 0 until 9) {
                    var color = Color.Black
                    var backGroundColor = Color.Gray
                    if (ballList.containsKey(Coordinate(row, column))) {
                        color = ballList[Coordinate(row, column)] ?: Color.Gray
                        backGroundColor = color.copy()
                    }
                    Box(
                        modifier = Modifier
                            .background(backGroundColor)
                            .border(
                                BorderStroke(
                                    width = 0.5.dp,
                                    color
                                )
                            )
                    )
                }
            }
        },
        modifier = modifier.padding(1.dp)
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

        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            var y = 0
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x, y)
                x += cellSize
                if ((index + 1) % 9 == 0) {
                    x = 0
                    y += cellSize
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val ballMap = mapOf(
        Coordinate(2, 3) to Color.Red
    )
    TheBallGameTheme {
        GameBoard(Modifier.fillMaxSize(), ballMap)
    }
}

class Coordinate(val row: Int, val column: Int) {
    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Coordinate) {
            other.row == this.row && other.column == this.column
        } else false
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        return result
    }
}