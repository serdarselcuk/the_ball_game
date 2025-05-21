package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor

class ScoreLine(
    private val viewModel: BallGameViewModel
) {
    @Composable
    fun Build(modifier: Modifier = Modifier) {
        val currentConfiguration = LocalConfiguration.current
        val orientation = currentConfiguration.orientation
//        val widthOfTheComponent = currentConfiguration.screenWidthDp
        val score by viewModel.score.collectAsState()
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(modifier){
                    ScorePresenter(score)
                    ScoreComparationLine(
                        Modifier
                            .width(20.dp)
                            .fillMaxHeight()
                        ,score)
                }
            }
            else -> {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ScorePresenter(score)
                        ScoreComparationLine(
                            Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            score)
                    }
                }
            }
        }

    }

    @Composable
    fun ScorePresenter(score: Int) {
        Box(
            Modifier
                .border(
                    width = 1.dp,
                    color = ScoreTextColor,
                )
                .padding(
                    horizontal = 3.dp,
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
    fun ScoreComparationLine(modifier: Modifier = Modifier, score: Int) {
            val oldScores by viewModel.oldScores.collectAsState()
            val topScore = oldScores.peek()?.score ?: 1
            val lengthOfScoreLine = (score / topScore).toFloat()
            val dynamicColor = when {
                lengthOfScoreLine > 0.2 -> com.allfreeapps.theballgame.ui.theme.ScoreLine[0]
                lengthOfScoreLine > 0.4 -> com.allfreeapps.theballgame.ui.theme.ScoreLine[1]
                lengthOfScoreLine > 0.6 -> com.allfreeapps.theballgame.ui.theme.ScoreLine[2]
                lengthOfScoreLine > 0.8 -> com.allfreeapps.theballgame.ui.theme.ScoreLine[3]
                lengthOfScoreLine > 0.9 -> com.allfreeapps.theballgame.ui.theme.ScoreLine[4]
                else -> com.allfreeapps.theballgame.ui.theme.ScoreLine[5]
            }

            Box(
                modifier = modifier
                    .background(Color.LightGray.copy(alpha = 0.3f)), // Optional: background for the container
                contentAlignment = Alignment.BottomStart // Bar grows from the bottom
            ) {
                Box(
                    modifier = Modifier
//                        .width((lengthOfScoreLine * widthOfTheComponent).dp) // Width of the actual score bar
                        .height(20.dp) // Dynamic height
                        .background(dynamicColor) // Color of the score bar
                )
            }
        }
}

@Preview(
    showBackground = true,
    name = "ScoreLinePreview",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun PreviewOnPortrait(){
    ScoreLine(BallGameViewModel(LocalContext.current)).Build()
}

@Preview(
    showBackground = true,
    name = "ScoreLinePreview",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
fun PreviewOnLandscape(){
    ScoreLine(BallGameViewModel(LocalContext.current)).Build()
}