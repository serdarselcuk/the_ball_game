package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.GameText
import com.allfreeapps.theballgame.ui.theme.lineColorScale
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun ScoreBoard(modifier: Modifier = Modifier, score: Int) {
    Box(
        modifier = modifier
            .shadow(elevation = 1.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            .padding(
                horizontal = 5.dp, vertical = 1.dp
            )
    ) {
        GameText(
            text = "Score: $score", color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun ComparableScoreLine(
    modifier: Modifier = Modifier,
    viewModel: BallGameViewModel = hiltViewModel(),
    maxSizeOfLine: Dp,
    orientation: Int
) {
    val score by viewModel.score.collectAsState()
    val allScores by viewModel.allScores.collectAsState()
    val topScore = allScores.firstOrNull()?.score ?: 1

    val rateOfScoreLine = remember(topScore, score) {
        if (score < topScore) score.toFloat() / topScore.toFloat()
        else 1f
    }

    val dynamicColor =
        lineColorScale[(rateOfScoreLine * 10).toInt().coerceAtMost(lineColorScale.size - 1)]

    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT

    val listOfGradients = remember(dynamicColor) {
        listOf(
            dynamicColor.copy(alpha = 0.4f), // Darker edge
            dynamicColor.copy(alpha = 0.7f), dynamicColor,                     // Lighter center
            dynamicColor,                     // Lighter center
            dynamicColor.copy(alpha = 0.7f), dynamicColor.copy(alpha = 0.4f)  // Darker edge
        )
    }

    val tubeGradient = if (isPortrait) {
        remember(listOfGradients) {
            Brush.verticalGradient(listOfGradients)
        }
    } else {
        remember(listOfGradients) {
            Brush.horizontalGradient(listOfGradients)
        }
    }

    val outerBoxModifier = modifier
        .then(
            remember(isPortrait) {
                if (isPortrait) Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                else Modifier
                    .fillMaxHeight()
                    .width(20.dp)
            }
        )
        .shadow(elevation = 2.dp, spotColor = Color.Black, ambientColor = Color.Black)

    val innerBoxModifier =
        remember(tubeGradient, isPortrait, rateOfScoreLine, maxSizeOfLine, dynamicColor) {
            Modifier
                .background(tubeGradient)
                .then(
                    if (isPortrait) Modifier
                        .width((rateOfScoreLine * maxSizeOfLine.value).dp)
                        .fillMaxHeight()
                    else Modifier
                        .height((rateOfScoreLine * maxSizeOfLine.value).dp)
                        .fillMaxWidth()
                )
                .border(width = 0.5.dp, color = dynamicColor.copy(alpha = 0.5f))
                .shadow(
                    elevation = 1.dp,
                    spotColor = Color.White.copy(alpha = 0.5f),
                    ambientColor = Color.Transparent
                )
        }

    Box(
        modifier = outerBoxModifier,
        contentAlignment = remember(isPortrait) { if (isPortrait) Alignment.CenterStart else Alignment.BottomStart } // Adjust alignment based on orientation
    ) {
        Box(
            modifier = innerBoxModifier
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewOnPortrait() {
    val orientation = Configuration.ORIENTATION_PORTRAIT

    ComparableScoreLine(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        maxSizeOfLine = 500.dp,
        orientation = orientation
    )
}

@Preview(
    showBackground = true,
    name = "ScoreLinePreview",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)

@Composable
fun PreviewOnLandscape() {
    val orientation = Configuration.ORIENTATION_LANDSCAPE
//    ScoreLine(Modifier, 10, 10)
    ComparableScoreLine(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        maxSizeOfLine = 500.dp,
        orientation = orientation
    )
}
