package com.allfreeapps.theballgame.ui

//import com.allfreeapps.theballgame.viewModels.SettingsViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        modifier = Modifier.padding(innerPadding).padding(16.dp),
                        viewModel = settingsViewModel

                    )

                }
            }
        }
    }
}


@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel) {
    val isMuteOnStart by viewModel.isMuteOnStart.collectAsState()
    val speed by viewModel.speed.collectAsState()
    val volume by viewModel.masterVolume.collectAsState()
    val clickVolume by viewModel.clickVolume.collectAsState()
    val bubbleSelectVolume by viewModel.bubbleSelectVolume.collectAsState()
    val bubbleExplodeVolume by viewModel.bubbleExplodeVolume.collectAsState()
    val tappingVolume by viewModel.tappingVolume.collectAsState()
    val modeOnStart by viewModel.darkTheme.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Game Settings", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

        SettingsToggle(
            label = "Mute on Start",
            checked = isMuteOnStart,
            onCheckedChange = { viewModel.setIsMuteOnStart(it) }
        )

        SettingsToggle(
            label = "Mode on Start (e.g., Dark Mode)",
            checked = modeOnStart,
            onCheckedChange = { viewModel.setModeOnStart(it) }
        )

        SettingsLevelControl(
            label = "Game Speed",
            value = speed,
            onIncrease = { viewModel.setSpeed( (speed + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setSpeed( (speed - 1).coerceAtLeast(0) ) }
        )

        SettingsLevelControl(
            label = "Master Volume",
            value = volume,
            onIncrease = { viewModel.setVolume( (volume + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setVolume( (volume - 1).coerceAtLeast(0) ) }
        )

        SettingsLevelControl(
            label = "Click Volume",
            value = clickVolume,
            onIncrease = { viewModel.setClickVolume( (clickVolume + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setClickVolume( (clickVolume - 1).coerceAtLeast(0) ) }
        )

        SettingsLevelControl(
            label = "Bubble Select Volume",
            value = bubbleSelectVolume,
            onIncrease = { viewModel.setBubbleSelectVolume( (bubbleSelectVolume + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setBubbleSelectVolume( (bubbleSelectVolume - 1).coerceAtLeast(0) ) }
        )

        SettingsLevelControl(
            label = "Bubble Explode Volume",
            value = bubbleExplodeVolume,
            onIncrease = { viewModel.setBubbleExplodeVolume( (bubbleExplodeVolume + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setBubbleExplodeVolume( (bubbleExplodeVolume - 1).coerceAtLeast(0) ) }
        )

        SettingsLevelControl(
            label = "Tapping Volume",
            value = tappingVolume,
            onIncrease = { viewModel.setTappingVolume( (tappingVolume + 1).coerceAtMost(10)) },
            onDecrease = { viewModel.setTappingVolume( (tappingVolume - 1).coerceAtLeast(0) ) }
        )
    }
}

@Composable
fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingsLevelControl(
    label: String,
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onDecrease) { Icon(Icons.Filled.KeyboardArrowUp, "Decrease") }
            Text(text = value.toString())
            Button(onClick = onIncrease) { Icon(Icons.Filled.KeyboardArrowUp, "Increase") }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
//    SettingsScreen(modifier = Modifier.padding(16.dp), viewModel = SettingsViewModel())
}
