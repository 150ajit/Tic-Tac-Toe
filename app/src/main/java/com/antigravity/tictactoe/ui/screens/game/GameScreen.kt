package com.antigravity.tictactoe.ui.screens.game

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.components.BoardCanvas
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.ScoreBoard
import com.antigravity.tictactoe.ui.components.StatusBanner
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonPink
import androidx.compose.foundation.layout.navigationBarsPadding
import com.antigravity.tictactoe.ui.components.BannerAdView

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.appSettings.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? android.app.Activity

    val statusText = when (val status = uiState.gameStatus) {
        is GameStatus.Ongoing -> {
            if (uiState.isAiThinking) {
                "AI is Thinking…"
            } else {
                when (val mode = uiState.gameMode) {
                    is GameMode.SinglePlayer -> {
                        if (uiState.currentPlayer == mode.humanSymbol) "Your Turn" else "AI\'s Turn"
                    }
                    is GameMode.TwoPlayers -> {
                        val name = if (uiState.currentPlayer == Player.X) mode.player1Name else mode.player2Name
                        "$name\'s Turn"
                    }
                }
            }
        }
        is GameStatus.Won -> {
            when (val mode = uiState.gameMode) {
                is GameMode.SinglePlayer -> {
                    if (status.winner == mode.humanSymbol) "You Win! 🎉" else "AI Wins!"
                }
                is GameMode.TwoPlayers -> {
                    val name = if (status.winner == Player.X) mode.player1Name else mode.player2Name
                    "$name Wins! 🎉"
                }
            }
        }
        is GameStatus.Draw -> "It\'s a Draw!"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BannerAdView(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top App Bar Area
            GameTopBar(
                gameMode = uiState.gameMode,
                isSoundEnabled = settings.soundEnabled,
                onBackClick = onNavigateBack,
                onToggleSound = { viewModel.toggleSound() },
                onRestartClick = { viewModel.newGame() }
            )

            // Scoreboard Area
            ScoreBoard(
                player1Label = uiState.player1Name,
                player1Symbol = Player.X,
                player1Score = uiState.sessionScore.player1Score,
                isPlayer1Turn = uiState.currentPlayer == Player.X && !uiState.gameStatus.isFinished,
                player2Label = uiState.player2Name,
                player2Symbol = Player.O,
                player2Score = uiState.sessionScore.player2Score,
                isPlayer2Turn = uiState.currentPlayer == Player.O && !uiState.gameStatus.isFinished,
                drawsScore = uiState.sessionScore.draws
            )

            // Turn & AI status banner
            StatusBanner(
                statusText = statusText,
                isAiThinking = uiState.isAiThinking
            )

            // Interactive 3x3 Board Canvas
            BoardCanvas(
                board = uiState.board,
                winningCombination = uiState.winningCombination,
                isInteractive = uiState.isInteractive,
                animationsEnabled = settings.animationsEnabled,
                onCellClick = { index -> viewModel.onCellClicked(index) },
                modifier = Modifier.weight(1f, fill = false)
            )

            // Bottom Quick Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                GameButton(
                    text = "RESTART MATCH",
                    onClick = { viewModel.newGame() },
                    modifier = Modifier.fillMaxWidth(0.75f),
                    icon = Icons.Default.Refresh,
                    gradient = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        listOf(Color(0xFF232A4F), Color(0xFF1B203D))
                    ),
                    borderColor = NeonCyan.copy(alpha = 0.35f)
                )
            }
        }

        // Result Dialog Overlay
        if (uiState.showResultDialog) {
            val title = when (val s = uiState.gameStatus) {
                is GameStatus.Won -> {
                    when (val mode = uiState.gameMode) {
                        is GameMode.SinglePlayer -> if (s.winner == mode.humanSymbol) "VICTORY!" else "GAME OVER"
                        is GameMode.TwoPlayers -> {
                            val winnerName = if (s.winner == Player.X) mode.player1Name else mode.player2Name
                            "$winnerName WINS!"
                        }
                    }
                }
                is GameStatus.Draw -> "DRAW!"
                else -> ""
            }

            val subtitle = when (val s = uiState.gameStatus) {
                is GameStatus.Won -> {
                    when (val mode = uiState.gameMode) {
                        is GameMode.SinglePlayer -> if (s.winner == mode.humanSymbol) "You outplayed the AI!" else "The AI claimed this round."
                        is GameMode.TwoPlayers -> "A well-fought battle!"
                    }
                }
                is GameStatus.Draw -> "No winner this time. Equal skills!"
                else -> ""
            }

            ResultDialog(
                status = uiState.gameStatus,
                title = title,
                subtitle = subtitle,
                winningPlayer = uiState.winner,
                animationsEnabled = settings.animationsEnabled,
                onPlayAgain = {
                    com.antigravity.tictactoe.TicTacToeApp.instance.interstitialAdManager.onMatchFinishedAction(activity) {
                        viewModel.playAgain()
                    }
                },
                onNewGame = {
                    com.antigravity.tictactoe.TicTacToeApp.instance.interstitialAdManager.onMatchFinishedAction(activity) {
                        viewModel.newGame()
                    }
                },
                onHome = {
                    com.antigravity.tictactoe.TicTacToeApp.instance.interstitialAdManager.onMatchFinishedAction(activity) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}

@Composable
private fun GameTopBar(
    gameMode: GameMode,
    isSoundEnabled: Boolean,
    onBackClick: () -> Unit,
    onToggleSound: () -> Unit,
    onRestartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
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

        // Game Mode Badge
        val badgeText = when (gameMode) {
            is GameMode.SinglePlayer -> "AI • ${gameMode.difficulty.displayName.uppercase()}"
            is GameMode.TwoPlayers -> "2 PLAYERS LOCAL"
        }
        val badgeColor = when (gameMode) {
            is GameMode.SinglePlayer -> NeonCyan
            is GameMode.TwoPlayers -> NeonPink
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(badgeColor.copy(alpha = 0.15f))
                .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = badgeText,
                color = badgeColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.8.sp
            )
        }

        // Actions: Sound & Reset
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onToggleSound,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = if (isSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                    contentDescription = "Toggle Sound",
                    tint = if (isSoundEnabled) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
