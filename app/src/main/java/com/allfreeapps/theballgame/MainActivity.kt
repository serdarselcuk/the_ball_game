package com.allfreeapps.theballgame

import BallGameViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.GameOverScreen
import com.allfreeapps.theballgame.ui.composables.MainLayout
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.Black
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BallGameViewModel by viewModels {
        BallGameViewModelFactory(application as MyApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by viewModel.state.collectAsState()
                    when (state) {
                        (GameState.GameOver) -> {
                            GameOverScreen(
                                onSaveScoreClicked = { username ->
                                    viewModel.saveScore(username)
                                    viewModel.setState(GameState.GameNotStarted)
                                    viewModel.resetGame()
                                }, onSkipClicked = {
                                    viewModel.setState(GameState.GameNotStarted)
                                    viewModel.resetGame()
                                }
                            )
                        }

                        else -> {
                            MainLayout(
                                viewModel,
                                Modifier
                                    .padding(innerPadding)
                                    .padding(4.dp)
                                    .background(BackgroundColor)
                                    .border(width = 2.dp, color = Black)
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
    GameOverScreen(
        onSaveScoreClicked = {},
        onSkipClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen() {
    val app = LocalActivity.current?.application
    val viewModel =
        BallGameViewModelFactory(app as MyApplication).create(BallGameViewModel::class.java).apply {
                setState(GameState.UserTurn)
            }
    MainLayout(
        viewModel,
        Modifier
            .padding(1.dp)
            .background(BackgroundColor)
            .border(width = 5.dp, color = Black)
    )
}

