package com.allfreeapps.theballgame.viewModels

import android.content.res.Resources
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.allfreeapps.theballgame.model.GameState
import com.allfreeapps.theballgame.service.SettingsRepository
import com.allfreeapps.theballgame.util.Applogger
import com.allfreeapps.theballgame.utils.Constants.Companion.MAX_BALLS_ON_WELCOME_SCREEN
import com.allfreeapps.theballgame.utils.SoundPlayerManager
import com.allfreeapps.theballgame.utils.Vibrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val soundPlayerManager: SoundPlayerManager,
    vibrator: Vibrator,
    settingsRepository: SettingsRepository,
    private val appLogger: Applogger
) : BaseViewModel(soundPlayerManager = soundPlayerManager, vibrator = vibrator) {
    companion object {
        private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        fun randomTargetSize(max: Int): Float = (25..max).random().toFloat()
        fun randomColor(): Int = (1..6).random()
        fun randomSpeed(): Int = ((50..200).random()) * 10

        // Screen size is obtained from Resources.getSystem().displayMetrics
        // The ball's size (max 120dp) needs to be considered when generating random positions.
        fun randomPosition(): Array<Int> = arrayOf(
            (0..(screenWidth - 120.dpToPx())).random(),
            (50..(screenHeight - 120.dpToPx() - 55)).random()
        )
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
    private var _ballList = MutableStateFlow(Array<BallData?>(MAX_BALLS_ON_WELCOME_SCREEN) { null })
    override val ballList: StateFlow<Array<Int>> = MutableStateFlow(Array(0) { 0 })
    private val _welcoming_ballList = MutableStateFlow(
        mutableStateListOf(
            BallData(
                colorValue = 5,
                targetSize = 100F,
                gameSpeed = 2000,
                position = arrayOf(335, 50)
            ),
            BallData(
                colorValue = 1,
                targetSize = 100F,
                gameSpeed = 500,
                position = arrayOf(0, 585)
            ),
            BallData(
                colorValue = 1,
                targetSize = 100F,
                gameSpeed = 500,
                position = arrayOf(150, 555)
            ),
            BallData(
                colorValue = 1,
                targetSize = 100F,
                gameSpeed = 500,
                position = arrayOf(285, 385)
            ),
            BallData(
                colorValue = 1,
                targetSize = 100F,
                gameSpeed = 500,
                position = arrayOf(50, 1505)
            )
        )
    )

    val welcoming_ballList: MutableStateFlow<SnapshotStateList<BallData>> = _welcoming_ballList

    fun changeBall(index: Int) {
        viewModelScope.launch {
            val newBall = BallData(
                targetSize = randomTargetSize(120),
                colorValue = randomColor(),
                gameSpeed = randomSpeed(),
                position = validatedRandomPosition()
            )
            val newList: MutableList<BallData> = welcoming_ballList.value

            newList.removeAt(index)
            newList.add(index, newBall)

            _welcoming_ballList.value = newList.toMutableStateList()
        }
    }

    suspend fun validatedRandomPosition(): Array<Int> {
        var position = randomPosition()
        viewModelScope.launch {

            var positionOverlapping = true

            while (positionOverlapping) {
                for (ball in welcoming_ballList.value) {
                    if (
                        ball.overlapping(position)
                    )
                        position = randomPosition()
                    break
                }
                positionOverlapping = false
            }
            return@launch
        }.join()
        return position
    }

    fun changeSoundStatus() {
        playClickSound()
        _isMuted.value = !isMuted.value
    }

    data class BallData(
        var colorValue: Int,
        val targetSize: Float,
        val gameSpeed: Int,
        var position: Array<Int>
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BallData

            if (colorValue != other.colorValue) return false
            if (targetSize != other.targetSize) return false
            if (gameSpeed != other.gameSpeed) return false
            if (!position.contentEquals(other.position)) return false

            return true
        }


        fun overlapping(array: Array<Int>): Boolean =
            position[0].toFloat() in this.position[0] + this.targetSize..this.position[0] + this.targetSize &&
                    this.position[1].toFloat() in this.position[1] + this.targetSize..this.position[1] + this.targetSize

        override fun hashCode(): Int {
            var result = colorValue
            result = 31 * result + targetSize.hashCode()
            result = 31 * result + gameSpeed
            result = 31 * result + position.contentHashCode()
            return result
        }
    }
}

// Extension function to convert dp to pixels
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

