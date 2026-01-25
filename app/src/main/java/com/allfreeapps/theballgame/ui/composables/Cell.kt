package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    ballColorValue: Int,
    isSelected: Boolean,
    gameSpeed: Int,
    removeTheBall: (Int) -> Unit
) {
    val sizeOfTheCell = remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier
            .padding(6.dp)
            .onGloballyPositioned {
                sizeOfTheCell.value = it.size
            },
        contentAlignment = Alignment.Center,
    ) {

        AnimatedBall(
            cellSize = sizeOfTheCell.value.height,
            colorValue = ballColorValue,
            isBallSelected = isSelected,
            removeTheBall = { purpose -> removeTheBall(purpose) },
            gameSpeed = gameSpeed
        )

    }
}


@Preview(showBackground = true)
@Composable
fun CellPreview() {
    Box(
        Modifier
            .size(250.dp)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            ),
        contentAlignment = Alignment.Center
    )
    {
        Cell(
            modifier = Modifier,
            ballColorValue = 3,
            isSelected = false,
            gameSpeed = 50,
            removeTheBall = {}
        )
    }
}