package com.antigravity.tictactoe.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.domain.model.DifficultyStats
import com.antigravity.tictactoe.domain.model.Statistics
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.GlowCard
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.statistics.collectAsState()
    val showResetDialog by viewModel.showResetDialog.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Lifetime Statistics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Overall Summary Card
            OverallStatsCard(stats = stats)

            Spacer(modifier = Modifier.height(18.dp))

            // Single Player by Difficulty Card
            SinglePlayerStatsCard(stats = stats)

            Spacer(modifier = Modifier.height(18.dp))

            // Two Players Card
            TwoPlayerStatsCard(stats = stats)

            Spacer(modifier = Modifier.height(26.dp))

            // Reset Statistics Button
            GameButton(
                text = "RESET STATISTICS",
                icon = Icons.Default.DeleteForever,
                onClick = { viewModel.showResetConfirmation() },
                modifier = Modifier.fillMaxWidth(),
                gradient = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    listOf(Color(0xFF8B0000), Color(0xFFC70039))
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            BasicAlertDialog(onDismissRequest = { viewModel.dismissResetConfirmation() }) {
                val shape = RoundedCornerShape(22.dp)
                Column(
                    modifier = Modifier
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.5.dp, NeonPink, shape)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Reset Lifetime Stats?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "This action will clear all your lifetime games, wins, and records. This cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { viewModel.dismissResetConfirmation() }) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { viewModel.resetStatistics() }) {
                            Text("Reset All", color = NeonPink, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OverallStatsCard(stats: Statistics) {
    GlowCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonGold.copy(alpha = 0.35f),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "ALL-TIME RECORD",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatMetric(label = "Matches", value = "${stats.totalGames}")
                StatMetric(label = "Wins", value = "${stats.wins}", valueColor = NeonPink)
                StatMetric(label = "Losses", value = "${stats.losses}", valueColor = NeonCyan)
                StatMetric(label = "Draws", value = "${stats.draws}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Win percentage progress bar
            val winRatePercent = stats.winRate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Win Percentage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "%.1f%%".format(winRatePercent),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = NeonGold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (winRatePercent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NeonGold,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun SinglePlayerStatsCard(stats: Statistics) {
    GlowCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonCyan.copy(alpha = 0.3f),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "SINGLE PLAYER (VS AI)",
                style = MaterialTheme.typography.labelLarge,
                color = NeonCyan,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            DifficultyRow(title = "Easy Mode", diffStats = stats.easyStats, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(10.dp))
            DifficultyRow(title = "Medium Mode", diffStats = stats.mediumStats, color = NeonGold)
            Spacer(modifier = Modifier.height(10.dp))
            DifficultyRow(title = "Hard (Minimax)", diffStats = stats.hardStats, color = NeonPink)
        }
    }
}

@Composable
private fun DifficultyRow(
    title: String,
    diffStats: DifficultyStats,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            .padding(vertical = 10.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = "${diffStats.wins}W • ${diffStats.losses}L • ${diffStats.draws}D",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TwoPlayerStatsCard(stats: Statistics) {
    GlowCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonPink.copy(alpha = 0.3f),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "TWO PLAYERS (LOCAL)",
                style = MaterialTheme.typography.labelLarge,
                color = NeonPink,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatMetric(label = "Matches", value = "${stats.twoPlayerStats.totalGames}")
                StatMetric(label = "X Wins", value = "${stats.twoPlayerStats.xWins}", valueColor = NeonPink)
                StatMetric(label = "O Wins", value = "${stats.twoPlayerStats.oWins}", valueColor = NeonCyan)
                StatMetric(label = "Draws", value = "${stats.twoPlayerStats.draws}")
            }
        }
    }
}

@Composable
private fun StatMetric(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = valueColor,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
