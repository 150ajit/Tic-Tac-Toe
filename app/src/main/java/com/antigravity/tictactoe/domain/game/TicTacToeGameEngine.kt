package com.antigravity.tictactoe.domain.game

import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player

class TicTacToeGameEngine {

    /**
     * Attempts to place a mark at [cellIndex] for [player].
     * Returns the updated [Board] and new [GameStatus] if valid, or null if the move is illegal.
     */
    fun makeMove(
        board: Board,
        cellIndex: Int,
        player: Player
    ): Pair<Board, GameStatus>? {
        if (cellIndex !in 0..8) return null
        if (board[cellIndex] != null) return null

        val updatedBoard = board.withMove(cellIndex, player)
        val winnerPair = updatedBoard.checkWinner()

        val newStatus = when {
            winnerPair != null -> GameStatus.Won(
                winner = winnerPair.first,
                combination = winnerPair.second
            )
            updatedBoard.isFull -> GameStatus.Draw
            else -> GameStatus.Ongoing
        }

        return Pair(updatedBoard, newStatus)
    }

    /**
     * Determines whether the game has reached a terminal state.
     */
    fun isGameOver(status: GameStatus): Boolean = status.isFinished
}
