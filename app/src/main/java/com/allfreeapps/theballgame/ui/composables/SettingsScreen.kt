package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log
import com.allfreeapps.theballgame.viewModels.SettingsViewModel


@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel) {
    val isMuteOnStart = viewModel.isMuteOnStart.collectAsState()
    val darkMode by viewModel.darkTheme.collectAsState()
    val speed by viewModel.speed.collectAsState()
    val volume by viewModel.masterVolume.collectAsState()
    val clickVolume by viewModel.clickVolume.collectAsState()
    val bubbleSelectVolume by viewModel.bubbleSelectVolume.collectAsState()
    val bubbleExplodeVolume by viewModel.bubbleExplodeVolume.collectAsState()
    val tappingVolume by viewModel.tappingVolume.collectAsState()
    val hissVolume by viewModel.hissVolume.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Game Settings", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Theme")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Checkbox(
                        checked = darkMode == null,
                        onCheckedChange = { viewModel.setSystemDefaultMode(it) })
                    Text(text = "Use system")
                }

                SettingsToggle(
                    label = "Dark mode",
                    checked = darkMode ?: false,
                    onCheckedChange = { viewModel.setModeOnStart(it) },
                    enabled = darkMode != null
                )
            }
        }

        SettingsToggle(
            label = "Mute on Start",
            checked = isMuteOnStart.value,
            onCheckedChange = { viewModel.setIsMuteOnStart(it) }
        )


        SettingsLevelControl(
            label = "Game Speed",
            value = speed,
            onValueChange = { viewModel.setSpeed(it) }
        )

        SettingsLevelControl(
            label = "Master Volume",
            value = volume,
            onValueChange = { viewModel.setVolume(it) }
        )

        SettingsLevelControl(
            label = "Click Volume",
            value = clickVolume,
            onValueChange = { viewModel.setClickVolume(it) }
        )

        SettingsLevelControl(
            label = "Bubble Select Volume",
            value = bubbleSelectVolume,
            onValueChange = { viewModel.setBubbleSelectVolume(it) }
        )

        SettingsLevelControl(
            label = "Bubble Explode Volume",
            value = bubbleExplodeVolume,
            onValueChange = { viewModel.setBubbleExplodeVolume(it) }
        )

        SettingsLevelControl(
            label = "Tapping Volume",
            value = tappingVolume,
            onValueChange = { viewModel.setTappingVolume(it) }
        )

        SettingsLevelControl(
            label = "Ball Movement Volume",
            value = hissVolume,
            onValueChange = { viewModel.setHissVolume(it) }
        )

    }
}

@Composable
fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(end = 8.dp),
            color = if (enabled)
                colorScheme.onSurface
            else
                colorScheme.onSurface.copy(alpha = 0.38f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
fun SettingsLevelControl(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f
) {
    Log.d("SettingsLevelControl", "$label - Value received: $value")
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = label)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Slider(
                value = value,
                onValueChange = { value->
                    Log.d("SettingsLevelControl", "$label - onValueChange: $value")
                    onValueChange(value)
                },
                valueRange = valueRange,
                modifier = Modifier.weight(1f)
            )
            Text(text = "${value.toInt()}%")
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
//    SettingsScreen(modifier = Modifier.padding(16.dp), viewModel = SettingsViewModel())
}
