package com.antigravity.tictactoe.domain.ai

import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.Player
import kotlin.random.Random

class TicTacToeAiEngine(
    private val random: Random = Random.Default
) {

    /**
     * Determines the best cell index (0..8) for the AI to play.
     * Returns null if no moves are available.
     */
    fun getBestMove(
        board: Board,
        difficulty: GameDifficulty,
        aiPlayer: Player,
        humanPlayer: Player = aiPlayer.opponent
    ): Int? {
        val availableMoves = board.emptyIndices
        if (availableMoves.isEmpty()) return null

        return when (difficulty) {
            GameDifficulty.EASY -> getEasyMove(availableMoves)
            GameDifficulty.MEDIUM -> getMediumMove(board, aiPlayer, humanPlayer, availableMoves)
            GameDifficulty.HARD -> getHardMove(board, aiPlayer, humanPlayer)
        }
    }

    /**
     * Easy: purely random legal move.
     */
    private fun getEasyMove(availableMoves: List<Int>): Int {
        return availableMoves.random(random)
    }

    /**
     * Medium:
     * 1. Check if AI can win immediately -> Take it!
     * 2. Check if Human can win immediately -> Block it!
     * 3. 70% of the time, choose strategic cells (center then corners).
     * 4. Otherwise, pick a random legal move.
     */
    private fun getMediumMove(
        board: Board,
        aiPlayer: Player,
        humanPlayer: Player,
        availableMoves: List<Int>
    ): Int {
        // 1. Immediate winning move
        for (move in availableMoves) {
            val nextBoard = board.withMove(move, aiPlayer)
            if (nextBoard.checkWinner()?.first == aiPlayer) {
                return move
            }
        }

        // 2. Block opponent's immediate winning move
        for (move in availableMoves) {
            val nextBoard = board.withMove(move, humanPlayer)
            if (nextBoard.checkWinner()?.first == humanPlayer) {
                return move
            }
        }

        // 3. Strategic heuristic with 70% probability
        if (random.nextFloat() < 0.7f) {
            // Center cell (4)
            if (4 in availableMoves) return 4

            // Corners (0, 2, 6, 8)
            val corners = listOf(0, 2, 6, 8).filter { it in availableMoves }
            if (corners.isNotEmpty()) {
                return corners.random(random)
            }
        }

        // 4. Random available move
        return availableMoves.random(random)
    }

    /**
     * Hard:
     * Minimax algorithm with Alpha-Beta Pruning.
     * Guaranteed optimal play; unbeatable in standard Tic Tac Toe.
     */
    fun getHardMove(
        board: Board,
        aiPlayer: Player,
        humanPlayer: Player
    ): Int? {
        val availableMoves = board.emptyIndices
        if (availableMoves.isEmpty()) return null

        // Optimization: if it's the very first move on an empty board, take center or a corner for variety
        if (availableMoves.size == 9) {
            val preferredFirstMoves = listOf(4, 0, 2, 6, 8)
            return preferredFirstMoves.random(random)
        }

        var bestScore = Int.MIN_VALUE
        var bestMove = availableMoves.first()

        for (move in availableMoves) {
            val nextBoard = board.withMove(move, aiPlayer)
            val score = minimax(
                board = nextBoard,
                depth = 0,
                isMaximizing = false,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE,
                aiPlayer = aiPlayer,
                humanPlayer = humanPlayer
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    private fun minimax(
        board: Board,
        depth: Int,
        isMaximizing: Boolean,
        alpha: Int,
        beta: Int,
        aiPlayer: Player,
        humanPlayer: Player
    ): Int {
        val winnerPair = board.checkWinner()
        if (winnerPair != null) {
            return if (winnerPair.first == aiPlayer) {
                10 - depth // Prefer faster wins
            } else {
                depth - 10 // Prefer delaying losses
            }
        }

        if (board.isFull) {
            return 0 // Draw
        }

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) {
            var maxScore = Int.MIN_VALUE
            for (move in board.emptyIndices) {
                val nextBoard = board.withMove(move, aiPlayer)
                val score = minimax(
                    board = nextBoard,
                    depth = depth + 1,
                    isMaximizing = false,
                    alpha = currentAlpha,
                    beta = currentBeta,
                    aiPlayer = aiPlayer,
                    humanPlayer = humanPlayer
                )
                maxScore = maxOf(maxScore, score)
                currentAlpha = maxOf(currentAlpha, score)
                if (currentBeta <= currentAlpha) {
                    break // Alpha-beta cutoff
                }
            }
            return maxScore
        } else {
            var minScore = Int.MAX_VALUE
            for (move in board.emptyIndices) {
                val nextBoard = board.withMove(move, humanPlayer)
                val score = minimax(
                    board = nextBoard,
                    depth = depth + 1,
                    isMaximizing = true,
                    alpha = currentAlpha,
                    beta = currentBeta,
                    aiPlayer = aiPlayer,
                    humanPlayer = humanPlayer
                )
                minScore = minOf(minScore, score)
                currentBeta = minOf(currentBeta, score)
                if (currentBeta <= currentAlpha) {
                    break // Alpha-beta cutoff
                }
            }
            return minScore
        }
    }
}
