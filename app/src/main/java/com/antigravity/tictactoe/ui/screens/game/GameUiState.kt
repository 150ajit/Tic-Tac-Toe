package com.antigravity.tictactoe.ui.screens.game

import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.domain.model.WinningCombination

data class SessionScore(
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val draws: Int = 0
)

data class GameUiState(
    val board: Board = Board.EMPTY,
    val currentPlayer: Player = Player.X,
    val gameMode: GameMode = GameMode.SinglePlayer(),
    val gameStatus: GameStatus = GameStatus.Ongoing,
    val sessionScore: SessionScore = SessionScore(),
    val isAiThinking: Boolean = false,
    val showResultDialog: Boolean = false,
    val isInteractive: Boolean = true
) {
    val winningCombination: WinningCombination?
        get() = (gameStatus as? GameStatus.Won)?.combination

    val winner: Player?
        get() = (gameStatus as? GameStatus.Won)?.winner

    val humanSymbol: Player?
        get() = (gameMode as? GameMode.SinglePlayer)?.humanSymbol

    val aiSymbol: Player?
        get() = (gameMode as? GameMode.SinglePlayer)?.aiSymbol

    val difficulty: GameDifficulty?
        get() = (gameMode as? GameMode.SinglePlayer)?.difficulty

    val player1Name: String
        get() = when (gameMode) {
            is GameMode.SinglePlayer -> if (humanSymbol == Player.X) "YOU" else "AI"
            is GameMode.TwoPlayers -> gameMode.player1Name
        }

    val player2Name: String
        get() = when (gameMode) {
            is GameMode.SinglePlayer -> if (humanSymbol == Player.O) "YOU" else "AI"
            is GameMode.TwoPlayers -> gameMode.player2Name
        }
}
