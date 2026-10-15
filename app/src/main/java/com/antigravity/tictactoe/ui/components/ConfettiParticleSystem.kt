package com.antigravity.tictactoe.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.NeonPurple
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val initialX: Float,
    val initialY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val isCircle: Boolean
)

@Composable
fun ConfettiParticleSystem(
    modifier: Modifier = Modifier,
    particleCount: Int = 55
) {
    var animProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animProgress = 1f
    }

    val progress by animateFloatAsState(
        targetValue = animProgress,
        animationSpec = tween(durationMillis = 1800, easing = LinearEasing),
        label = "confetti"
    )

    val colors = listOf(NeonPink, NeonCyan, NeonGold, NeonPurple, Color.White)

    val particles = remember {
        val rand = Random(System.currentTimeMillis())
        List(particleCount) {
            val angle = rand.nextDouble(0.0, 2 * Math.PI)
            val speed = rand.nextFloat() * 700f + 250f
            Particle(
                initialX = 0.5f,
                initialY = 0.45f,
                velocityX = (cos(angle) * speed).toFloat(),
                velocityY = (sin(angle) * speed).toFloat() - 250f, // Initial upward burst
                color = colors[rand.nextInt(colors.size)],
                size = rand.nextFloat() * 14f + 8f,
                rotationSpeed = rand.nextFloat() * 720f - 360f,
                isCircle = rand.nextBoolean()
            )
        }
    }

    if (progress < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val gravity = 900f * progress * progress

            val alpha = (1f - progress).coerceIn(0f, 1f)

            particles.forEach { p ->
                val px = (p.initialX * w) + (p.velocityX * progress)
                val py = (p.initialY * h) + (p.velocityY * progress) + (0.5f * gravity)

                val rotation = p.rotationSpeed * progress

                rotate(degrees = rotation, pivot = Offset(px, py)) {
                    if (p.isCircle) {
                        drawCircle(
                            color = p.color.copy(alpha = alpha),
                            radius = p.size / 2f,
                            center = Offset(px, py)
                        )
                    } else {
                        drawRect(
                            color = p.color.copy(alpha = alpha),
                            topLeft = Offset(px - p.size / 2f, py - p.size / 3f),
                            size = Size(p.size, p.size * 0.6f)
                        )
                    }
                }
            }
        }
    }
}
