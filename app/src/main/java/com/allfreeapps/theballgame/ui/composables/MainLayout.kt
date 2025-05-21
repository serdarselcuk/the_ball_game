package com.allfreeapps.theballgame.ui.composables

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.model.entities.Score
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainLayout(
    val viewModel: BallGameViewModel
) {
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
        buttons = Buttons() // Assuming Buttons are used within Header or elsewhere
        header = Header(viewModel, buttons)
        scoreBoard = ScoreBoard(viewModel)
        futureBalls = FutureBalls(viewModel)
        val orientation = LocalConfiguration.current.orientation

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = modifier.fillMaxSize(), // Ensure the main Column fills available space
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                {
                    header.build()
                    board.Layout() // Make sure this is board.Layout() not board.layout() if that's the correct function name
                    Spacer(Modifier.height(5.dp))
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                    ) {
                        futureBalls.Build()
                    }
                    Spacer(Modifier.height(5.dp))
                    scoreBoard.Table(scoreLine = ScoreLine(viewModel))
                }

            }

            else -> { // Landscape Mode
                val scoreLine = ScoreLine(viewModel)
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start, // Align the whole block (header and row) to the start
                ) {
                    header.build(Modifier.fillMaxWidth())

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.Top,
                        // Arrangement.Start is good, but the Spacer will handle the main distribution
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Group these items to the left
                        scoreLine.Build() // Takes its intrinsic width
                        Spacer(modifier = Modifier.width(8.dp)) // Optional: space after scoreLine

                        board.Layout() // Takes its intrinsic width

                        // This Spacer will take up all the remaining space in the Row
                        // pushing scoreBoard.Table to the far right.
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            futureBalls.Build() // Takes its intrinsic width
                        }
                        Spacer(modifier = Modifier.width(8.dp)) // Optional: space after futureBalls

                        // This will be on the far right
                        scoreBoard.Table(scoreLine = null) // Takes its intrinsic width
                    }
                }
            }
        }
    }
}


private fun mockViewModel(applicationContext: Context): BallGameViewModel
{
    return BallGameViewModel(applicationContext).apply {
        startGame()
        addBall(57, 1)
        addBall(21, 2)
        addBall(35, 3)
        addBall(36, 4)
        addBall(37, 5)
        addBall(39, 6)

        val scores = listOf(
            Score(
                null,
                "player_1",
                "1234",
                1234,
                null
            ),
            Score(
                null,
                "player_2",
                "1235",
                1234,
                null
            ),
            Score(
                null,
                "player_3",
                "1236",
                1234,
                null
            )
        )
        scores.forEach {
            addOldScores(it)
        }
        selectTheBall(37)
    }
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun PortraitPreview() {
    val mainLayOut = MainLayout(mockViewModel(LocalContext.current))
    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            mainLayOut.Build(Modifier.padding(innerPadding))
        }
    }
}

@Preview(
    showBackground = true,
    name = "MainLayout Landscape (Device Spec)",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
private fun LandscapePreview() {
    val mainLayOut = MainLayout(mockViewModel(LocalContext.current))
    TheBallGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            mainLayOut.Build(Modifier.padding(innerPadding))
        }
    }
}
