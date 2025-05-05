package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    var boarderColor = Color.Black
    var backGroundColor = Color.Gray

    Column(
        modifier = modifier.height(squareSize*9),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        for (row in 0..9) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (column in 0..9) {

                    val coordinate = Coordinate(row, column)
//                    if (ballList.containsKey(coordinate)) {
//                        backGroundColor = ballList[coordinate]?: Color.Gray
//                    }
                    Box(
                        modifier = Modifier
                            .background(backGroundColor)
                            .height(squareSize)
                            .width(squareSize)
                            .border(
                                BorderStroke(
                                    width = 1.dp,
                                    boarderColor
                                )
                            )
                    ) {

                    }
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