package com.antigravity.tictactoe.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.components.ConfettiParticleSystem
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.PlayerSymbol
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.SecondaryButtonGradient
import com.antigravity.tictactoe.ui.theme.WinGoldGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDialog(
    status: GameStatus,
    title: String,
    subtitle: String,
    winningPlayer: Player?,
    animationsEnabled: Boolean,
    onPlayAgain: () -> Unit,
    onNewGame: () -> Unit,
    onHome: () -> Unit
) {
    val isWin = status is GameStatus.Won

    Box(modifier = Modifier.fillMaxWidth()) {
        if (isWin && animationsEnabled) {
            ConfettiParticleSystem(
                modifier = Modifier.matchParentSize(),
                particleCount = 55
            )
        }

        BasicAlertDialog(
            onDismissRequest = { /* Modal: require user action */ },
            modifier = Modifier.padding(24.dp)
        ) {
            val shape = RoundedCornerShape(26.dp)
            val borderColor = if (isWin) NeonGold else NeonCyan.copy(alpha = 0.6f)

            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(width = 2.dp, color = borderColor, shape = shape)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Symbol / Trophy Icon
                if (winningPlayer != null) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                (if (winningPlayer == Player.X) NeonPink else NeonCyan).copy(alpha = 0.15f)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PlayerSymbol(
                            player = winningPlayer,
                            isWinning = true,
                            enablePulse = animationsEnabled
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isWin) NeonGold else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )

                if (subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions: Play Again
                GameButton(
                    text = "PLAY AGAIN",
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth(),
                    gradient = if (isWin) WinGoldGradient else SecondaryButtonGradient,
                    textColor = if (isWin) Color.Black else Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary Actions: New Game & Home
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameButton(
                        text = "NEW MATCH",
                        onClick = onNewGame,
                        modifier = Modifier.weight(1f),
                        gradient = SecondaryButtonGradient
                    )
                    GameButton(
                        text = "HOME",
                        onClick = onHome,
                        modifier = Modifier.weight(1f),
                        gradient = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(Color(0xFF2C324B), Color(0xFF1E2235))
                        ),
                        borderColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}
