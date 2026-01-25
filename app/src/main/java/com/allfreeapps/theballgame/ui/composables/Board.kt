package com.allfreeapps.theballgame.ui.composables


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.viewModels.BallGameViewModel
import kotlin.math.min


@Composable
fun Board(
    modifier: Modifier = Modifier,
    viewModel: BallGameViewModel = hiltViewModel()
) {
    val ballList by viewModel.ballList.collectAsState()
    val gameSpeed by viewModel.gameSpeed.collectAsState()
    val selectedBall by viewModel.selectedBall.collectAsState()

    Board(
        modifier,
        ballList,
        gameSpeed,
        selectedBall,
        { index, reason ->
            viewModel.removeBall(index, reason)
        },
        { index -> viewModel.onCellClick(index) }
    )
}


@Composable
fun Board(
    modifier: Modifier = Modifier,
    ballList: Array<Int> = Array(81) { 0 },
    gameSpeed: Int = 50,
    selectedBall: Int? = null,
    removeTheBall: (Int, Int) -> Unit,
    onCellClick: (Int) -> Unit
) {
    val paddingVal = 2
    val height = LocalContext.current.resources.displayMetrics.heightPixels
    val width = LocalContext.current.resources.displayMetrics.widthPixels
    val boardSize = remember(height, width) {
        min(width, height) - (2 * paddingVal)
    }
    val smallBoxSize = (boardSize / Constants.GRID_SIZE)

    Layout(
        modifier = modifier
            .padding(paddingVal.dp),
        content = {
            ballList.forEachIndexed { index, ballColorValue ->
                val isSelected = remember(selectedBall) {
                    selectedBall == index
                }

                val currentSpeed = gameSpeed

                Cell(
                    Modifier
                        .fillMaxSize()
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        .clickable(
                            onClick = { onCellClick(index) }
                        ),
                    ballColorValue = ballColorValue,
                    isSelected = isSelected,
                    gameSpeed = currentSpeed,
                    removeTheBall = { int -> removeTheBall(index, int) }
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            val boardConstraints = arrayOf(Constants.GRID_SIZE, Constants.GRID_SIZE)
            Log.d("BoardLayout", "Incoming constraints: $constraints")
            Log.d("BoardLayout", "boardSize (Dp): $boardSize, GRID_SIZE: ${boardConstraints[0]}")
            Log.d("BoardLayout", "smallBoxSize (Dp): $smallBoxSize")

            // Define constraints for each cell
            val cellConstraintsForChildren = Constraints.fixed(smallBoxSize, smallBoxSize)

            // Measure each cell (measurable)
            val placeables = measurables.map { measurable ->
                measurable.measure(cellConstraintsForChildren)
            }

            // Calculate the total width and height required by the board based on its content
            val boardWidthPx = Constants.GRID_SIZE * smallBoxSize
            val boardHeightPx = Constants.GRID_SIZE * smallBoxSize

            // Respect incoming constraints:
            // The board can't be larger than what the parent allows,
            // but it also shouldn't be smaller than its content if the parent allows it to be larger.
            val finalWidth = boardWidthPx.coerceIn(constraints.minWidth, constraints.maxWidth)
            val finalHeight = boardHeightPx.coerceIn(constraints.minHeight, constraints.maxHeight)

            layout(finalWidth, finalHeight) { //calculated and coerced size of the board
                var currentX = 0
                var currentY = 0
                placeables.forEachIndexed { index, placeable ->
                    // Ensure placement is within the finalWidth and finalHeight if necessary,
                    // though with fixed cell sizes and grid logic, it should fit.
                    placeable.placeRelative(currentX, currentY)
                    currentX += smallBoxSize
                    if (((index + 1) % boardConstraints[0]) == 0) { // after last grid of the board move ti other row
                        currentX = 0
                        currentY += smallBoxSize
                    }
                }
            }
        }
    )
}


@Composable
@Preview(
    showBackground = true,
    name = "board preview",
    device = "id:pixel_9_pro_xl"
)

fun BoardPreviewONPortrait() {

    Board(
        modifier = Modifier.fillMaxSize(),
        ballList = Array(81) { 0 }
            .apply {
                this[2] = 1
                this[21] = 2
                this[32] = 3
                this[42] = 4
            },
        gameSpeed = 50,
        selectedBall = 32,
        onCellClick = {},
        removeTheBall = {} as (Int, Int) -> Unit
    )

}


@Composable
@Preview(
    showBackground = true,
    name = "board preview",

    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)

fun BoardPreviewOnLandscape() {

    Board(
        modifier = Modifier.fillMaxSize(),
        ballList = Array(81) { 0 }
            .apply {
                this[2] = 1
                this[21] = 2
                this[32] = 3
                this[42] = 4
            },
        gameSpeed = 50,
        selectedBall = 32,
        onCellClick = {},
        removeTheBall = {} as (Int, Int) -> Unit,
    )

}