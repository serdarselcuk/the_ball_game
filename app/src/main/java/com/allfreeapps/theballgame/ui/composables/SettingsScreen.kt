package com.allfreeapps.theballgame.ui.composables

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.viewModels.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel) {
    val isMuteOnStart by viewModel.isMuteOnStart.collectAsState()
    val darkMode by viewModel.darkTheme.collectAsState()
    val systemTheme by viewModel.systemTheme.collectAsState()
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
        Text("Game Settings",
            style = typography.headlineSmall)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorScheme.secondary
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Theme",
                    style = typography.bodyLarge
                )

                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Use system")
                    Checkbox(
                        checked = systemTheme,
                        onCheckedChange = { viewModel.setSystemDefaultMode(it) }
                    )
                }
            }

            SettingsToggle(
                label = "Dark mode",
                checked = darkMode,
                onCheckedChange = { viewModel.setModeOnStart(it) },
                enabled = !systemTheme
            )
        }

        SettingsToggle(
            label = "Mute on Start",
            checked = isMuteOnStart,
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
            onValueChange = {
                viewModel.setVolume(it)
            }
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
            label = "Empty Cell Volume",
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
        modifier = Modifier.padding( 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
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
    value: Int,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0F..100F
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
                value = value.toFloat(),
                onValueChange = { value->
                    Log.d("SettingsLevelControl", "$label - onValueChange: $value")
                    onValueChange(value)
                },
                valueRange = valueRange,
                modifier = Modifier.weight(1f)
            )
            Text(text = "${value}%")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val context = LocalContext.current
    val mockSettingsViewModel = SettingsViewModel(
        settingsRepository = SettingsRepository(
            dataStore = PreferenceDataStoreFactory.create(
                corruptionHandler = androidx.datastore.core.handlers.ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
//            migrations = listOf(androidx.datastore.migrations.SharedPreferencesMigration(context, YOUR_SHARED_PREFS_NAME_IF_MIGRATING)),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { context.preferencesDataStoreFile("app_settings") }
            )
        )
    )
    SettingsScreen(
        modifier = Modifier.padding(16.dp),
        viewModel = mockSettingsViewModel
    )
}
