package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor

class Header(
    val viewModel: BallGameViewModel,
    val button: Buttons
) {

    @Composable
    fun build():Header {
        Row(
            modifier = Modifier.height(50.dp).background(HeaderBackGround),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                ),
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold,
                color = HeaderTextColor,
                text = "THE COLOR GAME"
            )

            Spacer(
                Modifier.weight(1f)
            )
            button.restartButton(viewModel)
        }

        return this

    }
}