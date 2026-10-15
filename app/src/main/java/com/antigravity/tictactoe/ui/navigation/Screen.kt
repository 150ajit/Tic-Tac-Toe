package com.antigravity.tictactoe.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object SinglePlayerSetup : Screen("single_player_setup")
    data object TwoPlayerSetup : Screen("two_player_setup")
    data object Game : Screen("game")
    data object Statistics : Screen("statistics")
    data object Settings : Screen("settings")
    data object HowToPlay : Screen("how_to_play")
}
