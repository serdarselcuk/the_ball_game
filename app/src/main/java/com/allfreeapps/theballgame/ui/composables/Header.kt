package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.GameText

@Composable
fun Header(
    modifier: Modifier = Modifier,
    content: List<@Composable () -> Unit>,
    isLandscape: Boolean = false,
    fontSize: Float = 24f

) {
    val headerTextStyle = TextStyle(
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = fontSize.sp,
        shadow = Shadow(
            color = MaterialTheme.colorScheme.inverseOnSurface,
            offset = Offset(2f, 2f),
            blurRadius = 4f
        )
    )

    val headerBackGroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.surface
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    if (isLandscape) {
        Column(
            modifier = modifier
                .background(headerBackGroundBrush),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameText(
                modifier = Modifier.padding(
                    start = 3.dp
                ),
                text = stringResource(R.string.header_label).replace(" ", "\n"),
                style = headerTextStyle
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    Modifier.weight(1f)
                )
                for (item in content) {
                    item()
                }
            }
        }
    } else {
        Row(
            modifier = modifier
                .background(headerBackGroundBrush),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            GameText(
                modifier = Modifier.padding(
                    start = 20.dp
                ),
                text = stringResource(R.string.header_label),
                style = headerTextStyle
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    Modifier.weight(1f)
                )
                for (item in content) {
                    item()
                }
            }

        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Header",
    device = "spec:width=1800dp,height=800dp,dpi=240,orientation=landscape",
)
@Composable
fun Preview() {

    GameText(
        modifier = Modifier.padding(
            start = 50.dp
        ),
        text = stringResource(R.string.header_label),
        style = TextStyle(
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            shadow = Shadow(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                offset = Offset(2f, 2f),
                blurRadius = 4f
            )
        )
    )
}