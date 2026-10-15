package com.antigravity.tictactoe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.antigravity.tictactoe.domain.model.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = Color.White,
    primaryContainer = NeonPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003554),
    onSecondaryContainer = Color.White,
    tertiary = NeonGold,
    background = BackgroundNavy,
    onBackground = Color(0xFFF1F5F9),
    surface = BackgroundSurfaceNavy,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = CardBackgroundNavy,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = BorderNavy
)

private val LightColorScheme = lightColorScheme(
    primary = NeonPinkDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD6E0),
    onPrimaryContainer = Color(0xFF470016),
    secondary = Color(0xFF0077B6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCAF0F8),
    onSecondaryContainer = Color(0xFF001F3F),
    tertiary = NeonGoldDark,
    background = BackgroundLight,
    onBackground = Color(0xFF0F172A),
    surface = BackgroundSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = CardBackgroundLight,
    onSurfaceVariant = Color(0xFF334155),
    outline = BorderLight
)

@Composable
fun TicTacToeTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
