package com.antigravity.tictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink

@Composable
fun ScoreBoard(
    player1Label: String,
    player1Symbol: Player,
    player1Score: Int,
    isPlayer1Turn: Boolean,
    player2Label: String,
    player2Symbol: Player,
    player2Score: Int,
    isPlayer2Turn: Boolean,
    drawsScore: Int,
    modifier: Modifier = Modifier
) {
    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        cornerRadius = 20.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Player 1 Card
            PlayerScoreSection(
                name = player1Label,
                symbol = player1Symbol,
                score = player1Score,
                isActive = isPlayer1Turn,
                modifier = Modifier.weight(1f)
            )

            // Vertical divider / Draws
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "DRAWS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$drawsScore",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )
            }

            // Player 2 Card
            PlayerScoreSection(
                name = player2Label,
                symbol = player2Symbol,
                score = player2Score,
                isActive = isPlayer2Turn,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PlayerScoreSection(
    name: String,
    symbol: Player,
    score: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val symbolColor = if (symbol == Player.X) NeonPink else NeonCyan
    val borderActiveColor = if (isActive) symbolColor else Color.Transparent

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) symbolColor.copy(alpha = 0.12f) else Color.Transparent)
            .border(1.5.dp, borderActiveColor, RoundedCornerShape(14.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(symbolColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol.name,
                    color = symbolColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$score",
            style = MaterialTheme.typography.headlineMedium,
            color = symbolColor,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp
        )
    }
}

@Composable
fun StatusBanner(
    statusText: String,
    isAiThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isAiThinking) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 6.dp),
                    strokeWidth = 2.5.dp,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = when {
                    statusText.contains("Win", ignoreCase = true) -> NeonGold
                    statusText.contains("Draw", ignoreCase = true) -> Color(0xFF90CAF9)
                    isAiThinking -> NeonCyan
                    else -> MaterialTheme.colorScheme.onSurface
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
