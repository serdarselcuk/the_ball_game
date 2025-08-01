package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.viewModels.WelcomeScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    onStartButtonClicked: () -> Unit = {}
) {
    val isMuted by viewModel.isMuted.collectAsState()
    val ballList by viewModel.ballList.collectAsState()


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val orientation = LocalContext.current.resources.configuration.orientation

        Header(
            Modifier
                .fillMaxWidth()
                .height(90.dp),
            listOf(
                {
                    MuteButton(
                        isMuted = isMuted,
                        onToggleMute = {
                            viewModel.changeSoundStatus()
                        }
                    )
                },
                {
                    SettingsButton(
                        Modifier.width(50.dp),
                        onClick = {
                            viewModel.playClickSound()
                            onSettingsClicked()
                        }
                    )
                }
            )
        )
        Spacer(modifier = Modifier.width(50.dp))

        // to draw a round button with text having same effect on the picture
        StartButton(restartButtonOnClick = onStartButtonClicked)

        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            val isLandscape =
                remember(orientation) { orientation == Configuration.ORIENTATION_LANDSCAPE }
            val boardShorterSizePx =
                remember(isLandscape) { if (isLandscape) maxHeight else maxWidth }
            val smallBoxSizePx =
                remember(boardShorterSizePx) { boardShorterSizePx / Constants.WELCOME_SCREEN_GRID_SIZE }
            val cellCountForLongerDimension =
                remember(smallBoxSizePx) { ((if (isLandscape) maxWidth else maxHeight) / smallBoxSizePx).toInt() }
            viewModel.updateCellCount(cellCountForLongerDimension)

            LaunchedEffect(ballList.size) {
                withContext(Dispatchers.Default) {
                    var currentNonZeroCount = ballList.count { it != 0 }
                    while (currentNonZeroCount < Constants.MAX_BALLS_ON_WELCOME_SCREEN) {
                        viewModel.createBall()
                        currentNonZeroCount =
                            ballList.count { it != 0 } // Recalculate after creation
                    }
                }
            }
//
//            WelcomingBoardBoard(
//                modifier = Modifier.fillMaxSize(),
//                orientation = TODO()
//            )
        }
    }
}

@Composable
fun StartButton(
    modifier: Modifier = Modifier, restartButtonOnClick: () -> Unit = {}
) {
    Box(
        modifier.background(StartButtonBackgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
        ) {
            val strokeWidth = 15.dp.toPx()
            drawArc(
                color = Color.White,
                startAngle = 210f,
                sweepAngle = 30f,
                useCenter = false,
                topLeft = Offset(strokeWidth, strokeWidth),
                size = size.copy(
                    width = size.width - strokeWidth * 2, height = size.height - strokeWidth * 2
                ),
                style = Stroke(width = strokeWidth / 1.5f, cap = StrokeCap.Round)
            )
        }

        ButtonWithText(Modifier
            .padding(2.dp)
            .width(90.dp)
            .height(90.dp),
            buttonText = stringResource(R.string.start_game),
            onclick = { restartButtonOnClick() })
    }
}
//
//@Composable
//fun WelcomingBoardBoard(
//    modifier: Modifier = Modifier, orientation: Int,
//    viewModel: WelcomeScreenViewModel = hiltViewModel(),
//) {
//    val ballList by viewModel.ballList.collectAsState()
//
//    Layout(modifier = modifier.border(width = 2.dp, color = Color.Gray), content = {
//        // Generate a Cell composable for each item in the ballList
//
//        // Iterate through all potential positions in the grid.
//        // If a ball exists at a position, create a Ball composable for it.
//        // This ensures that the order of measurables directly corresponds to the grid iteration order
//        // for cells that actually contain balls.
//        (0 until (ballList.size)).forEach { index ->
//            val ballData = ballList.getOrNull(index)
//            if (ballData != null && ballData.colorValue != Constants.NO_BALL) {
//                Ball(
//                    colorValue = ballData.colorValue,
//                    initialSize = ballData.initialSize,
//                    isBallSelected = false,
//                    targetSize = ballData.targetSize,
//                    onAnimationComplete = {
//                        // Remove the completed ball and add a new one in an empty spot
//                        val currentBalls = ballsState.value.toMutableList()
//                        currentBalls[ballData.position] = createAnEmptyBall(ballData.position) // Remove old ball
//                        val emptySlots =
//                            currentBalls.indices.filter { index -> currentBalls[index] == null || currentBalls[index]?.colorValue == Constants.NO_BALL }
//                        if (emptySlots.isNotEmpty()) {
//                            val newBallPosition = emptySlots.random()
//                            currentBalls[newBallPosition] = createNewBall(cellSize.intValue/5, newBallPosition, null)
//                        }
//                        ballsState.value = currentBalls
//                    },
//                    gameSpeed = ballData.gameSpeed
//                )
//            } else {
//                if (ballData != null) {
//                    createAnEmptyBall(ballData.position)
//                }
//            }
//        }
//    }, measurePolicy = { measurables, constraints ->
//        val GRID_SIZE = 3
//        // Use incoming constraints to determine board size, respecting parent constraints
//        val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
//        val boardShorterSizePx = if (isLandscape) constraints.maxHeight else constraints.maxWidth
//        val smallBoxSizePx = boardShorterSizePx / GRID_SIZE
//        if(cellSize.intValue == 0) cellSize = mutableIntStateOf(smallBoxSizePx)
//        val cellCountForLongerDimension =
//            if (isLandscape) (constraints.maxWidth / smallBoxSizePx) else (constraints.maxHeight / smallBoxSizePx)
//        val totalCellCount = cellCountForLongerDimension * GRID_SIZE
//
//        // Initialize ballsState here, now that totalCellCount is known
//        if (ballsState.value.isEmpty()) { // Initialize only once
//            ballsState.value = MutableList<BallData?>(totalCellCount) { createAnEmptyBall(0) }.apply {
//                val initialPositions = (0 until totalCellCount).shuffled().take(MAX_BALLS_ON_SCREEN)
//                initialPositions.forEach { position ->
//                    this[position] = createNewBall(smallBoxSizePx, position, null)
//                }
//            }
//        } else if (ballsState.value.size != totalCellCount) { // Adjust if size changes (e.g., orientation change)
//            // Handle recreation or adjustment of balls based on new totalCellCount
//            // This part might need more sophisticated logic depending on desired behavior
//        }
//        Log.d("Welcome Layout", "Incoming constraints: $constraints")
//        Log.d(
//            "Welcome Layout", "boardShorterSizePx: $boardShorterSizePx, GRID_SIZE: $GRID_SIZE"
//        )
//        Log.d("Welcome Layout", "smallBoxSizePx: $smallBoxSizePx")
//        // Define constraints for each cell
//        val roundedCellSizePx = smallBoxSizePx // No need to convert Dp if already in Px
//        val cellConstraintsForChildren = Constraints.fixed(roundedCellSizePx, roundedCellSizePx)
//
//        // Measure each cell (measurable)
//        val placeables = measurables.map { measurable ->
//            measurable.measure(cellConstraintsForChildren)
//        }
//
//        // Determine the final layout size based on orientation
//        val layoutWidth = if (isLandscape) {
//            (smallBoxSizePx * cellCountForLongerDimension).coerceIn(
//                constraints.minWidth, constraints.maxWidth
//            )
//        } else {
//            boardShorterSizePx.coerceIn(constraints.minWidth, constraints.maxWidth)
//        }
//        val layoutHeight = if (isLandscape) {
//            boardShorterSizePx.coerceIn(constraints.minHeight, constraints.maxHeight)
//        } else {
//            (smallBoxSizePx * cellCountForLongerDimension).coerceIn(
//                constraints.minHeight, constraints.maxHeight
//            )
//        }
//
//        layout(layoutWidth, layoutHeight) { //calculated and coerced size of the board
//            var currentPlaceableIndex = 0 // Index for iterating through the 'placeables' list
//            val xCount = if (isLandscape) cellCountForLongerDimension else GRID_SIZE
//            val yCount = if (isLandscape) GRID_SIZE else cellCountForLongerDimension
//
//            for (row in 0 until yCount) {
//                for (col in 0 until xCount) {
//                    val position = row * xCount + col // Ensure correct position calculation
//
//                    // Check if a ball is supposed to be at this position
//                    val ballDataAtIndex = ballsState.value.getOrNull(position)
//                    if (ballDataAtIndex != null && ballDataAtIndex.colorValue != Constants.NO_BALL) {
//                        // If a ball exists here, take the next available placeable
//                        // (which corresponds to this ball because of the content block's iteration)
//                        // and place it.
//                        if (currentPlaceableIndex < placeables.size) {
//                            val placeable = placeables[currentPlaceableIndex]
//                                val xPosition = col * roundedCellSizePx
//                                val yPosition = row * roundedCellSizePx
//                                placeable.placeRelative(xPosition, yPosition)
//                                currentPlaceableIndex++ // Move to the next placeable
//                            }
//                        }
//                    } // If no ball at this position, do nothing, move to the next grid cell.
//                }
//        }
//    })
//
//}
//
//
//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Welcome Page",
//    device = "spec:width=800dp,height=2400dp,orientation=portrait"
//)
//@Composable
//fun PreviewWelcomePagePortrait() {
////    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})
////    StartScreenBackground(Modifier.fillMaxHeight(), Configuration.ORIENTATION_PORTRAIT)
//}
//
//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Welcome Page",
//    device = "spec:width=800dp,height=2400dp,orientation=landscape"
//)
//@Composable
//fun PreviewWelcomePageLandscape() {
////    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})
////    StartScreenBackground(Modifier.fillMaxWidth(), Configuration.ORIENTATION_LANDSCAPE)
//}