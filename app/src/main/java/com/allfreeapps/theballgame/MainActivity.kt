package com.allfreeapps.theballgame

import BallGameViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.GameOverScreen
import com.allfreeapps.theballgame.ui.composables.MainLayout
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BallGameViewModel by viewModels {
        BallGameViewModelFactory(application as MyApplication)
    }
    private lateinit var mainLayOut: MainLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayOut = MainLayout(viewModel)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by viewModel.state.collectAsState()
                    when (state) {
                        (GameState.GameOver) -> {
                            GameOverScreen(onSaveScoreClicked = { username ->
                                viewModel.saveScore(username)
                                viewModel.setState(GameState.GameNotStarted)
                            })
                        }
                        else -> {
                            mainLayOut.Build(
                                Modifier
                                    .padding(innerPadding)
                                    .background(BackgroundColor)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameOverScreenWithInput() {
    GameOverScreen(onSaveScoreClicked = {})


}