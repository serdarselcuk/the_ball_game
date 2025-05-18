package com.allfreeapps.theballgame.ui.composables

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.DisabledColor
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.ui.theme.StartButtonTextColor

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
            colors = ButtonColors(
                containerColor = StartButtonBackgroundColor,
                contentColor = StartButtonTextColor,
                DisabledColor,
                DisabledColor
            ),
            content = {
                Text(
                    text = if( totalBallCount == 0) "Start Game" else "Restart Game",
                    style = MaterialTheme.typography.labelSmall
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