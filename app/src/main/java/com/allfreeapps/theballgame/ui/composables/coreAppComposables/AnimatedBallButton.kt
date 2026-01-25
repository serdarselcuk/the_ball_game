package com.allfreeapps.theballgame.ui.composables.coreAppComposables

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBallButton(
    modifier: Modifier = Modifier,
    targetSize: Dp = 90.dp,
    onClick: () -> Unit = {},
    animationCompleted: () -> Unit? = {},
    textOnTheBall: String? = null
) {

    val showText = remember { mutableStateOf(false) }

    AnimatedWelcomingBall(
        modifier = modifier
            .clickable(
                true,
                onClick = { onClick() }
            ),
        targetSize = targetSize,
        textOnTheBall = textOnTheBall?.let {
            if (showText.value) it else null
        },
        animationCompleted = {
            showText.value = true
            animationCompleted()
        }
    )
}