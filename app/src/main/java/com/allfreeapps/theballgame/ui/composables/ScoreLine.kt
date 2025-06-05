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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.GameColorScale
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor


@Composable
fun ScoreBoard(modifier: Modifier, score: Int) {
    Box(
        modifier
            .background(HeaderBackGround)
            .border(
                width = 2.dp,
                color = HeaderTextColor,
            )
            .padding(
                horizontal = 5.dp,
                vertical = 1.dp
            )
            .shadow(elevation = 1.dp)
    ) {
        Text(
            text = "Score: $score",
            color = ScoreTextColor
        )
    }
}

@Composable
fun ComparableScoreLine(
    modifier: Modifier,
    maxSizeOfLine: Dp,
    orientation: Int,
    score: Int, topScore: Int
) {

    val maxSizeOfLineFloat = maxSizeOfLine.value
    val rateOfScoreLine = if (topScore > 0) score.toFloat() / topScore else 0f
    val dynamicColor = when {
        rateOfScoreLine > 0.9 -> GameColorScale[6]
        rateOfScoreLine > 0.8 -> GameColorScale[5]
        rateOfScoreLine > 0.6 -> GameColorScale[4]
        rateOfScoreLine > 0.4 -> GameColorScale[3]
        rateOfScoreLine > 0.2 -> GameColorScale[2]
        else -> GameColorScale[1]
    }

    var outerBoxModifier = modifier
    var innerBoxModifier : Modifier

    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            outerBoxModifier = outerBoxModifier
                .fillMaxWidth()
                .height(20.dp)
            innerBoxModifier = Modifier // Apply background to the inner Box
                .background(dynamicColor) // Use the calculated dynamicColor here
                .width((rateOfScoreLine * maxSizeOfLineFloat).dp)
                .fillMaxHeight()
        }
        else -> { // Assuming Configuration.ORIENTATION_LANDSCAPE
            outerBoxModifier = outerBoxModifier
                .fillMaxHeight()
                .width(20.dp)
            innerBoxModifier = Modifier // Apply background to the inner Box
                .background(dynamicColor) // Use the calculated dynamicColor here
                .height((rateOfScoreLine * maxSizeOfLineFloat).dp)
                .fillMaxWidth()
        }
    }
  Box(
        modifier = outerBoxModifier,
        contentAlignment = Alignment.BottomStart
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