package com.contactunifier.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Color palette
private val PrimaryColor = Color(0xFF00897B) // Deep Teal
private val SecondaryColor = Color(0xFFFFB74D) // Gold
private val TertiaryColor = Color(0xFFFF7043) // Coral
private val ErrorColor = Color(0xFFB00020)
private val BackgroundLight = Color(0xFFFAFAFA)
private val BackgroundDark = Color(0xFF121212)
private val SurfaceLight = Color(0xFFFFFFFF)
private val SurfaceDark = Color(0xFF1E1E1E)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    error = ErrorColor,
    background = BackgroundLight,
    surface = SurfaceLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    error = ErrorColor,
    background = BackgroundDark,
    surface = SurfaceDark,
)

@Composable
fun ContactUnifierTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
