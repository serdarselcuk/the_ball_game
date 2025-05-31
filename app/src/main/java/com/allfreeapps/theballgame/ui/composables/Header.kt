package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor

@Composable
    fun Header(modifier: Modifier = Modifier, buttonContent: @Composable () -> Unit) {
        val orientation = LocalConfiguration.current.orientation

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(
                    start = when (orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> 20.dp
                            Configuration.ORIENTATION_LANDSCAPE -> 50.dp
                            else -> 0.dp
                        }

                ),
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold,
                color = HeaderTextColor,
                text = stringResource(R.string.header_label)
            )

            Spacer(
                Modifier.weight(1f)
            )

            buttonContent()
        }
    }

@Preview(
    showBackground = true,
    name = "Header",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape"
)
@Composable
fun Preview(){
    Header(
        Modifier,
        buttonContent = { RestartButton(GameState.GameNotStarted, {}) }
    )
}