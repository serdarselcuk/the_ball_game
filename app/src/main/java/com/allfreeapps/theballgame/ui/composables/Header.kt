package com.allfreeapps.theballgame.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.theme.HeaderBackGround
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor

class Header(
    val viewModel: BallGameViewModel,
    val button: Buttons
) {

    @Composable
    fun build(modifier: Modifier = Modifier):Header {
        val orientation = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        val headerHeight = if (orientation) 50.dp else 40.dp
        val startPadding = if(orientation) 20.dp else 50.dp

        Row(
            modifier = modifier
                .height(headerHeight)
                .background(HeaderBackGround),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(
                    start = startPadding
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
            button.restartButton(viewModel)
        }

        return this

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
        BallGameViewModel(LocalContext.current),
        Buttons()
    ).build()
}