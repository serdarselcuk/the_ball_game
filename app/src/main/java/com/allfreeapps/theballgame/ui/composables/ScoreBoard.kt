package com.allfreeapps.theballgame.ui.composables

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.model.entities.Score
import java.util.PriorityQueue

class ScoreBoard(private val viewModel: BallGameViewModel) {

    @Composable
    fun Table(modifier: Modifier = Modifier, scoreLine: ScoreLine?) {
        Column(
            modifier = modifier

        ) {
            scoreLine?.Build()
            ScoresTable()
        }
    }

    @Composable
    fun ScoresTable() {
        val scores by viewModel.allScores.collectAsState()
        if (scores.isEmpty()) {
            Text("No old scores yet!", modifier = Modifier.padding(16.dp))
            return
        } else {

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
                    items(scores) { item ->
                        val scoreItem = item as Score
                        ScoreRow(
                            playerName = scoreItem.firstName,
                            score = scoreItem.score.toString(),
                            date = scoreItem.date.toString()
                        )
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
}


@Preview(showBackground = true)
@Composable
fun PreviewScoreBoard() {
    val mockViewModel = BallGameViewModel(Application()).apply {
        increaseScoreFor(9)
        increaseScoreFor(9)

    val scores = listOf(
            Score(
                null,
                "player_1",
                "last name",
                1224,
                null
            ),
            Score(
                null,
                "player_1",
                "last name",
                1234,
                null
            ),
            Score(
                null,
                "player_3",
                "last name",
                1244,
                null
            ),

        )
        increaseScoreFor(80)
    }
    val score = ScoreBoard(mockViewModel)
    score.Table(modifier = Modifier, null)
}
