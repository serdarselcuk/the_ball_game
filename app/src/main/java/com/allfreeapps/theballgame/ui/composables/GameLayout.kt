package com.allfreeapps.theballgame.ui.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.BuildConfig
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.LearningMessages
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.util.Applogger
import com.allfreeapps.theballgame.viewModels.BallGameViewModel
import kotlin.math.abs

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameLayout(
    modifier: Modifier,
    viewModel: BallGameViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    gameOver: (int: Int) -> Unit = {}
) {
    val context = LocalContext.current
    val appLogger = viewModel.getLogger()
    val orientation = context.resources.configuration.orientation
    val allScores by viewModel.allScores.collectAsState()
    val scoresTableVisible = remember { mutableStateOf(false) }
    val scoreTableIsFullSize = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.pushLearningMessages(LearningMessages.BEFORE_START)
        viewModel.gameOverEvent.collect { finalScore ->
            gameOver(finalScore)
        }
    }

    Column(
        modifier = modifier
    ) {
        if (!scoreTableIsFullSize.value) {
            LearnerPopup(
                Modifier,
                Color.LightGray
            )
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitView(
                        viewModel,
                        Modifier.fillMaxSize(),
                        onSettingsClicked
                    )
                }

                Configuration.ORIENTATION_LANDSCAPE -> {
                    LandscapeView(
                        viewModel,
                        Modifier.fillMaxSize(),
                        onSettingsClicked
                    )
                }

                else -> {
                    appLogger.i("GameLayout", "Unknown orientation")
                }
            }

            // send logs if debug or start learner if release
            ButtonWithText(
                modifier = Modifier.padding(1.dp),
                buttonText = if (BuildConfig.DEBUG) "Send Logs" else "learn",
                onclick = {
                    if (BuildConfig.DEBUG) viewModel.sendLogs(context)
                    else {
                        viewModel.pushLearningMessages(LearningMessages.BEFORE_START)
                        viewModel.setUserAsAFreshUser(true)
                    }
                }
            )
        }

        val weighOfScoreColumn = remember { mutableFloatStateOf(0.3f) }
        val weightOfSpace2 = remember { mutableFloatStateOf(2f) }

        OpenableScoreBoard(
            modifier = Modifier
                .weight(weighOfScoreColumn.floatValue)
                .fillMaxWidth(),
            viewModel,
            allScores,
            weighOfScoreColumn,
            weightOfSpace2,
            scoreTableIsFullSize,
            scoresTableVisible
        )
    }

}

@Composable
fun OpenableScoreBoard(
    modifier: Modifier,
    viewModel: BallGameViewModel,
    allScores: List<Score> = emptyList(),
    weighOfScoreColumn: MutableFloatState,
    weightOfSpace2: MutableFloatState,
    scoreTableIsFullSize: MutableState<Boolean>,
    scoresTableVisible: MutableState<Boolean>
) {
    val appLogger = viewModel.getLogger()
    val lastGestureInProgressTime = remember { mutableLongStateOf(0L) }

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        appLogger.i("DragDebug", "Drag Start")
                    },
                    onDragEnd = {
                        appLogger.i("DragDebug", "Drag End")
                        lastGestureInProgressTime.longValue = System.currentTimeMillis()
                    },
                    onDragCancel = {
                        appLogger.i("DragDebug", "Drag Cancel")
                        lastGestureInProgressTime.longValue = System.currentTimeMillis()
                    },
                    onVerticalDrag = { change: PointerInputChange, dragAmount: Float ->
                        appLogger.i("DragDebug", "Drag Amount: $dragAmount")
                        val currentTime = System.currentTimeMillis()
                        change.consume()
                        if (abs(dragAmount) > 20) {
                            if (currentTime - lastGestureInProgressTime.longValue < 500) {
                                appLogger.i(
                                    "DragDebug",
                                    "Ignoring drag currentTime = $currentTime    " +
                                            "lastGestureTime = ${lastGestureInProgressTime.longValue} " +
                                            "differance = ${currentTime - lastGestureInProgressTime.longValue}"
                                )
                                return@detectVerticalDragGestures
                            }
                            lastGestureInProgressTime.longValue = currentTime
                            moveScoreBoardLine(
                                dragAmount,
                                appLogger,
                                openScoreboard = {
                                    scoresTableVisible.value = true
                                    weighOfScoreColumn.floatValue = 1.5f
                                    weightOfSpace2.floatValue = 0.1f

                                },
                                closeScoreboard = {
                                    scoresTableVisible.value = false
                                    scoreTableIsFullSize.value = false
                                    weighOfScoreColumn.floatValue = 0.3f
                                    weightOfSpace2.floatValue = 2f
                                },
                                scoreTableIsFullSize

                            )
                        }
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(weightOfSpace2.floatValue))
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            imageVector = if (scoresTableVisible.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = stringResource(
                if (scoresTableVisible.value) R.string.swipe_down_to_close
                else R.string.pull_up_for_scores
            ),

            )
        Spacer(Modifier.height(4.dp))
        if (scoresTableVisible.value) {
            ScoresTable(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                scores = allScores,
                onDeleteClicked = { id ->
                    viewModel.deleteScore(id)
                }
            )
        }
    }
}

fun moveScoreBoardLine(
    dragAmount: Float,
    appLogger: Applogger,
    openScoreboard: () -> Unit,
    closeScoreboard: () -> Unit,
    scoreTableIsFullSize: MutableState<Boolean>
) = if (dragAmount < -20) {
    appLogger.i("DragDebug", "dragging effective, drag amount = $dragAmount")
    openScoreboard()
    if (dragAmount < -45) {
        scoreTableIsFullSize.value = true
    } else {
    }
} else {
    closeScoreboard()
}