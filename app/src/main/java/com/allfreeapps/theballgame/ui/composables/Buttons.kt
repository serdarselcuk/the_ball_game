package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.DisabledColor
import com.allfreeapps.theballgame.ui.theme.StartButtonBackgroundColor
import com.allfreeapps.theballgame.ui.theme.StartButtonTextColor


@Composable
fun ButtonWithText(
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(R.string.restart_game),
    colors: ButtonColors = ButtonColors(
        containerColor = StartButtonBackgroundColor,
        contentColor = StartButtonTextColor,
        disabledContainerColor = DisabledColor,
        disabledContentColor = DisabledColor
    ),
    onclick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onclick()
        },
        contentPadding = PaddingValues(
            horizontal = 6.dp,
            vertical = 2.dp
        ),
        colors = colors,
        content = {
            Text(
                text = buttonText,
                maxLines = 1,
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
        onClick = {
            onToggleMute()
        }
    ) {
        val icon: Painter
        val contentDesc: String

        if (isMuted) {
            icon = painterResource(R.drawable.ic_muted_sount_icon)
            contentDesc = "Unmute Sound"
        } else {
            icon = painterResource(R.drawable.ic_sound_icon)
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
    onClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onClicked()
        }
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
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier.fillMaxSize(),
            onClick = {
                onClick()
            }
        )
        {
            Icon(
                painter = painterResource(R.drawable.settigns_icon),
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                color = Color.Black,
                width = 2.dp,
                shape = CircleShape
            )
    ) {
        TextButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
        ) {
            Text(text = "SKIP", color = color, fontSize = 20.sp)
        }
    }
}


@Composable
fun SaveScoreButton(
    modifier: Modifier,
    username: String,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier, // Make Button take 50% of column width
        shape = ButtonDefaults.elevatedShape, // Use the default button shape
        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor.copy(alpha = 0.5f)),
        enabled = username.isNotBlank() // Enable button only if username is not empty
    ) {
        Text("SAVE SCORE")
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewButtons() {
    ButtonWithText(
        Modifier
            .height(52.dp)
            .width(52.dp),
        onclick = {}
    )
}


@Composable
@Preview(showBackground = true)
fun PreviewRestartButton() {
//    RestartButton(
//        true,
//        Modifier
//            .padding(2.dp)
//            .width(90.dp)
//            .height(35.dp),
//        GameState.UserTurn,
//        onclick = {}
//    )
}

@Composable
@Preview(showBackground = true)
fun PreviewMuteButton() {
    MuteButton(isMuted = true, onToggleMute = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewMuteButtonOff() {
    MuteButton(isMuted = false, onToggleMute = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteSettingsButton() {
    SettingsButton(
        Modifier
            .height(50.dp)
            .width(50.dp), onClick = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteSkipButton() {
//    SkipButton(onClick = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewMuteDelteButton() {
//    DeleteButton(onClicked = {})
}