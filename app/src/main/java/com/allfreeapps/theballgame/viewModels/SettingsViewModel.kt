package com.allfreeapps.theballgame.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.SoundType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    val isMuteOnStart: StateFlow<Boolean> = settingsRepository.isMuteOnStart
    val systemTheme: StateFlow<Boolean> = settingsRepository.systemTheme
    val darkTheme: StateFlow<Boolean> = settingsRepository.darkTheme
    val speed: StateFlow<Int> = settingsRepository.speed
    val masterVolume: StateFlow<Int> = settingsRepository.masterVolume
    val hissVolume: StateFlow<Int> = settingsRepository.hissVolume
    val clickVolume: StateFlow<Int> = settingsRepository.clickVolume
    val bubbleSelectVolume: StateFlow<Int> = settingsRepository.bubbleSelectVolume
    val bubbleExplodeVolume: StateFlow<Int> = settingsRepository.bubbleExplodeVolume
    val tappingVolume: StateFlow<Int> = settingsRepository.tappingVolume

    fun setVolume(volumeLevel: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setVolume(volumeLevel)
            }catch (e: Exception){
                Log.e(TAG, "volumeLevel could not be set")
            }
        }
    }

    fun setIsMuteOnStart(isMuteOnStart: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.setIsMuteOnStart(isMuteOnStart)
            }catch (e: Exception){
                Log.e(TAG, "isMuteOnStart could not be set")
            }
        }
    }

    fun setModeOnStart(darkTheme: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.setDarkMoeOnStart(darkTheme)
            }catch (e: Exception){
                Log.e(TAG, "darkTheme could not be set")
            }
        }
    }

    fun setSpeed(gameSpeed: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setSpeed(gameSpeed)
            }catch (e: Exception){
                Log.e(TAG, "gameSpeed could not be set")
            }
        }
    }

    fun setClickVolume(clickVolume: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setClickVolume(clickVolume)
            }catch (e: Exception){
                Log.e(TAG, "clickVolume could not be set")
            }
        }
    }

    fun setBubbleSelectVolume(bubbleselectVolume: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setBubbleSelectVolume(bubbleselectVolume)
            }catch (e: Exception){
                Log.e(TAG, "bubbleselectVolume could not be set")
            }
        }
    }

    fun setBubbleExplodeVolume(bubbleExplodeVolume: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setBubbleExplodeVolume(bubbleExplodeVolume)

            }catch (e: Exception){
                Log.e(TAG, "bubbleExplodeVolume could not be set")
            }
        }
    }

    fun setTappingVolume(tappingVolume: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setTappingVolume(tappingVolume)

            }catch (e: Exception){
                Log.e(TAG, "tappingVolume could not be set")
            }
        }
    }

    fun setSystemDefaultMode(it: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.setSystemTheme(it)
            }catch (e: Exception){
                Log.e(TAG, "darkTheme could not be set")
            }
        }

    }

    fun setHissVolume(it: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.setHissVolume(it)
            }catch (e: Exception){
                Log.e(TAG, "tappingVolume could not be set")
            }
        }
    }

}
