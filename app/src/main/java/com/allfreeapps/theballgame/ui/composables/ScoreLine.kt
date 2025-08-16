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
import androidx.compose.material3.Text
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
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor
import com.allfreeapps.theballgame.ui.theme.lineColorScale
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun ScoreBoard(modifier: Modifier = Modifier, score: Int) {
    Box(
        modifier = modifier
            .shadow(elevation = 1.dp)
            .background(HeaderBackGround)
            .border(
                width = 2.dp,
                color = HeaderTextColor,
            )
            .padding(
                horizontal = 5.dp, vertical = 1.dp
            )
    ) {
        Text(
            text = "Score: $score", color = ScoreTextColor
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
    // Use remember to store the calculated rate.
    // The calculation itself is simple and doesn't need to be in a remember block
    // if it's cheap, but here it depends on topScore and score, so remembering it
    // prevents recalculation on every recomposition unless topScore or score changes.
    val rateOfScoreLine = remember(topScore, score) {
        if (score < topScore) (score / topScore).toFloat()
        else 1f
    }

    // dynamicColor depends on rateOfScoreLine, so it should be calculated after rateOfScoreLine.
    // No need for remember here as it's a direct calculation from rateOfScoreLine.
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

    // outerBoxModifier depends on isPortrait, so it can be remembered with isPortrait as a key.
    // However, modifier itself can change, so if `modifier` parameter is expected to change frequently
    // and recomposition due to its change is costly, then `modifier` should also be a key.
    // For simplicity, assuming `modifier` doesn't change frequently or the cost is low.
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

    // No need for LaunchEffect here as there are no side effects that need to be managed
    // based on lifecycle or state changes that are not directly related to UI recomposition.
    // All calculations are for UI display and are handled by remember and recomposition.
    Box(
        modifier = outerBoxModifier,
        // Alignment can be remembered if isPortrait is the key.
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
//    ScoreLine(Modifier, 10, 10)
    ComparableScoreLine(
        modifier = Modifier.background(BackgroundColor),
        maxSizeOfLine = 500.dp,
        orientation = orientation
        // Removed unused parameters score and topScore from preview call
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
        modifier = Modifier.background(BackgroundColor),
        maxSizeOfLine = 500.dp,
        orientation = orientation
        // Removed unused parameters score and topScore from preview call
    )
}
