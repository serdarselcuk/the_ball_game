package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.model.Scores
import com.allfreeapps.theballgame.ui.theme.ScoreLine
import com.allfreeapps.theballgame.ui.theme.ScoreTextColor
import java.util.PriorityQueue

class ScoreBoard(val viewModel: BallGameViewModel) {

    @Composable
    fun get() {
        val currentConfiguration = LocalConfiguration.current
        val widthOfTheComponent = currentConfiguration.screenWidthDp
        val score by viewModel.score.collectAsState()
        val oldScores by viewModel.oldScores.collectAsState()
        val topScore = oldScores.peek()?.score?:1
        val lengthOfScoreLine = (score / topScore).toFloat()
        val dynamicColor = when {
            lengthOfScoreLine > 0.2 -> ScoreLine[0]
            lengthOfScoreLine > 0.4 -> ScoreLine[1]
            lengthOfScoreLine > 0.6 -> ScoreLine[2]
            lengthOfScoreLine > 0.8 -> ScoreLine[3]
            lengthOfScoreLine > 0.9 -> ScoreLine[4]
            else -> ScoreLine[5]
        }
        Column {
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
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
                Box(
                    modifier = Modifier
                        .width(widthOfTheComponent.dp) // Fixed width for the bar's container
                        .height(20.dp) // Container takes full max height for alignment
                        .background(Color.LightGray.copy(alpha = 0.3f)), // Optional: background for the container
                    contentAlignment = Alignment.BottomStart // Bar grows from the bottom
                ) {
                    Box(
                        modifier = Modifier
                            .width((lengthOfScoreLine * widthOfTheComponent).dp) // Width of the actual score bar
                            .height(20.dp) // Dynamic height
                            .background(dynamicColor) // Color of the score bar
                    )
                }
            }
            ScoresTable(
                oldScores
            )

        }
    }
}

@Composable
fun ScoresTable(scores: PriorityQueue<Scores>) {
    if (scores.isEmpty()) {
        Text("No old scores yet!", modifier = Modifier.padding(16.dp))
        return
    }else {

        Column(modifier = Modifier.padding(8.dp)) {
            // Table Header
            ScoreRow(
                playerName = "Player",
                score = "Score",
                date = "Date",
                isHeader = true
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))

            // Table Content (Scrollable if many scores)
            LazyColumn {
                items(scores.toArray()) { scoreItem ->
                    if (scoreItem as Scores != null) {
                        ScoreRow(
                            playerName = scoreItem.playerName,
                            score = scoreItem.score.toString(),
                            date = scoreItem.date
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Composable
fun ScoreRow(
    playerName: String,
    score: String, // Use String to accommodate header text
    date: String,
    isHeader: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = playerName,
            modifier = Modifier
                .weight(0.4f)
                .padding(horizontal = 4.dp), // Adjust weights as needed
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            style = if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = score,
            modifier = Modifier
                .weight(0.3f)
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.End,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            style = if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = date,
            modifier = Modifier
                .weight(0.3f)
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.End,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            style = if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun previewScoreBoard() {
    val mockViewModel = BallGameViewModel().apply {
        increaseScoreFor(9)
        increaseScoreFor(9)

        addOldScores(
            Scores(
                123,
                "player_1",
                1234,
                "2023-01-01"
            )
        )
        addOldScores(
            Scores(
                124,
                "player_2",
                1235,
                "2023-01-02"
            )
        )
        increaseScoreFor(8)
    }
    val score = ScoreBoard(mockViewModel)
    score.get()
}
