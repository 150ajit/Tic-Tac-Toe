package com.antigravity.tictactoe.domain.model

sealed interface GameMode {
    data class SinglePlayer(
        val humanSymbol: Player = Player.X,
        val difficulty: GameDifficulty = GameDifficulty.HARD
    ) : GameMode {
        val aiSymbol: Player get() = humanSymbol.opponent
    }

    data class TwoPlayers(
        val player1Name: String = "Player 1",
        val player2Name: String = "Player 2"
    ) : GameMode
}
