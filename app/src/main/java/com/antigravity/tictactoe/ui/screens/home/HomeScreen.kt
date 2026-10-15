package com.antigravity.tictactoe.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.ui.components.GameButton
import com.antigravity.tictactoe.ui.components.PlayerSymbol
import com.antigravity.tictactoe.ui.theme.NeonCyan
import com.antigravity.tictactoe.ui.theme.NeonGold
import com.antigravity.tictactoe.ui.theme.NeonPink
import com.antigravity.tictactoe.ui.theme.PrimaryButtonGradient
import com.antigravity.tictactoe.ui.theme.SecondaryButtonGradient
import androidx.compose.foundation.layout.navigationBarsPadding
import com.antigravity.tictactoe.ui.components.BannerAdView

@Composable
fun HomeScreen(
    onNavigateSinglePlayer: () -> Unit,
    onNavigateTwoPlayers: () -> Unit,
    onNavigateStatistics: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateHowToPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appSettings by TicTacToeApp.instance.preferencesRepository.appSettingsFlow
        .collectAsState(initial = com.antigravity.tictactoe.domain.model.AppSettings())

    val logoScale = remember { Animatable(0.75f) }
    LaunchedEffect(Unit) {
        if (appSettings.animationsEnabled) {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        } else {
            logoScale.snapTo(1f)
        }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Floating Neon Background Effect
            HomeBackgroundEffect(
                modifier = Modifier.fillMaxSize(),
                animationsEnabled = appSettings.animationsEnabled
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // App Branding & Animated Logo
                Column(
                    modifier = Modifier.scale(logoScale.value),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Central X and O Brand Icon
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NeonPink.copy(alpha = 0.25f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(54.dp)) {
                                PlayerSymbol(player = Player.X, drawProgress = 1f, enablePulse = false)
                            }
                            Box(modifier = Modifier.size(54.dp)) {
                                PlayerSymbol(player = Player.O, drawProgress = 1f, enablePulse = false)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "TIC TAC TOE",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonPink.copy(alpha = 0.2f), NeonCyan.copy(alpha = 0.2f))
                                )
                            )
                            .border(
                                1.dp,
                                Brush.horizontalGradient(listOf(NeonPink, NeonCyan)),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "X O X O",
                            color = NeonGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            letterSpacing = 4.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Primary Actions: Single Player & Two Players
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GameButton(
                        text = "SINGLE PLAYER",
                        subtitle = "Challenge Smart AI Engine",
                        icon = Icons.Default.Person,
                        onClick = onNavigateSinglePlayer,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = PrimaryButtonGradient
                    )

                    GameButton(
                        text = "TWO PLAYERS",
                        subtitle = "Pass & Play on Same Device",
                        icon = Icons.Default.People,
                        onClick = onNavigateTwoPlayers,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = SecondaryButtonGradient
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Secondary Actions: Statistics, Settings, How to Play
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GameButton(
                            text = "STATS",
                            icon = Icons.Default.BarChart,
                            onClick = onNavigateStatistics,
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(
                                listOf(Color(0xFF232A4F), Color(0xFF1B203D))
                            ),
                            borderColor = NeonCyan.copy(alpha = 0.4f)
                        )

                        GameButton(
                            text = "SETTINGS",
                            icon = Icons.Default.Settings,
                            onClick = onNavigateSettings,
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(
                                listOf(Color(0xFF232A4F), Color(0xFF1B203D))
                            ),
                            borderColor = NeonPink.copy(alpha = 0.4f)
                        )
                    }

                    GameButton(
                        text = "HOW TO PLAY",
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        onClick = onNavigateHowToPlay,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFF1E233D), Color(0xFF161A2E))
                        ),
                        borderColor = Color.White.copy(alpha = 0.2f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
