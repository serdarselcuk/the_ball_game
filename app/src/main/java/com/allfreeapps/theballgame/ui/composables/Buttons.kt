package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.GameText

@Composable
fun ButtonWithText(
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(R.string.restart_game),
    colors: ButtonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        disabledContentColor = MaterialTheme.colorScheme.surface
    ),
    enabled: Boolean = true,
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
        enabled = enabled,
        content = {
            GameText(
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
        val icon: ImageVector
        val contentDesc: String

        if (isMuted) {
            icon = Icons.Filled.MusicOff //painterResource(R.drawable.ic_muted_sount_icon)
            contentDesc = "Unmute Sound"
        } else {
            icon = Icons.Filled.MusicNote//painterResource(R.drawable.ic_sound_icon) as ImageVector
            contentDesc = "Mute Sound"
        }

        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
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
            imageVector = Icons.Filled.Delete,
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
            },
            content = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        )
    }
}

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
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
            GameText(text = "SKIP", fontSize = 20.sp)
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
        enabled = username.isNotBlank() // Enable button only if username is not empty
    ) {
        GameText("SAVE SCORE")
    }
}


@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = {
            onClick()
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Back button",
            tint = MaterialTheme.colorScheme.onBackground
        )
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
fun PreviewButtonsDisabled() {
    ButtonWithText(
        Modifier
            .height(52.dp)
            .width(52.dp),
        onclick = {},
        enabled = false
    )
}

@Composable
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun PreviewButtonsDarkMode() {
    ButtonWithText(
        Modifier
            .height(52.dp)
            .width(52.dp),
        onclick = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun PreviewButtonsDisabledDarkMode() {
    ButtonWithText(
        Modifier
            .height(52.dp)
            .width(52.dp),
        onclick = {},
        enabled = false
    )
}

@Preview
@Composable
fun PreviewBackButton() {
    BackButton(onClick = {})
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
            .width(50.dp), onClick = {}
    )
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