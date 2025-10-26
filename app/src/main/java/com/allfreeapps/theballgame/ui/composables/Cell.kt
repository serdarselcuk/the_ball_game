package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.utils.getRadialGradientBrush

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    ballColorValue: Int,
    cellSize: Dp,
    isSelected: Boolean,
    gameSpeed: Int,
    onCellClick: () -> Unit,
    removeTheBall: () -> Unit
) {
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val boarderBrush = remember(cellSize.value, secondaryColor) {
        getRadialGradientBrush(
            ballSizePx = cellSize.value,
            xRate = 1f,
            yRate = 1f,
            radius = 0.5f,
            baseColor = secondaryColor
        )
    }

    Box(
        modifier = modifier
            .border(
                BorderStroke(
                    width = 1.dp,
                    brush = boarderBrush
                )
            )
            .clickable(
                onClick = onCellClick
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedBall(
            cellSize = cellSize,
            colorValue = ballColorValue,
            isBallSelected = isSelected,
            removeTheBall = removeTheBall,
            gameSpeed = gameSpeed
        )

    }
}


@Preview(showBackground = true)
@Composable
fun CellPreview() {
    Cell(
        modifier = Modifier,
        ballColorValue = 1,
        cellSize = 100.dp,
        isSelected = false,
        gameSpeed = 50,
        onCellClick = {},
        removeTheBall = {}
    )
}