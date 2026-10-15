package com.antigravity.tictactoe.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonCyanGlow
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.NeonPinkGlow

@Composable
fun PlayerSymbol(
    player: Player,
    modifier: Modifier = Modifier,
    drawProgress: Float = 1f,
    isWinning: Boolean = false,
    enablePulse: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by if (isWinning && enablePulse) {
        infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(modifier = modifier) {
        when (player) {
            Player.X -> NeonXSymbol(
                modifier = Modifier.fillMaxSize(),
                drawProgress = drawProgress,
                isWinning = isWinning,
                scale = pulseScale
            )
            Player.O -> NeonOSymbol(
                modifier = Modifier.fillMaxSize(),
                drawProgress = drawProgress,
                isWinning = isWinning,
                scale = pulseScale
            )
        }
    }
}

@Composable
fun NeonXSymbol(
    modifier: Modifier = Modifier,
    drawProgress: Float = 1f,
    isWinning: Boolean = false,
    scale: Float = 1f
) {
    val mainColor = if (isWinning) NeonGold else NeonPink
    val glowColor = if (isWinning) Color(0x66FFD166) else NeonPinkGlow

    Canvas(modifier = modifier.padding(14.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Scaled bounds
        val strokeWidth = (w * 0.14f) * scale
        val glowStrokeWidth = strokeWidth * 1.8f
        val halfSpan = (w * 0.36f) * scale

        val p1Start = Offset(cx - halfSpan, cy - halfSpan)
        val p1End = Offset(cx + halfSpan, cy + halfSpan)

        val p2Start = Offset(cx + halfSpan, cy - halfSpan)
        val p2End = Offset(cx - halfSpan, cy + halfSpan)

        // First stroke progress (0f..0.5f mapped to 0f..1f)
        val stroke1Progress = (drawProgress / 0.5f).coerceIn(0f, 1f)
        // Second stroke progress (0.5f..1f mapped to 0f..1f)
        val stroke2Progress = ((drawProgress - 0.5f) / 0.5f).coerceIn(0f, 1f)

        if (stroke1Progress > 0f) {
            val currEnd1 = Offset(
                x = p1Start.x + (p1End.x - p1Start.x) * stroke1Progress,
                y = p1Start.y + (p1End.y - p1Start.y) * stroke1Progress
            )
            // Glow layer
            drawLine(
                color = glowColor,
                start = p1Start,
                end = currEnd1,
                strokeWidth = glowStrokeWidth,
                cap = StrokeCap.Round
            )
            // Main crisp layer
            drawLine(
                color = mainColor,
                start = p1Start,
                end = currEnd1,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        if (stroke2Progress > 0f) {
            val currEnd2 = Offset(
                x = p2Start.x + (p2End.x - p2Start.x) * stroke2Progress,
                y = p2Start.y + (p2End.y - p2Start.y) * stroke2Progress
            )
            // Glow layer
            drawLine(
                color = glowColor,
                start = p2Start,
                end = currEnd2,
                strokeWidth = glowStrokeWidth,
                cap = StrokeCap.Round
            )
            // Main crisp layer
            drawLine(
                color = mainColor,
                start = p2Start,
                end = currEnd2,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun NeonOSymbol(
    modifier: Modifier = Modifier,
    drawProgress: Float = 1f,
    isWinning: Boolean = false,
    scale: Float = 1f
) {
    val mainColor = if (isWinning) NeonGold else NeonCyan
    val glowColor = if (isWinning) Color(0x66FFD166) else NeonCyanGlow

    Canvas(modifier = modifier.padding(14.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        val strokeWidth = (w * 0.13f) * scale
        val glowStrokeWidth = strokeWidth * 1.8f
        val radius = (w * 0.35f) * scale

        val sweep = 360f * drawProgress.coerceIn(0f, 1f)

        if (sweep > 0f) {
            val arcTopLeft = Offset(cx - radius, cy - radius)
            val arcSize = Size(radius * 2, radius * 2)

            // Glow layer
            drawArc(
                color = glowColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = glowStrokeWidth, cap = StrokeCap.Round)
            )

            // Main crisp layer
            drawArc(
                color = mainColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
