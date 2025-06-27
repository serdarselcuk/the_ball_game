package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    onMuteClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    restartButtonOnClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header(
            Modifier
                .fillMaxWidth()
                .height(90.dp),
            listOf(
                {
                    MuteButton(isMuted = isMuted, onToggleMute = { onMuteClicked() })
                },
                {
                    SettingsButton(Modifier.width(50.dp), onClick = { onSettingsClicked() } )
                }
            )
        )
        Spacer(modifier = Modifier.width(50.dp))

        // to draw a round button with text having same effect on the picture
        Box(
            Modifier
                .background(StartButtonBackgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier
                .width(90.dp)
                .height(90.dp)) {
                val strokeWidth = 15.dp.toPx()
                drawArc(
                    color = Color.White,
                    startAngle = 210f,
                    sweepAngle = 30f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth, strokeWidth ),
                    size = size.copy(width = size.width - strokeWidth*2, height = size.height - strokeWidth*2),
                    style = Stroke(width = strokeWidth/1.5f, cap = StrokeCap.Round)
                )
            }
            ButtonWithText(
                Modifier
                    .padding(2.dp)
                    .width(90.dp)
                    .height(90.dp),
                buttonText = stringResource(R.string.start_game),
                onclick = { restartButtonOnClick() }
            )
        }

        Image(

            painter = painterResource(id = R.drawable.ic_launcher_icon),
            contentDescription = "Background",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomePage(){
    WelcomeScreen(isMuted = false, onMuteClicked = {}, onSettingsClicked = {})

}