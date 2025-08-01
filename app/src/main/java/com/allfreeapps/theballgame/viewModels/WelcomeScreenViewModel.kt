package com.allfreeapps.theballgame.viewModels

import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.utils.Constants
import com.allfreeapps.theballgame.utils.Constants.Companion.MAX_BALL_COUNT
import com.allfreeapps.theballgame.utils.Constants.Companion.WELCOME_SCREEN_GRID_SIZE
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.Vibrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val soundPlayerManager: SoundPlayerManager,
    vibrator: Vibrator,
    settingsRepository: SettingsRepository,
) : BaseViewModel(soundPlayerManager = soundPlayerManager, vibrator = vibrator) {
    companion object {
        fun randomInitSize(max: Int): Float = (5..max).random().toFloat()
        fun randomTargetSize(initial: Int, max: Int): Float = (initial..max).random().toFloat()
        fun randomColor(): Int = (1..6).random()
        fun randomSpeed(): Int = (1..2).random()
    }

    private val _errorState: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    override val errorState: StateFlow<Throwable?> = _errorState

    private val _state = MutableStateFlow<GameState?>(null)
    override val state: StateFlow<GameState?> = _state

    override fun logError(tag: String, exception: Exception) {
        _errorState.value = RuntimeException(tag, exception)
    }

    val _isMuted = MutableStateFlow(settingsRepository.isMuteOnStart.value)
    override val isMuted: StateFlow<Boolean> = _isMuted

    private var _vibrationTurnedOn = MutableStateFlow(settingsRepository.isVibrationTurnedOn.value)
    override val vibrationTurnedOn: StateFlow<Boolean> = _vibrationTurnedOn

    // order represents the position in the board and the value stands for the color
    private var _ballList = MutableStateFlow(Array(MAX_BALL_COUNT) { 0 })
    override val ballList: StateFlow<Array<Int>> = _ballList

    suspend fun createBall() {
        val currentBallList = _ballList.value
        val newBallArray = withContext(Dispatchers.Default) {
            val ballListCopy = currentBallList.copyOf()
            val emptySlots = ballListCopy.indices.filter { index -> ballListCopy[index] == 0 }
            if (emptySlots.isNotEmpty()) {
                val newBallPosition = emptySlots.random()
                ballListCopy[newBallPosition] = randomColor()
                ballListCopy // Return the modified copy
            } else {
                null
            }
        }

        if (newBallArray != null && newBallArray !== currentBallList) { // Check if a new array was actually created and is different
            _ballList.value = newBallArray // Update StateFlow on the original context (likely main)
        }
    }

    fun userClickedCreateBallButton() {
        viewModelScope.launch {
            createBall()
        }
    }

    fun updateCellCount(cellCountForLongerDimension: Int) {
        viewModelScope.launch {
            _ballList =
                MutableStateFlow(Array(cellCountForLongerDimension * WELCOME_SCREEN_GRID_SIZE) { 0 })
        }
    }

    fun removeBall(position: Int) {
        val ballList = _ballList.value.copyOf()
        if (ballList[position] != 0) {
            ballList[position] = 0
            _ballList.value = ballList
        }
    }

    fun changeSoundStatus() {
        playClickSound()
        _isMuted.value = !isMuted.value
    }

    data class BallData(
        var colorValue: Int,
        val initialSize: Float,
        val targetSize: Float,
        val gameSpeed: Int,
        val position: Int
    )


    fun createNewBall(maxSize: Int, position: Int, color: Int?): BallData {
        var initial = 0F
        var target = 0F
        while (target == initial) {
            initial = randomInitSize(maxSize)
            target = randomTargetSize(maxSize, maxSize * 3)
        }

        return BallData(
            colorValue = color ?: randomColor(),
            initialSize = initial,
            targetSize = target,
            gameSpeed = randomSpeed(),
            position = position
        )
    }

    fun createAnEmptyBall(position: Int): BallData {
        return BallData(
            colorValue = Constants.NO_BALL,
            initialSize = 0F,
            targetSize = 0F,
            gameSpeed = 0,
            position = position
        )
    }

}

