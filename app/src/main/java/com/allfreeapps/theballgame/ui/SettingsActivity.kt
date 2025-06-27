package com.allfreeapps.theballgame.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.composables.SettingsScreen
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.viewModels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity: ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TheBallGameTheme {
                Scaffold { innerPadding ->
                    SettingsScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp),
                        viewModel = settingsViewModel

                    )

                }
            }
        }
    }
}

