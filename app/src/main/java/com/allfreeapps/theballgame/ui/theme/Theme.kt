package com.allfreeapps.theballgame.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = HeaderBackGround, // Purple80 - for header background
    onPrimary = HeaderTextColor, // PurpleGrey40 - for header text
    primaryContainer = StartButtonBackgroundColor, // Purple40 - for start button background
    onPrimaryContainer = StartButtonTextColor, // White - for start button text
    secondary = UserNameFieldColor, // GreyPurple - for username field
    background = BackgroundColor, // LightGray - for general background
    surface = CellBoarderColor, // DarkGray - for cell border
    onSurface = ScoreTextColor, // PurpleGrey40 - for score text
    error = DisabledColor, // Gray - for disabled elements
    onError = Pink80 // GameOverBackground - for game over background
)

private val LightColorScheme = lightColorScheme(
    primary = HeaderBackGround, // Purple80 - for header background
    onPrimary = HeaderTextColor, // PurpleGrey40 - for header text
    primaryContainer = StartButtonBackgroundColor, // Purple40 - for start button background
    onPrimaryContainer = StartButtonTextColor, // White - for start button text
    secondary = UserNameFieldColor, // GreyPurple - for username field
    background = BackgroundColor, // LightGray - for general background
    surface = CellBoarderColor, // DarkGray - for cell border
    onSurface = ScoreTextColor, // PurpleGrey40 - for score text
    error = DisabledColor, // Gray - for disabled elements
    onError = Pink80 // GameOverBackground - for game over background
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun TheBallGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}