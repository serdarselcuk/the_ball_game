package com.allfreeapps.theballgame.viewModels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.model.entities.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    val masterVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.VOLUME] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )
    val isMuteOnStart: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.IS_MUTE_ON_START] ?: false
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = true
        )
    val darkTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.DARK_THEME] ?: false
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = false
        )
    val speed: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.SPEED] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )
    val clickVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.CLICK_VOLUME] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )
    val bubbleSelectVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_SELECT_VOLUME] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )
    val bubbleExplodeVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_EXPLODE_VOLUME] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )

    val tappingVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.TAPPING_VOLUME] ?: 5
        } .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 5
        )


    fun setVolume(volumeLevel: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.VOLUME] = volumeLevel
            }
        }
    }

    fun setIsMuteOnStart(isMuteOnStart: Boolean) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.IS_MUTE_ON_START] = isMuteOnStart
            }
        }
    }

    fun setModeOnStart(darkTheme: Boolean) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.DARK_THEME] = darkTheme
            }
        }
    }

    fun setSpeed(gameSpeed: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.SPEED] = gameSpeed
            }
        }
    }

    fun setClickVolume(clickVolume: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.CLICK_VOLUME] = clickVolume
            }
        }
    }

    fun setBubbleSelectVolume(bubbleselectVolume: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.BUBBLE_SELECT_VOLUME] = bubbleselectVolume
            }
        }
    }

    fun setBubbleExplodeVolume(bubbleexplodeVolume: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.BUBBLE_EXPLODE_VOLUME] = bubbleexplodeVolume
            }
        }
    }

    fun setTappingVolume(tappingVolume: Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[Settings.TAPPING_VOLUME] = tappingVolume
            }
        }
    }

}
