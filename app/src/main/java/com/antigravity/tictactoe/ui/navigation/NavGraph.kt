package com.antigravity.tictactoe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.antigravity.tictactoe.ui.screens.game.GameScreen
import com.antigravity.tictactoe.ui.screens.game.GameViewModel
import com.antigravity.tictactoe.ui.screens.home.HomeScreen
import com.antigravity.tictactoe.ui.screens.howtoplay.HowToPlayScreen
import com.antigravity.tictactoe.ui.screens.multiplayer.TwoPlayerSetupScreen
import com.antigravity.tictactoe.ui.screens.multiplayer.TwoPlayerSetupViewModel
import com.antigravity.tictactoe.ui.screens.settings.SettingsScreen
import com.antigravity.tictactoe.ui.screens.settings.SettingsViewModel
import com.antigravity.tictactoe.ui.screens.singleplayer.SinglePlayerSetupScreen
import com.antigravity.tictactoe.ui.screens.singleplayer.SinglePlayerSetupViewModel
import com.antigravity.tictactoe.ui.screens.statistics.StatisticsScreen
import com.antigravity.tictactoe.ui.screens.statistics.StatisticsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(320)
            ) + fadeIn(animationSpec = tween(320))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(320)
            ) + fadeOut(animationSpec = tween(320))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(320)
            ) + fadeIn(animationSpec = tween(320))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(320)
            ) + fadeOut(animationSpec = tween(320))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateSinglePlayer = { navController.navigate(Screen.SinglePlayerSetup.route) },
                onNavigateTwoPlayers = { navController.navigate(Screen.TwoPlayerSetup.route) },
                onNavigateStatistics = { navController.navigate(Screen.Statistics.route) },
                onNavigateSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateHowToPlay = { navController.navigate(Screen.HowToPlay.route) }
            )
        }

        composable(Screen.SinglePlayerSetup.route) {
            val setupViewModel: SinglePlayerSetupViewModel = viewModel()
            SinglePlayerSetupScreen(
                viewModel = setupViewModel,
                onStartGame = { mode ->
                    gameViewModel.initializeGame(mode)
                    navController.navigate(Screen.Game.route)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TwoPlayerSetup.route) {
            val setupViewModel: TwoPlayerSetupViewModel = viewModel()
            TwoPlayerSetupScreen(
                viewModel = setupViewModel,
                onStartGame = { mode ->
                    gameViewModel.initializeGame(mode)
                    navController.navigate(Screen.Game.route)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                viewModel = gameViewModel,
                onNavigateBack = { navController.popBackStack(Screen.Home.route, inclusive = false) }
            )
        }

        composable(Screen.Statistics.route) {
            val statsViewModel: StatisticsViewModel = viewModel()
            StatisticsScreen(
                viewModel = statsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.HowToPlay.route) {
            HowToPlayScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
