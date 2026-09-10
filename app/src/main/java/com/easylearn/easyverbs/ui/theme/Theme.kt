package com.easylearn.easyverbs.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo900,
    secondary = Purple500,
    onSecondary = Color.White,
    secondaryContainer = Indigo50,
    onSecondaryContainer = Indigo800,
    tertiary = Emerald500,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate800,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,
    error = Color(0xFFEF4444),
    onError = Color.White,
    outline = Slate200
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo400,
    onPrimary = Indigo900,
    primaryContainer = Indigo800,
    onPrimaryContainer = Indigo100,
    secondary = Purple500,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = Indigo200,
    tertiary = Emerald500,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Slate400,
    error = Color(0xFFF87171),
    onError = Color.Black,
    outline = Slate700
)

@Composable
fun EasyVerbsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
