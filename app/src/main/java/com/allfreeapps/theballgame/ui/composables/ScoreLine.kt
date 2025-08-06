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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor
import com.allfreeapps.theballgame.ui.theme.lineColorScale

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
                horizontal = 5.dp,
                vertical = 1.dp
            )
    ) {
        Text(
            text = "Score: $score",
            color = ScoreTextColor
        )
    }
}

@Composable
fun ComparableScoreLine(
    modifier: Modifier = Modifier,
    maxSizeOfLine: Dp,
    orientation: Int,
    score: Int,
    topScore: Int
) {
    val rateOfScoreLine = if (score < topScore)
        score / topScore
    else 1
    val dynamicColor = lineColorScale[(rateOfScoreLine * 10)
        .coerceAtMost(lineColorScale.size - 1)]

    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT

    val listOfGradients = listOf(
        dynamicColor.copy(alpha = 0.4f), // Darker edge
        dynamicColor.copy(alpha = 0.7f),
        dynamicColor,                     // Lighter center
        dynamicColor,                     // Lighter center
        dynamicColor.copy(alpha = 0.7f),
        dynamicColor.copy(alpha = 0.4f)  // Darker edge
    )

    val tubeGradient = if (isPortrait) {
        Brush.verticalGradient(
            listOfGradients
        )
    } else {
        Brush.horizontalGradient(
            listOfGradients
        )
    }

    val outerBoxModifier = modifier
        .then(
            if (isPortrait) Modifier
                .fillMaxWidth()
                .height(20.dp)
            else Modifier
                .fillMaxHeight()
                .width(20.dp)
        )
        .shadow(elevation = 2.dp, spotColor = Color.Black, ambientColor = Color.Black)

    val innerBoxModifier = Modifier
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

    Box(
        modifier = outerBoxModifier,
        contentAlignment = if (isPortrait) Alignment.CenterStart else Alignment.BottomStart // Adjust alignment based on orientation
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
        Modifier.background(BackgroundColor),
        500.dp,
        orientation,
        30,50
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
        Modifier.background(BackgroundColor),
        500.dp,
        orientation,
        30, 50
    )
}
