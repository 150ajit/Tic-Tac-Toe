package com.antigravity.tictactoe.ui.screens.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.NeonPurple

private data class FloatingElement(
    val relX: Float,
    val relY: Float,
    val size: Float,
    val isX: Boolean,
    val color: Color,
    val rotationSpeed: Float,
    val floatOffsetSpeed: Float
)

@Composable
fun HomeBackgroundEffect(
    modifier: Modifier = Modifier,
    animationsEnabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bgFloat")

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (animationsEnabled) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val elements = listOf(
        FloatingElement(0.12f, 0.14f, 44f, true, NeonPink.copy(alpha = 0.15f), 1.2f, 1f),
        FloatingElement(0.85f, 0.18f, 52f, false, NeonCyan.copy(alpha = 0.15f), -0.8f, 1.4f),
        FloatingElement(0.18f, 0.52f, 36f, false, NeonCyan.copy(alpha = 0.12f), 1.0f, 0.8f),
        FloatingElement(0.88f, 0.62f, 48f, true, NeonPink.copy(alpha = 0.14f), -1.1f, 1.2f),
        FloatingElement(0.22f, 0.86f, 54f, true, NeonPurple.copy(alpha = 0.16f), 0.7f, 1.5f),
        FloatingElement(0.78f, 0.88f, 40f, false, NeonCyan.copy(alpha = 0.14f), -1.3f, 0.9f)
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        elements.forEach { elem ->
            val floatY = kotlin.math.sin(Math.toRadians((phase * elem.floatOffsetSpeed).toDouble())).toFloat() * 18f
            val px = elem.relX * w
            val py = elem.relY * h + floatY
            val rot = phase * elem.rotationSpeed

            rotate(degrees = rot, pivot = Offset(px, py)) {
                if (elem.isX) {
                    val half = elem.size / 2f
                    drawLine(
                        color = elem.color,
                        start = Offset(px - half, py - half),
                        end = Offset(px + half, py + half),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = elem.color,
                        start = Offset(px + half, py - half),
                        end = Offset(px - half, py + half),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                } else {
                    drawCircle(
                        color = elem.color,
                        radius = elem.size / 2f,
                        center = Offset(px, py),
                        style = Stroke(width = 6f)
                    )
                }
            }
        }
    }
}
