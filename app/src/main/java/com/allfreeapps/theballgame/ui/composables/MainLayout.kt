package com.allfreeapps.theballgame.ui.composables

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
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
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainLayout(
    val viewModel: BallGameViewModel
) {

    @Composable
    fun Build(
        modifier: Modifier,
    ) {

        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                val sizeOfScreenWidth = configuration.screenWidthDp
                Column(
                    modifier = modifier.fillMaxSize(), // Ensure the main Column fills available space
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                {
                    Header(viewModel,
                        Modifier.fillMaxWidth()
                            .height((sizeOfScreenWidth * 0.12).dp)
                            .background(HeaderBackGround)
                    )
                    Board(
                        viewModel,
                        Modifier,
                        sizeOfScreenWidth.dp
                    )
                    Spacer(Modifier.height(5.dp))
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                    ) {
                        FutureBalls(viewModel)
                    }
                    Spacer(Modifier.height(5.dp))
                    Table(viewModel, scoreLine = ScoreLine(viewModel))
                }

            }

            else -> { // Landscape Mode
                val scoreLine = ScoreLine(viewModel)
                val sizeOfHeight= configuration.screenHeightDp
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Header(viewModel, Modifier
                        .fillMaxWidth()
                        . height((sizeOfHeight * 0.1).dp)
                        .background(HeaderBackGround))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Group these items to the left
                        scoreLine.Build() // Takes its intrinsic width
                        Spacer(modifier = Modifier.width(8.dp))

                        Board(
                            viewModel,
                            Modifier,
                            (sizeOfHeight * 0.9).dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            FutureBalls(viewModel) // Takes its intrinsic width
                        }
                        Spacer(modifier = Modifier.width(8.dp)) // Optional: space after futureBalls

                        // This will be on the far right
                        Table(viewModel, scoreLine= null) // Takes its intrinsic width
                    }
                }
            }
        }
    }
}


private fun mockViewModel(applicationContext: Context): BallGameViewModel
{
    return BallGameViewModel(applicationContext.applicationContext as MyApplication).apply {
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
