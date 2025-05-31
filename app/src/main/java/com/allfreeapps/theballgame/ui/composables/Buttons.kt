package com.allfreeapps.theballgame.ui.composables

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.DisabledColor
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.ui.theme.StartButtonTextColor


    @Composable
    fun RestartButton(
        gameStatus: GameState,
        onclick: () -> Unit
    ){
        Button(
            modifier = Modifier.padding(2.dp).width(90.dp).height(35.dp),
            onClick = { onclick() },
            contentPadding = PaddingValues(
                horizontal = 6.dp,
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
                    text = if(gameStatus == GameState.GameNotStarted ) {
                        stringResource(R.string.start_game)
                    } else {
                        stringResource(R.string.restart_game)
                    }
                    ,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }


//@Composable
//@Preview(showBackground = true)
//fun PreviewButtons(){
//
//    val mockViewModel = BallGameViewModel(Application()).apply {
//        startGame()
//}
//    RestartButton()
//}
//
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewRestartButton(){
//    val mockViewModel = BallGameViewModel(Application()).apply {
//    }
//    RestartButton()
//}