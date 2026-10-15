package com.antigravity.tictactoe.domain.ai

import com.antigravity.tictactoe.domain.game.TicTacToeGameEngine
import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class TicTacToeAiEngineTest {

    private lateinit var aiEngine: TicTacToeAiEngine
    private lateinit var gameEngine: TicTacToeGameEngine

    @Before
    fun setUp() {
        aiEngine = TicTacToeAiEngine(random = Random(42))
        gameEngine = TicTacToeGameEngine()
    }

    @Test
    fun easyAi_returnsLegalMove() {
        val board = Board.EMPTY.withMove(0, Player.X).withMove(4, Player.O)
        val move = aiEngine.getBestMove(board, GameDifficulty.EASY, aiPlayer = Player.O)

        assertNotNull(move)
        assertTrue(move in board.emptyIndices)
        assertNotEquals(0, move)
        assertNotEquals(4, move)
    }

    @Test
    fun mediumAi_takesImmediateWin() {
        // AI is O. O occupies 0, 1. Cell 2 is free.
        val board = Board.EMPTY
            .withMove(0, Player.O)
            .withMove(1, Player.O)
            .withMove(3, Player.X)
            .withMove(4, Player.X)

        val move = aiEngine.getBestMove(board, GameDifficulty.MEDIUM, aiPlayer = Player.O, humanPlayer = Player.X)
        assertEquals(2, move)
    }

    @Test
    fun mediumAi_blocksImmediateLoss() {
        // Human is X and threatens to win at 2 (0, 1 occupied by X)
        val board = Board.EMPTY
            .withMove(0, Player.X)
            .withMove(1, Player.X)
            .withMove(4, Player.O)

        val move = aiEngine.getBestMove(board, GameDifficulty.MEDIUM, aiPlayer = Player.O, humanPlayer = Player.X)
        assertEquals("Medium AI must block opponent's winning cell 2", 2, move)
    }

    @Test
    fun hardAi_takesWinningMove_whenAiIsO() {
        // O has cells 3, 4. Cell 5 is empty.
        val board = Board.EMPTY
            .withMove(3, Player.O)
            .withMove(4, Player.O)
            .withMove(0, Player.X)
            .withMove(6, Player.X)

        val move = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.O, humanPlayer = Player.X)
        assertEquals(5, move)
    }

    @Test
    fun hardAi_takesWinningMove_whenAiIsX() {
        // AI is X. X has cells 0, 4. Cell 8 is empty.
        val board = Board.EMPTY
            .withMove(0, Player.X)
            .withMove(4, Player.X)
            .withMove(1, Player.O)
            .withMove(2, Player.O)

        val move = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.X, humanPlayer = Player.O)
        assertEquals(8, move)
    }

    @Test
    fun hardAi_blocksForcedImmediateLoss() {
        // Opponent is X on 0 and 3. AI is O. Col 0 winning threat at 6.
        val board = Board.EMPTY
            .withMove(0, Player.X)
            .withMove(3, Player.X)
            .withMove(4, Player.O)

        val move = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.O, humanPlayer = Player.X)
        assertEquals(6, move)
    }

    @Test
    fun hardAi_neverSelectsOccupiedCell() {
        var board = Board.EMPTY
        for (i in 0..7) {
            val player = if (i % 2 == 0) Player.X else Player.O
            board = board.withMove(i, player)
        }
        // Only index 8 is left
        val move = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.X, humanPlayer = Player.O)
        assertEquals(8, move)
    }

    @Test
    fun hardAi_cannotBeBeatenInOptimalPlayScenarios() {
        // Simulate games where Hard AI plays against an optimal opponent.
        // The result MUST always be a DRAW or an AI WIN; AI must NEVER LOSE!
        val testOpenings = listOf(0, 2, 4, 6, 8, 1, 3, 5, 7)

        for (opening in testOpenings) {
            var board = Board.EMPTY.withMove(opening, Player.X) // Human opening
            var currentPlayer = Player.O // AI is O

            while (!board.isTerminal) {
                if (currentPlayer == Player.O) {
                    val aiMove = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.O, humanPlayer = Player.X)
                    assertNotNull("AI must find a move", aiMove)
                    board = board.withMove(aiMove!!, Player.O)
                    currentPlayer = Player.X
                } else {
                    // Minimax human move
                    val humanMove = aiEngine.getHardMove(board, aiPlayer = Player.X, humanPlayer = Player.O)
                    if (humanMove != null) {
                        board = board.withMove(humanMove, Player.X)
                    }
                    currentPlayer = Player.O
                }
            }

            val winner = board.checkWinner()?.first
            assertNotEquals("Hard AI playing as O must NEVER lose against any opening!", Player.X, winner)
        }
    }

    @Test
    fun hardAi_firstMove_returnsValidStrategicCell() {
        val board = Board.EMPTY
        val move = aiEngine.getBestMove(board, GameDifficulty.HARD, aiPlayer = Player.X, humanPlayer = Player.O)
        assertNotNull(move)
        val validFirstMoves = listOf(4, 0, 2, 6, 8)
        assertTrue("Hard AI should take center or corner on first move", move in validFirstMoves)
    }
}
