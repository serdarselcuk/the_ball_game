package com.allfreeapps.theballgame.model.entities

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Settings {
    val IS_MUTE_ON_START= booleanPreferencesKey("isMuteOnStart")
    val SPEED = floatPreferencesKey("speed")
    val VOLUME= floatPreferencesKey("volume")
    val CLICK_VOLUME= floatPreferencesKey("clickVolume")
    val HISS_VOLUME= floatPreferencesKey("hissVolume")
    val BUBBLE_SELECT_VOLUME= floatPreferencesKey("bubbleSelectVolume")
    val BUBBLE_EXPLODE_VOLUME= floatPreferencesKey("bubbleExplodeVolume")
    val TAPPING_VOLUME= floatPreferencesKey("tappingVolume")
    val DARK_THEME= intPreferencesKey("modeOnStart")
}
