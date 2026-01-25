package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.GameText
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
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header
        Row(

            horizontalArrangement = Arrangement.Start,
        ) {
            BackButton(
                modifier = Modifier,
                onClick = { onBackClicked() }
            )

            GameText(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(R.string.game_settings),
                style = typography.headlineMedium
            )
        }

        // Theme box
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .weight(2f) // Removed weight to allow scrolling
                .border(
                    width = 1.dp,
                    color = colorScheme.secondary
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Theme text
            GameText(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.theme),
                style = typography.headlineSmall
            )

            // Theme controls
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Use system check box
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GameText(text = stringResource(R.string.use_system))
                    Checkbox(
                        checked = systemTheme,
                        onCheckedChange = { viewModel.setSystemDefaultMode(it) }
                    )
                }

                SettingsToggle( // Light/Dark mode toggle
                    label = stringResource(R.string.settings_light),
                    secondaryLabel = stringResource(R.string.settings_dark),
                    checked = if (systemTheme) isSystemInDarkTheme() else darkMode,
                    onCheckedChange = { viewModel.setModeOnStart(it) },
                    enabled = !systemTheme
                )
            }


        }
        // Mute on Start and Vibration toggles
        Row(
            Modifier
                .fillMaxWidth(),
//                .weight(1f), // Removed weight to allow scrolling
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
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.game_speed),
            value = speed,
            onValueChange = { viewModel.setSpeed(it) }
        )

        SettingsLevelControl(
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.master_volume),
            value = volume,
            onValueChange = {
                viewModel.setVolume(it)
            }
        )

        SettingsLevelControl(
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.click_volume),
            value = clickVolume,
            onValueChange = { viewModel.setClickVolume(it) }
        )

        SettingsLevelControl(
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.bubble_select_volume),
            value = bubbleSelectVolume,
            onValueChange = { viewModel.setBubbleSelectVolume(it) }
        )

        SettingsLevelControl(
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.bubble_explode_volume),
            value = bubbleExplodeVolume,
            onValueChange = { viewModel.setBubbleExplodeVolume(it) }
        )

        SettingsLevelControl(
            modifier = Modifier, //.weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.empty_cell_volume),
            value = tappingVolume,
            onValueChange = { viewModel.setTappingVolume(it) }
        )

        SettingsLevelControl(
            modifier = Modifier
                .padding(bottom = 8.dp) // Add some padding at the bottom for better scroll visibility
                .fillMaxWidth(),
//                .weight(1f), // Removed weight to allow scrolling
            label = stringResource(R.string.ball_movement_volume),
            value = hissVolume,
            onValueChange = { viewModel.setHissVolume(it) }
        )

    }
}

@Composable
fun SettingsToggle(
    label: String,
    secondaryLabel: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val fullColor = colorScheme.onPrimary
        val disabledColor = colorScheme.onPrimary.copy(alpha = 0.38f)
        GameText(
            text = label,
            modifier = Modifier
                .padding(end = 8.dp),
            color = if (checked == (secondaryLabel == null)) fullColor else disabledColor
        )

        Switch(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp
            ),
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorScheme.outline,
                checkedTrackColor = colorScheme.primaryContainer,
                // You can also define colors for the unchecked state
                uncheckedThumbColor = colorScheme.primary,
                uncheckedTrackColor = colorScheme.surfaceVariant,
                // And for the disabled state
                disabledCheckedThumbColor = colorScheme.onSurface.copy(alpha = 0.12f),
                disabledCheckedTrackColor = colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )

        if (secondaryLabel != null) {
            GameText(
                text = secondaryLabel,
                modifier = Modifier
                    .padding(end = 8.dp),
                color = if (checked) fullColor else disabledColor
            )
        }
    }
}

@Composable
fun SettingsLevelControl(
    modifier: Modifier,
    label: String,
    value: Int,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0F..100F
) {
    Log.d("SettingsLevelControl", "$label - Value received: $value")

    Row(
        modifier = modifier
            .padding(vertical = 4.dp) // Reduced vertical padding
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GameText( //  label before the slider
            modifier = Modifier
                .padding(start = 4.dp) // Reduced start padding
                .weight(1f),
            text = label
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Reduced spacing
        ) {
            Slider(
                value = value.toFloat(),
                onValueChange = { value ->
                    Log.d("SettingsLevelControl", "$label - onValueChange: $value")
                    onValueChange(value)
                },
                valueRange = valueRange,
                modifier = Modifier.weight(5f)
            )

            GameText( // slider value
                modifier = Modifier.weight(1f),
                text = "${value}%"
            )
        }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    LocalContext.current
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
    LocalContext.current
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

