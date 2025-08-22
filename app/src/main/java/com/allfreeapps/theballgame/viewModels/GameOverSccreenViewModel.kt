package com.allfreeapps.theballgame.viewModels

import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.service.ScoreRepository
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.util.Applogger
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.Vibrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GameOverScreenViewModel @Inject constructor(
    private val repository: ScoreRepository,
    vibrator: Vibrator,
    soundPlayerManager: SoundPlayerManager,
    appLogger: Applogger,
    settingsRepository: SettingsRepository,
) : BaseViewModel(soundPlayerManager, vibrator, appLogger) {

    override val isMuted = MutableStateFlow(settingsRepository.isMuteOnStart.value)
    override val vibrationTurnedOn = MutableStateFlow(settingsRepository.isVibrationTurnedOn.value)
    override val errorState: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    override fun logError(tag: String, exception: Exception) {
        appLogger.e("Error captured", tag, exception)
        errorState.value = RuntimeException(tag, exception)
    }

    fun saveScoreClicked(userName: String, score: Int) {
        playClickSound()
        saveScore(userName, score)

    }

    fun skipClicked() {
        playClickSound()
        setState(GameState.GAME_NOT_STARTED)
    }


    private fun saveScore(userName: String, score: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScore(
                Score(
                    id = null,
                    firstName = userName,
                    lastName = "",
                    score = score,
                    date = Date()
                )
            )
        }
    }


}