package com.allfreeapps.theballgame.model.entities

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Settings {
    val IS_A_FRESH_USER by lazy { booleanPreferencesKey("isAFreshUser") }
    val IS_VIBRATION_ON by lazy { booleanPreferencesKey("vibrationOn") }
    val SYSTEM_THEME by lazy { booleanPreferencesKey("systemTheme") }
    val DARK_THEME by lazy { booleanPreferencesKey("darkThemeOnStart") }
    val IS_MUTE_ON_START by lazy { booleanPreferencesKey("isMuteOnStart") }
    val SPEED by lazy { intPreferencesKey("speed") }
    val VOLUME by lazy { intPreferencesKey("volume") }
    val CLICK_VOLUME by lazy { intPreferencesKey("clickVolume") }
    val HISS_VOLUME by lazy { intPreferencesKey("hissVolume") }
    val BUBBLE_SELECT_VOLUME by lazy { intPreferencesKey("bubbleSelectVolume") }
    val BUBBLE_EXPLODE_VOLUME by lazy { intPreferencesKey("bubbleExplodeVolume") }
    val TAPPING_VOLUME by lazy { intPreferencesKey("tappingVolume") }
}
