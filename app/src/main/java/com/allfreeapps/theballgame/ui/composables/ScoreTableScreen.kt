package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.ui.theme.GameOverBackground
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun ScoreTableScreen(
    modifier: Modifier = Modifier,
    viewModel: BallGameViewModel = hiltViewModel(),
    onCloseScoresClicked: () -> Unit
) {
    val allScores by viewModel.allScores.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GameOverBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoresTable(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            scores = allScores,
            onDeleteClicked = { id ->
                viewModel.deleteScore(id)
            }
        )
        ButtonWithText(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 16.dp),
            onclick = {
                onCloseScoresClicked()
            },
            buttonText = "Close"
        )
    }
}
