package com.antigravity.tictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.domain.model.WinningCombination
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonGoldDark

@Composable
fun BoardCanvas(
    board: Board,
    winningCombination: WinningCombination?,
    isInteractive: Boolean,
    animationsEnabled: Boolean,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val winningIndices = winningCombination?.indices.orEmpty()

        // 3x3 Grid of interactive Cells
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        val player = board[index]
                        val isWinning = index in winningIndices

                        BoardCell(
                            player = player,
                            isWinning = isWinning,
                            isInteractive = isInteractive && player == null,
                            animationsEnabled = animationsEnabled,
                            onClick = { onCellClick(index) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }

        // Animated Winning Line Overlay
        if (winningCombination != null) {
            WinningLineOverlay(
                combination = winningCombination,
                animationsEnabled = animationsEnabled,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BoardCell(
    player: Player?,
    isWinning: Boolean,
    isInteractive: Boolean,
    animationsEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && isInteractive) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 700f),
        label = "cellPress"
    )

    // Progressive symbol drawing animation
    val drawProgress = remember(player != null) { Animatable(if (player != null && animationsEnabled) 0f else 1f) }
    LaunchedEffect(player) {
        if (player != null) {
            if (animationsEnabled) {
                drawProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
                )
            } else {
                drawProgress.snapTo(1f)
            }
        }
    }

    val shape = RoundedCornerShape(18.dp)

    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val borderColor = when {
        isWinning -> NeonGold
        player == Player.X -> Color(0xFFFF2A6D).copy(alpha = 0.5f)
        player == Player.O -> Color(0xFF00F2FE).copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    }

    val cellBackgroundBrush = if (isWinning) {
        Brush.radialGradient(
            colors = listOf(NeonGold.copy(alpha = 0.25f), surfaceColor)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(surfaceColor, surfaceColor.copy(alpha = 0.7f))
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isWinning) 8.dp else 3.dp,
                shape = shape,
                spotColor = if (isWinning) NeonGold else Color.Black.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(cellBackgroundBrush)
            .border(
                width = if (isWinning) 2.dp else 1.2.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isInteractive,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            PlayerSymbol(
                player = player,
                modifier = Modifier.fillMaxSize(),
                drawProgress = drawProgress.value,
                isWinning = isWinning,
                enablePulse = animationsEnabled
            )
        }
    }
}

@Composable
private fun WinningLineOverlay(
    combination: WinningCombination,
    animationsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val lineProgress = remember(combination) { Animatable(if (animationsEnabled) 0f else 1f) }

    LaunchedEffect(combination) {
        if (animationsEnabled) {
            lineProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
            )
        } else {
            lineProgress.snapTo(1f)
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val cellW = w / 3f
        val cellH = h / 3f

        // Cell centers (0.5, 1.5, 2.5)
        val rawStartX = (combination.startCol + 0.5f) * cellW
        val rawStartY = (combination.startRow + 0.5f) * cellH
        val rawEndX = (combination.endCol + 0.5f) * cellW
        val rawEndY = (combination.endRow + 0.5f) * cellH

        // Direction unit vector to slightly extend line past cell centers for a balanced game aesthetic
        val dx = rawEndX - rawStartX
        val dy = rawEndY - rawStartY
        val dist = kotlin.math.hypot(dx, dy)
        val extend = cellW * 0.22f

        val startX: Float
        val startY: Float
        val endX: Float
        val endY: Float

        if (dist > 0f) {
            val uX = dx / dist
            val uY = dy / dist
            startX = rawStartX - uX * extend
            startY = rawStartY - uY * extend
            endX = rawEndX + uX * extend
            endY = rawEndY + uY * extend
        } else {
            startX = rawStartX
            startY = rawStartY
            endX = rawEndX
            endY = rawEndY
        }

        val currEndX = startX + (endX - startX) * lineProgress.value
        val currEndY = startY + (endY - startY) * lineProgress.value

        val strokeWidth = cellW * 0.08f
        val glowWidth = strokeWidth * 2.5f

        // Outer Neon Glow Layer
        drawLine(
            color = Color(0x66FFD166),
            start = Offset(startX, startY),
            end = Offset(currEndX, currEndY),
            strokeWidth = glowWidth,
            cap = StrokeCap.Round
        )

        // Middle Vibrant Layer
        drawLine(
            color = NeonGoldDark,
            start = Offset(startX, startY),
            end = Offset(currEndX, currEndY),
            strokeWidth = strokeWidth * 1.3f,
            cap = StrokeCap.Round
        )

        // Core Crisp Gold Line
        drawLine(
            color = NeonGold,
            start = Offset(startX, startY),
            end = Offset(currEndX, currEndY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
