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
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
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

            WelcomingBoardBoard(
                modifier = Modifier.fillMaxSize(),
                orientation = orientation
            )
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

@Composable
fun WelcomingBoardBoard(
    modifier: Modifier = Modifier, orientation: Int,
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
) {
    val ballList by viewModel.ballList.collectAsState()

    Text(
        "Under Construction!!! \n $orientation"
    )
//    Layout(modifier = modifier,){
//        measureables, constraints ->
//        val placeables = measureables.map { measurable ->
//            measurable.measure(constraints)
//        }
//    }

}


@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Welcome Page",
    device = "spec:width=800dp,height=2400dp,orientation=portrait"
)
@Composable
fun PreviewWelcomePagePortrait() {
//    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})
//    StartScreenBackground(Modifier.fillMaxHeight(), Configuration.ORIENTATION_PORTRAIT)
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Welcome Page",
    device = "spec:width=800dp,height=2400dp,orientation=landscape"
)
@Composable
fun PreviewWelcomePageLandscape() {
//    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})
//    StartScreenBackground(Modifier.fillMaxWidth(), Configuration.ORIENTATION_LANDSCAPE)
}