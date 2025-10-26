package com.allfreeapps.theballgame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GenericBackgroundColor_dark, // The primary color is the color displayed most frequently across your app’s * screens and components.
    onPrimary = GenericTextColor_dark,
    secondary = SecondaryBackGroundColor_dark, // header
    onSecondary = SecondaryTextColor_dark,
    tertiary = rarelyUserBackgroundColor_dark,
    onTertiary = rarelyUsedTextColor_dark,
    tertiaryContainer = rarelyUsedContainerColor_dark,
    onTertiaryContainer = rarelyUsedContainerTextColor_dark,
    surface = startButtonColor_dark // start button
)

private val LightColorScheme = lightColorScheme(
    primary = GenericBackgroundColor_light,
    onPrimary = GenericTextColor_light,
    secondary = SecondaryBackGroundColor_light,
    onSecondary = SecondaryTextColor_light,
    tertiary = rarelyUserBackgroundColor_light,
    onTertiary = rarelyUsedTextColor_light,
    tertiaryContainer = rarelyUsedContainerColor_light,
    onTertiaryContainer = rarelyUsedContainerTextColor_light,
    surface = startButtonColor_light // start button
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