package com.allfreeapps.theballgame.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.allfreeapps.theballgame.model.entities.Settings
import com.allfreeapps.theballgame.util.Applogger
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
    private val dataStore: DataStore<Preferences>,
    private val appLogger: Applogger
){

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        appLogger.i(TAG, "Initialized")
    }


    val isAFreshUser: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.IS_A_FRESH_USER] ?: true
        }.stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = true
        ).also { appLogger.i("isAFreshUser", "isAFreshUser: $it") }

    val isVibrationTurnedOn: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.IS_VIBRATION_ON] ?: false
        }.stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    val isMuteOnStart: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.IS_MUTE_ON_START] ?: false
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )


    val darkTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.DARK_THEME]?:false
        }.stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    val systemTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Settings.SYSTEM_THEME]?:false
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    val masterVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )

    val hissVolume:  StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.HISS_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )

    val speed: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.SPEED] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )
    val clickVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.CLICK_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )
    val bubbleSelectVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_SELECT_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )
    val bubbleExplodeVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.BUBBLE_EXPLODE_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )

    val tappingVolume: StateFlow<Int> = dataStore.data
        .map { preferences ->
            preferences[Settings.TAPPING_VOLUME] ?: 50
        } .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = 50
        )

    suspend fun setIsAFreshUser(isAFreshUser: Boolean) {
        appLogger.i(TAG, "setIsAFreshUser called with $isAFreshUser")
        dataStore.edit { settings ->
            settings[Settings.IS_A_FRESH_USER] = isAFreshUser
        }
    }


    suspend fun setVolume(volumeLevel: Float) {
        appLogger.i(TAG, "setVolume called with $volumeLevel")
        dataStore.edit { settings ->
            settings[Settings.VOLUME] = volumeLevel.roundToInt()
        }
    }

    suspend fun setIsMuteOnStart(isMuteOnStart: Boolean) {
        appLogger.i(TAG, "setIsMuteOnStart called with $isMuteOnStart")
        dataStore.edit { settings ->
            settings[Settings.IS_MUTE_ON_START] = isMuteOnStart
        }

    }

    suspend fun setDarkMoeOnStart(darkTheme: Boolean) {
        appLogger.i(TAG, "setDarkMoeOnStart called with $darkTheme")
        dataStore.edit { settings ->
            settings[Settings.DARK_THEME] = darkTheme
        }

    }

    suspend fun setSpeed(gameSpeed: Float) {
        appLogger.i(TAG, "setSpeed called with $gameSpeed")
        dataStore.edit { settings ->
            settings[Settings.SPEED] = gameSpeed.roundToInt()
        }

    }

    suspend fun setClickVolume(clickVolume: Float) {
        appLogger.i(TAG, "setClickVolume called with $clickVolume")
        dataStore.edit { settings ->
            settings[Settings.CLICK_VOLUME] = clickVolume.roundToInt()
        }

    }

    suspend fun setBubbleSelectVolume(bubbleselectVolume: Float) {
        appLogger.i(TAG, "setBubbleSelectVolume called with $bubbleselectVolume")
        dataStore.edit { settings ->
            settings[Settings.BUBBLE_SELECT_VOLUME] = bubbleselectVolume.roundToInt()
        }

    }

    suspend fun setBubbleExplodeVolume(bubbleexplodeVolume: Float) {
        appLogger.i(TAG, "setBubbleExplodeVolume called with $bubbleexplodeVolume")
        dataStore.edit { settings ->
            settings[Settings.BUBBLE_EXPLODE_VOLUME] = bubbleexplodeVolume.roundToInt()
        }

    }

    suspend fun setTappingVolume(tappingVolume: Float) {
        appLogger.i(TAG, "setTappingVolume called with $tappingVolume")
        dataStore.edit { settings ->
            settings[Settings.TAPPING_VOLUME] = tappingVolume.roundToInt()
        }

    }

    suspend fun setHissVolume(it: Float) {
        appLogger.i(TAG, "setHissVolume called with $it")
        dataStore.edit { settings ->
            settings[Settings.HISS_VOLUME] = it.roundToInt()
        }
    }

    fun getMasterVolume(): Int {
        val volume = masterVolume.value
        appLogger.i(TAG, "getMasterVolume returning $volume")
        return volume
    }

    suspend fun setSystemTheme(setSystemTheme: Boolean) {
        appLogger.i(TAG, "setSystemTheme called with $setSystemTheme")
        dataStore.edit { setting ->
            setting[Settings.SYSTEM_THEME] = setSystemTheme
        }
    }

    fun getVolume(it: SoundType): Int {
        val volume = when (it) {
            SoundType.DEFAULT_TAP -> masterVolume.value
            SoundType.BUBBLE_EXPLODE -> bubbleExplodeVolume.value
            SoundType.EMPTY_TAP -> tappingVolume.value
            SoundType.FILLED_TAP -> bubbleSelectVolume.value
            SoundType.HISS -> hissVolume.value
        }

        appLogger.i(TAG, "getVolume for $it returning $volume")
        return volume
    }

    suspend fun setVibrationTurnedOn(boolean: Boolean) {
        appLogger.i(TAG, "setVibrationTurnedOn called with $boolean")
        dataStore.edit {
            it[Settings.IS_VIBRATION_ON] = boolean
        }
    }

    companion object {
        private const val TAG = "SettingsRepository"
    }
}