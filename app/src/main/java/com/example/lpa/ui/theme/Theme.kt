package com.example.lpa.ui.theme

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

// ─── Dark Color Scheme ────────────────────────────────────────────────────────
private val PrismDarkColorScheme = darkColorScheme(
    primary = PrismPrimary,
    onPrimary = PrismOnPrimary,
    primaryContainer = PrismPrimaryContainer,
    onPrimaryContainer = PrismOnPrimaryContainer,
    secondary = PrismSecondary,
    onSecondary = PrismOnSecondary,
    secondaryContainer = PrismSecondaryContainer,
    onSecondaryContainer = PrismOnSecondaryContainer,
    tertiary = PrismTertiary,
    onTertiary = PrismOnTertiary,
    tertiaryContainer = PrismTertiaryContainer,
    onTertiaryContainer = PrismOnTertiaryContainer,
    error = PrismError,
    onError = PrismOnError,
    errorContainer = PrismErrorContainer,
    onErrorContainer = PrismOnErrorContainer,
    background = PrismBackground,
    onBackground = PrismOnBackground,
    surface = PrismSurface,
    onSurface = PrismOnSurface,
    surfaceVariant = PrismSurfaceVariant,
    onSurfaceVariant = PrismOnSurfaceVariant,
    outline = PrismOutline
)

// ─── Light Color Scheme ───────────────────────────────────────────────────────
private val PrismLightColorScheme = lightColorScheme(
    primary = PrismPrimary,
    onPrimary = PrismOnPrimary,
    primaryContainer = PrismPrimaryContainer,
    onPrimaryContainer = PrismOnPrimaryContainer,
    secondary = PrismSecondary,
    onSecondary = PrismOnSecondary,
    secondaryContainer = PrismSecondaryContainer,
    onSecondaryContainer = PrismOnSecondaryContainer,
    tertiary = PrismTertiary,
    onTertiary = PrismOnTertiary,
    tertiaryContainer = PrismTertiaryContainer,
    onTertiaryContainer = PrismOnTertiaryContainer,
    error = PrismError,
    onError = PrismOnError,
    errorContainer = PrismErrorContainer,
    onErrorContainer = PrismOnErrorContainer,
    background = PrismBackgroundLight,
    onBackground = PrismOnBackgroundLight,
    surface = PrismSurfaceLight,
    onSurface = PrismOnSurfaceLight,
    surfaceVariant = PrismSurfaceVariantLight,
    outline = PrismOutlineLight
)

/**
 * PrismEsimLpa Material 3 theme.
 *
 * Supports dynamic color on Android 12+ (API 31) with a curated static
 * fallback color scheme for older API levels or when dynamic color is disabled.
 *
 * @param darkTheme     Whether to apply dark theme. Defaults to system setting.
 * @param dynamicColor  Whether to use dynamic colors (Monet). Defaults to true on API 31+.
 * @param content       The composable content to be themed.
 */
@Composable
fun PrismEsimLpaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> PrismDarkColorScheme
        else -> PrismLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PrismTypography,
        content = content
    )
}
