package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel

class Buttons {


    @Composable
    fun restartButton(viewModel: BallGameViewModel){
        val totalBallCount by viewModel.totalBallCount.collectAsState()
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { if ( totalBallCount == 0) viewModel.startGame() else viewModel.restartGame()}
        ) {
            Text(
                text = if( totalBallCount == 0) "Start Game" else "Restart Game",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}