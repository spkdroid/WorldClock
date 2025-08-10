package com.spkd.wordlclock.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = WorldClockDarkPrimary,
    primaryContainer = WorldClockDarkPrimaryVariant,
    secondary = WorldClockDarkSecondary,
    background = WorldClockDarkBackground,
    surface = WorldClockDarkSurface,
    onPrimary = WorldClockDarkOnPrimary,
    onSecondary = WorldClockDarkOnSecondary,
    onBackground = WorldClockDarkOnBackground,
    onSurface = WorldClockDarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = WorldClockPrimary,
    primaryContainer = WorldClockPrimaryVariant,
    secondary = WorldClockSecondary,
    background = WorldClockBackground,
    surface = WorldClockSurface,
    error = WorldClockError,
    onPrimary = WorldClockOnPrimary,
    onSecondary = WorldClockOnSecondary,
    onBackground = WorldClockOnBackground,
    onSurface = WorldClockOnSurface,
    onError = WorldClockOnError
)

@Composable
fun WorldClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom theme
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
