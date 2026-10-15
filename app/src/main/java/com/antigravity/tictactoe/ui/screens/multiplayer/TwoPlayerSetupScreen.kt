package com.antigravity.tictactoe.ui.screens.multiplayer

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.GlowCard
import com.antigravity.tictactoe.ui.components.PlayerSymbol
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.PrimaryButtonGradient

@Composable
fun TwoPlayerSetupScreen(
    viewModel: TwoPlayerSetupViewModel,
    onStartGame: (GameMode.TwoPlayers) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

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
                        .padding(top = 12.dp, bottom = 24.dp),
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
                        text = "Two Players",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "LOCAL PASS & PLAY",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Player 1 Name Input Card
                PlayerNameCard(
                    symbol = Player.X,
                    symbolColor = NeonPink,
                    label = "Player 1 (Plays X)",
                    name = state.player1Name,
                    onNameChange = { viewModel.updatePlayer1Name(it) },
                    imeAction = ImeAction.Next,
                    onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Player 2 Name Input Card
                PlayerNameCard(
                    symbol = Player.O,
                    symbolColor = NeonCyan,
                    label = "Player 2 (Plays O)",
                    name = state.player2Name,
                    onNameChange = { viewModel.updatePlayer2Name(it) },
                    imeAction = ImeAction.Done,
                    onImeAction = { focusManager.clearFocus() }
                )
            }

            // Bottom Start Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                GameButton(
                    text = "START MATCH",
                    onClick = {
                        focusManager.clearFocus()
                        onStartGame(viewModel.buildGameMode())
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
private fun PlayerNameCard(
    symbol: Player,
    symbolColor: Color,
    label: String,
    name: String,
    onNameChange: (String) -> Unit,
    imeAction: ImeAction,
    onImeAction: () -> Unit
) {
    GlowCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = symbolColor.copy(alpha = 0.4f),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(symbolColor.copy(alpha = 0.15f))
                        .border(1.5.dp, symbolColor.copy(alpha = 0.4f), CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PlayerSymbol(player = symbol, isWinning = false, enablePulse = false)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (symbol == Player.X) "Moves First" else "Moves Second",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                maxLines = 1,
                placeholder = { Text(if (symbol == Player.X) "Player 1" else "Player 2") },
                supportingText = {
                    Text(
                        text = "${name.length}/15",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(onNext = { onImeAction() }, onDone = { onImeAction() }),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = symbolColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
