package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.model.GameState
import com.allfreeapps.theballgame.ui.theme.DisabledColor
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.ui.theme.StartButtonTextColor


@Composable
    fun RestartButton(
        gameStatus: GameState,
        onclick: () -> Unit
    ){
        Button(
            modifier = Modifier
                .padding(2.dp)
                .width(90.dp)
                .height(35.dp),
            onClick = { onclick() },
            contentPadding = PaddingValues(
                horizontal = 6.dp,
                vertical = 2.dp
            ),
            colors = ButtonColors(
                containerColor = StartButtonBackgroundColor,
                contentColor = StartButtonTextColor,
                DisabledColor,
                DisabledColor
            ),
            content = {
                Text(
                    text = if(gameStatus == GameState.GameNotStarted ) {
                        stringResource(R.string.start_game)
                    } else {
                        stringResource(R.string.restart_game)
                    }
                    ,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }

@Composable
fun MuteButton(
    modifier: Modifier = Modifier,
    isMuted: Boolean,
    onToggleMute: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onToggleMute
    ) {
        val icon: Painter
        val contentDesc: String

        if (isMuted) {
            icon = painterResource( R.drawable.ic_muted_sount_icon )
            contentDesc = "Unmute Sound"
        } else {
            icon = painterResource( R.drawable.ic_sound_icon )
            contentDesc = "Mute Sound"
        }

        Icon(
            painter = icon,
            contentDescription = contentDesc
        )
    }
}
@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onClicked
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_delete_icon),
            contentDescription = "Delete items"
        )
    }
}



@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    IconButton(
        modifier = modifier,
        onClick = onClick
    )
    {
        Icon(
            painter =  painterResource( R.drawable.settigns_icon ),
            contentDescription = "Settings"
        )

    }
}

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    onClick: () -> Unit
){
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = "SKIP", color = color, fontSize = 20.sp)
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewButtons(){
    RestartButton(GameState.GameNotStarted, onclick = {})
}


@Composable
@Preview(showBackground = true)
fun PreviewRestartButton(){
    RestartButton(GameState.UserTurn, onclick = {})
}

@Composable
@Preview(showBackground = true)
fun PreviewMuteButton(){
    MuteButton(isMuted = true, onToggleMute = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewMuteButtonOff(){
    MuteButton(isMuted = false, onToggleMute = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteSettingsButton(){
    SettingsButton(onClick = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteSkipButton(){
    SkipButton(onClick = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteDelteButton(){
    DeleteButton(onClicked = {})
}