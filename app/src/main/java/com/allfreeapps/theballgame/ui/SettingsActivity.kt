package com.allfreeapps.theballgame.ui

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.viewModels.SettingsViewModel

class SettingsActivity: ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TheBallGameTheme {
                Scaffold { innerPadding ->
                    SettingsScreen(
                        Modifier.padding(innerPadding)

                    )

                }
            }
        }
    }


}


@Composable
fun SettingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = "Settings is not ready yet! ")
        }
    }
}

@Preview
@Composable
fun previewSettingsScreen() {
    SettingsScreen(modifier = Modifier)
}
