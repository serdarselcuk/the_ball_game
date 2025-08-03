package com.allfreeapps.theballgame.viewModels

import androidx.lifecycle.ViewModel
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.Vibrator
import com.allfreeapps.theballgame.viewModels.BallGameViewModel.Companion.REMOVE_BALLS_VIBRATION_DURATION
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel(
    private val soundPlayerManager: SoundPlayerManager,
    private val vibrator: Vibrator,
) : ViewModel() {
    abstract val isMuted: StateFlow<Boolean>
    abstract val ballList: StateFlow<Array<Int>>
    abstract val vibrationTurnedOn: StateFlow<Boolean>
    abstract val errorState: StateFlow<Throwable?>
    abstract val state: StateFlow<GameState?>


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

}