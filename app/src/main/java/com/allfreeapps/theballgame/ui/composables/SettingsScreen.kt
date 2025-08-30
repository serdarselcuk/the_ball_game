package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.viewModels.SettingsViewModel


@Composable
fun SettingsScreen(
    modifier: Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
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
    val isVibrationTurnedOn by viewModel.isVibrationTurnedOn.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            BackButton(
                modifier = Modifier,
                onClick = { onBackClicked() }
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(R.string.game_settings),
                style = typography.headlineSmall
            )
        }
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
                    text = stringResource(R.string.theme),
                    style = typography.bodyLarge
                )

                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.use_system))
                    Checkbox(
                        checked = systemTheme,
                        onCheckedChange = { viewModel.setSystemDefaultMode(it) }
                    )
                }
            }

            SettingsToggle(
                label = stringResource(id = R.string.settings_dark_mode),
                checked = darkMode,
                onCheckedChange = { viewModel.setModeOnStart(it) },
                enabled = !systemTheme
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsToggle(
                label = stringResource(R.string.mute_on_start),
                checked = isMuteOnStart,
                onCheckedChange = { viewModel.setIsMuteOnStart(it) }
            )

            SettingsToggle(
                label = stringResource(R.string.vibration),
                checked = isVibrationTurnedOn,
                onCheckedChange = { viewModel.setVibrationTurnedOn(it) }
            )
        }


        SettingsLevelControl(
            label = stringResource(R.string.game_speed),
            value = speed,
            onValueChange = { viewModel.setSpeed(it) }
        )

        SettingsLevelControl(
            label = stringResource(R.string.master_volume),
            value = volume,
            onValueChange = {
                viewModel.setVolume(it)
            }
        )

        SettingsLevelControl(
            label = stringResource(R.string.click_volume),
            value = clickVolume,
            onValueChange = { viewModel.setClickVolume(it) }
        )

        SettingsLevelControl(
            label = stringResource(R.string.bubble_select_volume),
            value = bubbleSelectVolume,
            onValueChange = { viewModel.setBubbleSelectVolume(it) }
        )

        SettingsLevelControl(
            label = stringResource(R.string.bubble_explode_volume),
            value = bubbleExplodeVolume,
            onValueChange = { viewModel.setBubbleExplodeVolume(it) }
        )

        SettingsLevelControl(
            label = stringResource(R.string.empty_cell_volume),
            value = tappingVolume,
            onValueChange = { viewModel.setTappingVolume(it) }
        )

        SettingsLevelControl(
            label = stringResource(R.string.ball_movement_volume),
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
//    val mockSettingsViewModel = SettingsViewModel(
//        settingsRepository = SettingsRepository(
//            dataStore = PreferenceDataStoreFactory.create(
//                corruptionHandler = ReplaceFileCorruptionHandler(
//                    produceNewData = { emptyPreferences() }
//                ),
////            migrations = listOf(androidx.datastore.migrations.SharedPreferencesMigration(context, YOUR_SHARED_PREFS_NAME_IF_MIGRATING)),
//                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//                produceFile = { context.preferencesDataStoreFile("app_settings") }
//            )
//        ),
//        appLogger = AppLoggerImpl()
//    )

//    SettingsScreen(
//        modifier = Modifier.padding(16.dp),
//        viewModel = mockSettingsViewModel,
//        onBackClicked = {
//        }
//    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.ORIENTATION_LANDSCAPE
)
@Composable
fun PreviewSettingsLandScapeScreen() {
    val context = LocalContext.current
//    val mockSettingsViewModel = SettingsViewModel(
//        settingsRepository = SettingsRepository(
//            dataStore = PreferenceDataStoreFactory.create(
//                corruptionHandler = ReplaceFileCorruptionHandler(
//                    produceNewData = { emptyPreferences() }
//                ),
////            migrations = listOf(androidx.datastore.migrations.SharedPreferencesMigration(context, YOUR_SHARED_PREFS_NAME_IF_MIGRATING)),
//                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//                produceFile = { context.preferencesDataStoreFile("app_settings") }
//            )
//        ),
//        appLogger = AppLoggerImpl()
//    )
//    SettingsScreen(
//        modifier = Modifier.padding(16.dp),
//        viewModel = mockSettingsViewModel,
//        onBackClicked = {
//        }
//    )
}

