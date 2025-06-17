package com.allfreeapps.theballgame.ui.composables


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.utils.Constants


@Composable
fun Board(
    modifier: Modifier = Modifier,
    boardSize: Dp,
    ballList: Array<Int>,
    selectedBallIndex: Int? = -1,
    onCellClick: (Int) -> Unit,
    removeTheBall: (Int) -> Unit
) {

    val smallBoxSize = boardSize / Constants.GRID_SIZE

    Layout(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color.Gray
            ),
        content = {
            // Generate a Cell composable for each item in the ballList
            ballList.forEachIndexed { index, ballColorValue ->
                Cell(
                    ballColorValue = ballColorValue,
                    cellSize = smallBoxSize,
                    isSelected = selectedBallIndex == index,
                    onCellClick = { onCellClick(index) },
                    removeTheBall = {
                        removeTheBall(index)
                    }
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            Log.d("BoardLayout", "Incoming constraints: $constraints")
            Log.d("BoardLayout", "boardSize (Dp): $boardSize, GRID_SIZE: ${Constants.GRID_SIZE}")
            Log.d("BoardLayout", "smallBoxSize (Dp): $smallBoxSize")
            // Calculate cell size in pixels once
            val roundedCellSizePx = (smallBoxSize).roundToPx()
            Log.d("BoardLayout", "roundedCellSizePx: $roundedCellSizePx")
            // Define constraints for each cell
            val cellConstraintsForChildren = Constraints.fixed(roundedCellSizePx, roundedCellSizePx)

            // Measure each cell (measurable)
            val placeables = measurables.map { measurable ->
                measurable.measure(cellConstraintsForChildren)
            }

            // Calculate the total width and height required by the board based on its content
            val boardWidthPx = Constants.GRID_SIZE * roundedCellSizePx
            val boardHeightPx = Constants.GRID_SIZE * roundedCellSizePx

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
                    currentX += roundedCellSizePx
                    if (((index + 1) % Constants.GRID_SIZE) == 0) {
                        currentX = 0
                        currentY += roundedCellSizePx
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
    device = "spec:width=3800dp,height=1800dp,dpi=240,orientation=portrait"
)

fun BoardPreview() {
    Board(
        modifier=Modifier,
        boardSize = 800.dp,
        ballList = Array(81) { 0 }.apply {
            this@apply[2] = 1
            this@apply[3] = 2
            this@apply[4] = 3
            this@apply[5] = 4
            this@apply[6] = 5
            this@apply[7] = 6
        },
        selectedBallIndex = 3,
        onCellClick = { },
        removeTheBall = { }

    )
}