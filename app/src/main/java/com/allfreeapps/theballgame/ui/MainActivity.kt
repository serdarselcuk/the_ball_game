package com.allfreeapps.theballgame.ui

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.allfreeapps.theballgame.navigation.Screen
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.ui.composables.GameLayout
import com.allfreeapps.theballgame.ui.composables.GameOverScreen
import com.allfreeapps.theballgame.ui.composables.ScoreTableScreen
import com.allfreeapps.theballgame.ui.composables.SettingsScreen
import com.allfreeapps.theballgame.ui.composables.WelcomeScreen
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.Black
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.util.Applogger
import com.allfreeapps.theballgame.viewModels.WelcomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WelcomeScreenViewModel by viewModels()

    @Inject
    lateinit var settingRepository: SettingsRepository

    @Inject
    lateinit var appLogger: Applogger


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            InitialView(
                settingsRepository = settingRepository,
                navigateToSettings = { navigateToSettings() }
            )
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

@Composable
fun InitialView(
    settingsRepository: SettingsRepository,
    navigateToSettings: () -> Unit = {}
) {
    val systemTheme by settingsRepository.systemTheme.collectAsState()
    val darkThemeEnabled by settingsRepository.darkTheme.collectAsState()
    val useDarkTheme = if (systemTheme) isSystemInDarkTheme() else darkThemeEnabled

    TheBallGameTheme(
        darkTheme = useDarkTheme
    ) {

        val navController = rememberNavController()
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Welcome.route) {
                    WelcomeScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .border(width = 2.dp, color = Black),
                        onSettingsClicked = {
                            navigateToSettings()
                        },
                        onStartButtonClicked = {
                            navController.navigate(Screen.Game.route)
                        }
                    )
                }

                composable(Screen.Game.route) {

                    GameLayout(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(BackgroundColor)
                            .border(width = 2.dp, color = Black),
                        onSettingsClicked = {
                            navigateToSettings()
                        },
                        gameOver = {
                            navController.navigate(Screen.GameOver.route) {
                                popUpTo(Screen.Game.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable(Screen.GameOver.route) {
                    GameOverScreen(
                        onSaveScoreClicked = {
                            navController.navigate(Screen.Scores.route) {
                                popUpTo(Screen.GameOver.route) { inclusive = true }
                            }
                        },
                        onSkipClicked = {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(Screen.GameOver.route) { inclusive = true }
                            }
                        },
                        onSettingsClicked = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)

                    )
                }

                composable(Screen.Scores.route) {
                    ScoreTableScreen(
                        Modifier
                            .padding(8.dp),
                        onCloseScoresClicked = {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(Screen.Scores.route) { inclusive = true }
                            }
                        }
                    )

                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen() {
    InitialView(
        settingsRepository = hiltViewModel()
    )
}

