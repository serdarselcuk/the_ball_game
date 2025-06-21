package com.allfreeapps.theballgame.service


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.media3.common.util.NullableType
import com.allfreeapps.theballgame.model.entities.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
){

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val isMuteOnStart: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.IS_MUTE_ON_START] ?: false
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )


    val darkTheme: StateFlow<Boolean?> = dataStore.data
        .map { preferences ->
            when (preferences[Settings.DARK_THEME]) {
                0 -> false // not dark mode
                1 -> true // dark mode
                else -> null // system default
            }
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val masterVolume: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )

    val hissVolume:  StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.HISS_VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )

    val speed: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.SPEED] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )
    val clickVolume: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.CLICK_VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )
    val bubbleSelectVolume: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_SELECT_VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )
    val bubbleExplodeVolume: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_EXPLODE_VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )

    val tappingVolume: StateFlow<Float> = dataStore.data
        .map { preferences ->
            preferences[Settings.TAPPING_VOLUME] ?: 50f
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50f
        )


    suspend fun setVolume(volumeLevel: Float) {

        dataStore.edit { settings ->
            settings[Settings.VOLUME] = volumeLevel
        }

    }

    suspend fun setIsMuteOnStart(isMuteOnStart: Boolean) {

        dataStore.edit { settings ->
            settings[Settings.IS_MUTE_ON_START] = isMuteOnStart
        }

    }

    suspend fun setModeOnStart(darkTheme: Boolean?) {

        dataStore.edit { settings ->
            settings[Settings.DARK_THEME] = when(darkTheme){
                false -> 0 // no dark theme
                true -> 1 // dark theme
                else -> -1 // system default
            }
        }

    }

    suspend fun setSpeed(gameSpeed: Float) {

        dataStore.edit { settings ->
            settings[Settings.SPEED] = gameSpeed
        }

    }

    suspend fun setClickVolume(clickVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.CLICK_VOLUME] = clickVolume
        }

    }

    suspend fun setBubbleSelectVolume(bubbleselectVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.BUBBLE_SELECT_VOLUME] = bubbleselectVolume
        }

    }

    suspend fun setBubbleExplodeVolume(bubbleexplodeVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.BUBBLE_EXPLODE_VOLUME] = bubbleexplodeVolume
        }

    }

    suspend fun setTappingVolume(tappingVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.TAPPING_VOLUME] = tappingVolume
        }

    }

    suspend fun setHissVolume(it: Float) {
        dataStore.edit { settings ->
            settings[Settings.HISS_VOLUME] = it
        }
    }

    fun getSoundEffectsVolume(): Float {
        return masterVolume.value
    }
}