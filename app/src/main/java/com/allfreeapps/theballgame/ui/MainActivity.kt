package com.allfreeapps.theballgame.ui

import BallGameViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.ui.composables.GameOverScreen
import com.allfreeapps.theballgame.ui.composables.MainLayout
import com.allfreeapps.theballgame.ui.composables.WelcomeScreen
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.Black
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BallGameViewModel by viewModels {
        BallGameViewModelFactory(application as MyApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            InitialView()
        }
    }

    @Composable
    fun InitialView() {
        TheBallGameTheme {

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val state by viewModel.state.collectAsState()
                val isMuted by viewModel.isMuted.collectAsState()
                val orientation = resources.configuration.orientation
                val ballList by viewModel.ballList.collectAsState()
                val selectedBallIndex by viewModel.selectedBall.collectAsState()
                val score by viewModel.score.collectAsState()
                val upcomingBalls by viewModel.upcomingBalls.collectAsState()
                val allScores by viewModel.allScores.collectAsState()

                when (state) {
                    GameState.GameNotStarted -> {
                        WelcomeScreen(
                            Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.background)
                                .border(width = 2.dp, color = Black),
                            isMuted = isMuted,
                            onMuteClicked = {
                                viewModel.changeSoundStatus()
                              },
                            onSettingsClicked = {
                                viewModel.playClickSound()
                                navigateToSettings()
                            },
                            restartButtonOnClick = {
                                viewModel.restartButtonOnClick()
                            }
                        )
                    }

                    GameState.GameOver -> {
                        GameOverScreen(
                            onSaveScoreClicked = { username ->
                                viewModel.saveScoreClicked(username)
                            }, onSkipClicked = {
                                viewModel.skipClicked()
                            },
                            onSettingsClicked = {
                                viewModel.playClickSound()
                                navigateToSettings()
                            }
                        )
                    }

                    else -> {

                        MainLayout(
                            Modifier
                                .padding(innerPadding)
                                .padding(4.dp)
                                .background(BackgroundColor)
                                .border(width = 2.dp, color = Black),
                            isMuted,
                            orientation,
                            ballList,
                            selectedBallIndex,
                            score,
                            upcomingBalls,
                            allScores,
                            onDeleteClicked = { id ->
                                viewModel.deleteScore(id)
                            },
                            changeSoundStatus = {
                                viewModel.changeSoundStatus()
                            },
                            restartButtonOnClick = {
                                viewModel.restartButtonOnClick()
                            },
                            onCellClick = { index ->

                                viewModel.onCellClick(
                                    ballList[index],
                                    index
                                )
                            },
                            removeTheBall = { index ->
                                viewModel.removeBall(index)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseSoundManagers()
    }


    private fun navigateToSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen() {
   MainActivity().InitialView()
}

