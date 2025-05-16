package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.Board
import com.allfreeapps.theballgame.ui.composables.Buttons
import com.allfreeapps.theballgame.ui.theme.Blue
import com.allfreeapps.theballgame.ui.theme.Cyan
import com.allfreeapps.theballgame.ui.theme.Green
import com.allfreeapps.theballgame.ui.theme.Magenta
import com.allfreeapps.theballgame.ui.theme.Red
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.ui.theme.White
import com.allfreeapps.theballgame.ui.theme.Yellow
import com.allfreeapps.theballgame.utils.Constants.Companion.ballLimitToRemove
import com.allfreeapps.theballgame.utils.Constants.Companion.gridSize
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BallGameViewModel>()
    private lateinit var board: Board
    private lateinit var buttons: Buttons

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        board = Board()
        buttons = Buttons()

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LayOut(
                        Modifier.padding(innerPadding),
                        content = { board.get(viewModel) },
                        restartButton = { buttons.restartButton(viewModel) }
                    )
                }
            }
        }
    }
}

@Composable
fun LayOut(
    modifier: Modifier,
    content:@Composable ()->Unit,
    restartButton: @Composable () -> Unit = {}
) {
    val cell_count = gridSize * gridSize
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val minSizeOfMainSq = min(screenWidthDp, screenHeightDp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly )
    {
        Layout(
            modifier = Modifier.height(minSizeOfMainSq),
            content = content
        ) { measurables, constraints ->
            val cellSize = (minSizeOfMainSq/gridSize).roundToPx()
            val cellConstraints = constraints.copy(
                minWidth = cellSize,
                minHeight = cellSize,
                maxWidth = cellSize,
                maxHeight = cellSize
            )

            val placeables = measurables.map { measurable ->
                measurable.measure(cellConstraints)
            }

            val sizeOfTheRow = sqrt(cell_count.toDouble()).toInt()

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

        restartButton()
    }
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
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LayOut(
                Modifier.padding(innerPadding),
                content = { Board().get(mockViewModel) },
                restartButton = { Buttons().restartButton(mockViewModel) }
            )
        }
    }
}
