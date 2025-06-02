package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.CellBoarderColor
import java.util.Date


@Composable
fun ScoresTable(modifier: Modifier = Modifier, scores: List<Score>) {

    Column(modifier.background(BackgroundColor)) {

        if (scores.isEmpty()) {
            Text(stringResource(R.string.no_previous_scores_yet), modifier = Modifier.padding(16.dp))
        } else {

            // Table Header
            ScoreRow(
                Modifier.height(25.dp).fillMaxWidth(),
                playerName = stringResource(R.string.player),
                score = stringResource(R.string.score),
                date = stringResource(R.string.date),
                isHeader = true
            )
            HorizontalDivider(color = CellBoarderColor)

            LazyColumn {
                items(scores) { scoreItem ->
                    ScoreRow(
                        Modifier.height(22.dp),
                        playerName = scoreItem.firstName,
                        score = scoreItem.score.toString(),
                        date = scoreItem.date.toString()
                    )
                    HorizontalDivider(color = CellBoarderColor)
                }
            }
        }
    }

}


@Composable
fun ScoreRow(
    modifier: Modifier = Modifier,
    playerName: String, score: String,
    date: String, isHeader: Boolean = false
) {

    val style =
        if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
    val fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = playerName,
            modifier = Modifier
                .weight(0.4f)
                .padding(horizontal = 4.dp),
            fontWeight = fontWeight,
            style = style
        )
        Text(
            text = score,
            modifier = Modifier
                .weight(0.3f)
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.End,
            fontWeight = fontWeight,
            style = style
        )
        Text(
            text = date,
            modifier = Modifier
                .weight(0.3f)
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.End,
            fontWeight = fontWeight,
            style = style
        )
    }
}


val scores = listOf(
    Score(
        1, "player_1", "last name", 1224, Date()
    ), Score(
        2, "player_1", "last name", 1234, null
    ), Score(
        3, "player_3", "last name", 1244, null
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewScoreBoard() {
    ScoresTable(modifier = Modifier.height(12.dp).fillMaxWidth(), scores = scores)
}


@Preview(showBackground = true)
@Composable
fun PreviewEmptyScoreBoard() {
    ScoresTable(modifier = Modifier, scores = emptyList())
}

