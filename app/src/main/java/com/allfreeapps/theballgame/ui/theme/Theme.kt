package com.allfreeapps.theballgame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = primaryBackGround_dark,//header like primary obj
    onPrimary = textsOnPrimaryBackground_dark,
    secondary = secondaryBackGround_dark,
    onSecondary = textsOnSecondary_dark,
    tertiaryContainer = tertiaryBackGround_dark,
    tertiary = onTertiary_dark,
    onTertiary = textsOnTertiary_dark,
    surface = surfaceBackGround_dark,//main background
    onSurface = textOnSurface_dark,
    inverseOnSurface = textShadowOnText_dark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryBackGround_light,//header like primary obj
    onPrimary = textsOnPrimaryBackground_light,
    secondary = secondaryBackGround_light,
    onSecondary = textsOnSecondary_light,
    tertiaryContainer = tertiaryBackGround_light,
    tertiary = onTertiary_light,
    onTertiary = textsOnTertiary_light,
    surface = surfaceBackGround_light,//main background
    onSurface = textsOnSurface_light,
    inverseOnSurface = textShadowOnText_light
)


@Composable
fun TheBallGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    followSystem: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        followSystem -> {
            if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
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