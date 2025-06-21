package com.allfreeapps.theballgame.model.entities

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Settings {
    val IS_MUTE_ON_START= booleanPreferencesKey("isMuteOnStart")
    val SPEED = intPreferencesKey("speed")
    val VOLUME= intPreferencesKey("volume")
    val CLICK_VOLUME= intPreferencesKey("clickVolume")
    val BUBBLE_SELECT_VOLUME= intPreferencesKey("bubbleSelectVolume")
    val BUBBLE_EXPLODE_VOLUME= intPreferencesKey("bubbleExplodeVolume")
    val TAPPING_VOLUME= intPreferencesKey("tappingVolume")
    val DARK_THEME= booleanPreferencesKey("modeOnStart")
}
