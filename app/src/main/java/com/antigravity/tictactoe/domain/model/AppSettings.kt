package com.antigravity.tictactoe.domain.model

enum class AppThemeMode {
    SYSTEM,
    DARK,
    LIGHT
}

data class AppSettings(
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val animationsEnabled: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.DARK
)
