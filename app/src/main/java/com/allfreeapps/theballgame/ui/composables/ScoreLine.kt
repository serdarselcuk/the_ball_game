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
import com.allfreeapps.theballgame.ui.theme.GameColorScale
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor

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
    val rateOfScoreLine = if (topScore > 0) score.toFloat() / topScore else 0f


    val colorIndex = when {
        rateOfScoreLine > 0.9 -> 6
        rateOfScoreLine > 0.8 -> 5
        rateOfScoreLine > 0.6 -> 4
        rateOfScoreLine > 0.4 -> 3
        rateOfScoreLine > 0.2 -> 2
        rateOfScoreLine > 0.0 -> 1
        else -> 0
    }
    val dynamicColor = GameColorScale[colorIndex]

    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT

    val tubeGradient = if (isPortrait) {
        Brush.verticalGradient(
            colors = listOf(
                dynamicColor.copy(alpha = 0.7f),
                dynamicColor,
                dynamicColor.copy(alpha = 0.7f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                dynamicColor.copy(alpha = 0.7f),
                dynamicColor,
                dynamicColor.copy(alpha = 0.7f)
            )
        )
    }

    val outerBoxModifier = modifier
        .then(
            if (isPortrait) Modifier.fillMaxWidth().height(20.dp)
            else Modifier.fillMaxHeight().width(20.dp)
        )
        .shadow(elevation = 2.dp, spotColor = Color.Black, ambientColor = Color.Black)

    val innerBoxModifier = Modifier
        .background(tubeGradient)
        .then(
            if (isPortrait) Modifier.width((rateOfScoreLine * maxSizeOfLine.value).dp).fillMaxHeight()
            else Modifier.height((rateOfScoreLine * maxSizeOfLine.value).dp).fillMaxWidth()
        )
        .border(width = 0.5.dp, color = dynamicColor.copy(alpha = 0.5f))
        .shadow(elevation = 1.dp, spotColor = Color.White.copy(alpha = 0.5f), ambientColor = Color.Transparent)

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
    ScoreBoard( Modifier, 10)
}