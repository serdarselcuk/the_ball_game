package com.allfreeapps.theballgame.viewModels

import androidx.lifecycle.ViewModel
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.util.Applogger
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.Vibrator
import com.allfreeapps.theballgame.viewModels.BallGameViewModel.Companion.REMOVE_BALLS_VIBRATION_DURATION
import com.allfreeapps.theballgame.viewModels.BallGameViewModel.Companion.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel(
    private val soundPlayerManager: SoundPlayerManager,
    private val vibrator: Vibrator,
    protected val appLogger: Applogger
) : ViewModel() {
    abstract val isMuted: StateFlow<Boolean>
    abstract val vibrationTurnedOn: StateFlow<Boolean>
    abstract val errorState: StateFlow<Throwable?>
    private val _state = MutableStateFlow<GameState?>(GameState.GAME_NOT_STARTED)
    val state: StateFlow<GameState?> = _state


    fun vibrate(duration: Long) {
        if (vibrationTurnedOn.value) vibrator.vibrate(duration)
    }

    fun playClickSound() {
        soundPlayerManager.playClickSound(isMuted.value)
    }

    fun playBubbleExplodeSound() {
        vibrate(REMOVE_BALLS_VIBRATION_DURATION)
        soundPlayerManager.playBubbleExplodeSound(isMuted.value)
    }

    fun playEmptyTapSound() {
        soundPlayerManager.playEmptyTapSound(isMuted.value)
    }

    fun playFilledTapSound() {
        soundPlayerManager.playFilledTapSound(isMuted.value)
    }

    fun playHissSound() {
        soundPlayerManager.playHissSound(isMuted.value)
    }


    fun releaseSoundManagers() {
        soundPlayerManager.releaseAll()
    }

    abstract fun logError(tag: String, exception: Exception)

    fun setState(gameState: GameState) {
        appLogger.i(TAG, "setState: $gameState")
        _state.value = gameState
    }

}