package com.antigravity.tictactoe.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Neon & Brand Colors
val NeonPink = Color(0xFFFF2A6D)
val NeonPinkLight = Color(0xFFFF5E8A)
val NeonPinkDark = Color(0xFFC7004C)
val NeonPinkGlow = Color(0x66FF2A6D)

val NeonCyan = Color(0xFF00F2FE)
val NeonCyanLight = Color(0xFF4FACFE)
val NeonCyanDark = Color(0xFF00B4D8)
val NeonCyanGlow = Color(0x6600F2FE)

val NeonPurple = Color(0xFF9D4EDD)
val NeonPurpleDark = Color(0xFF5A189A)
val NeonPurpleGlow = Color(0x559D4EDD)

val NeonGold = Color(0xFFFFD166)
val NeonGoldDark = Color(0xFFFFB703)

// Dark Theme Surfaces
val BackgroundNavy = Color(0xFF0B0E1B)
val BackgroundSurfaceNavy = Color(0xFF13172E)
val CardBackgroundNavy = Color(0xFF1B203D)
val CellBackgroundNavy = Color(0xFF232A4F)
val BorderNavy = Color(0xFF2E3766)

// Light Theme Surfaces (rich crisp neon-pop look)
val BackgroundLight = Color(0xFFF0F4F8)
val BackgroundSurfaceLight = Color(0xFFFFFFFF)
val CardBackgroundLight = Color(0xFFE2E8F0)
val CellBackgroundLight = Color(0xFFD6E0EA)
val BorderLight = Color(0xFFCBD5E1)

// Common Gradients
val PrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF2A6D), Color(0xFF9D4EDD))
)

val SecondaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
)

val CardGlowGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF232A4F), Color(0xFF13172E))
)

val WinGoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFD166), Color(0xFFFF9E00))
)
