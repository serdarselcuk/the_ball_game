package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel

class Buttons {

    @Composable
    fun restartButton(viewModel: BallGameViewModel){
        val totalBallCount by viewModel.totalBallCount.collectAsState()
        Button(
            modifier = Modifier.padding(1.dp),
            onClick = { if ( totalBallCount == 0) viewModel.startGame() else viewModel.restartGame()},
            contentPadding = PaddingValues(
                horizontal = 8.dp,
                vertical = 2.dp
            ),
            content = {
                Text(
                    text = if( totalBallCount == 0) "Start Game" else "Restart Game",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun previewButtons(){
    val mockViewModel = BallGameViewModel().apply {
    addBall(57, 5)
    addBall(21, 3)
    addBall(35, 2)
}
    Buttons().restartButton(mockViewModel)
}