package com.antigravity.tictactoe.ui.screens.singleplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.GlowCard
import com.antigravity.tictactoe.ui.components.PlayerSymbol
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.PrimaryButtonGradient

@Composable
fun SinglePlayerSetupScreen(
    viewModel: SinglePlayerSetupViewModel,
    onStartGame: (GameMode.SinglePlayer) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        text = "Single Player",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Section 1: Choose Your Symbol
                Text(
                    text = "CHOOSE YOUR SYMBOL",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Symbol X Card
                    SymbolSelectionCard(
                        player = Player.X,
                        isSelected = state.selectedSymbol == Player.X,
                        color = NeonPink,
                        onClick = {
                            TicTacToeApp.instance.soundManager.playButtonTap()
                            TicTacToeApp.instance.hapticsManager.vibrateButtonClick()
                            viewModel.selectSymbol(Player.X)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // Symbol O Card
                    SymbolSelectionCard(
                        player = Player.O,
                        isSelected = state.selectedSymbol == Player.O,
                        color = NeonCyan,
                        onClick = {
                            TicTacToeApp.instance.soundManager.playButtonTap()
                            TicTacToeApp.instance.hapticsManager.vibrateButtonClick()
                            viewModel.selectSymbol(Player.O)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Section 2: Choose AI Difficulty
                Text(
                    text = "CHOOSE AI DIFFICULTY",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DifficultyCard(
                        difficulty = GameDifficulty.EASY,
                        description = "Playful & casual moves. Great for beginners.",
                        badgeColor = Color(0xFF4CAF50),
                        isSelected = state.selectedDifficulty == GameDifficulty.EASY,
                        onClick = {
                            TicTacToeApp.instance.soundManager.playButtonTap()
                            TicTacToeApp.instance.hapticsManager.vibrateButtonClick()
                            viewModel.selectDifficulty(GameDifficulty.EASY)
                        }
                    )

                    DifficultyCard(
                        difficulty = GameDifficulty.MEDIUM,
                        description = "Blocks winning lines & seizes tactical opportunities.",
                        badgeColor = NeonGold,
                        isSelected = state.selectedDifficulty == GameDifficulty.MEDIUM,
                        onClick = {
                            TicTacToeApp.instance.soundManager.playButtonTap()
                            TicTacToeApp.instance.hapticsManager.vibrateButtonClick()
                            viewModel.selectDifficulty(GameDifficulty.MEDIUM)
                        }
                    )

                    DifficultyCard(
                        difficulty = GameDifficulty.HARD,
                        description = "Optimal Minimax with Alpha-Beta pruning. Unbeatable!",
                        badgeColor = NeonPink,
                        isSelected = state.selectedDifficulty == GameDifficulty.HARD,
                        onClick = {
                            TicTacToeApp.instance.soundManager.playButtonTap()
                            TicTacToeApp.instance.hapticsManager.vibrateButtonClick()
                            viewModel.selectDifficulty(GameDifficulty.HARD)
                        }
                    )
                }
            }

            // Bottom Action: Start Game
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                GameButton(
                    text = "START MATCH",
                    onClick = {
                        val mode = viewModel.buildGameMode()
                        onStartGame(mode)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    gradient = PrimaryButtonGradient,
                    icon = Icons.Default.PlayArrow
                )
            }
        }
    }
}

@Composable
private fun SymbolSelectionCard(
    player: Player,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(22.dp)
    val backgroundBrush = if (isSelected) {
        Brush.verticalGradient(
            listOf(color.copy(alpha = 0.25f), color.copy(alpha = 0.08f))
        )
    } else {
        Brush.verticalGradient(
            listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush)
            .border(
                width = if (isSelected) 2.5.dp else 1.2.dp,
                color = if (isSelected) color else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                PlayerSymbol(
                    player = player,
                    drawProgress = 1f,
                    isWinning = isSelected,
                    enablePulse = false
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Play as ${player.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (player == Player.X) "Moves First" else "Moves Second",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun DifficultyCard(
    difficulty: GameDifficulty,
    description: String,
    badgeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (isSelected) badgeColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) badgeColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = difficulty.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            // Radio indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSelected) badgeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                }
            }
        }
    }
}
