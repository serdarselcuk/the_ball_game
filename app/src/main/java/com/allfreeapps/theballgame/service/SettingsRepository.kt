package com.allfreeapps.theballgame.service


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.allfreeapps.theballgame.model.entities.Settings
import com.allfreeapps.theballgame.utils.SoundType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt


@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
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


    val darkTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.DARK_THEME]?:false
        }.stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val systemTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.SYSTEM_THEME]?:false
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val masterVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )

    val hissVolume:  StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.HISS_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )

    val speed: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.SPEED] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )
    val clickVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.CLICK_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )
    val bubbleSelectVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_SELECT_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )
    val bubbleExplodeVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_EXPLODE_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )

    val tappingVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.TAPPING_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            initialValue = 50
        )


    suspend fun setVolume(volumeLevel: Float) {

        dataStore.edit { settings ->
            settings[Settings.VOLUME] = volumeLevel.roundToInt()
        }
    }

    suspend fun setIsMuteOnStart(isMuteOnStart: Boolean) {

        dataStore.edit { settings ->
            settings[Settings.IS_MUTE_ON_START] = isMuteOnStart
        }

    }

    suspend fun setDarkMoeOnStart(darkTheme: Boolean) {

        dataStore.edit { settings ->
            settings[Settings.DARK_THEME] = darkTheme
        }

    }

    suspend fun setSpeed(gameSpeed: Float) {

        dataStore.edit { settings ->
            settings[Settings.SPEED] = gameSpeed.roundToInt()
        }

    }

    suspend fun setClickVolume(clickVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.CLICK_VOLUME] = clickVolume.roundToInt()
        }

    }

    suspend fun setBubbleSelectVolume(bubbleselectVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.BUBBLE_SELECT_VOLUME] = bubbleselectVolume.roundToInt()
        }

    }

    suspend fun setBubbleExplodeVolume(bubbleexplodeVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.BUBBLE_EXPLODE_VOLUME] = bubbleexplodeVolume.roundToInt()
        }

    }

    suspend fun setTappingVolume(tappingVolume: Float) {

        dataStore.edit { settings ->
            settings[Settings.TAPPING_VOLUME] = tappingVolume.roundToInt()
        }

    }

    suspend fun setHissVolume(it: Float) {
        dataStore.edit { settings ->
            settings[Settings.HISS_VOLUME] = it.roundToInt()
        }
    }

    fun getMasterVolume(): Int {
        return masterVolume.value
    }

    suspend fun setSystemTheme(setSystemTheme: Boolean) {
        dataStore.edit { setting ->
            setting[Settings.SYSTEM_THEME] = setSystemTheme
        }
    }

    fun getVolume(it: SoundType): Int {
        return when (it) {
            SoundType.DEFAULT_TAP -> masterVolume.value
            SoundType.BUBBLE_EXPLODE -> bubbleExplodeVolume.value
            SoundType.EMPTY_TAP -> tappingVolume.value
            SoundType.FILLED_TAP -> bubbleSelectVolume.value
            SoundType.HISS -> hissVolume.value
        }
    }
}