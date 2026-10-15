package com.antigravity.tictactoe.domain.game

import com.antigravity.tictactoe.domain.model.Board
import com.antigravity.tictactoe.domain.model.GameStatus
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.domain.model.WinningCombination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TicTacToeGameEngineTest {

    private lateinit var gameEngine: TicTacToeGameEngine

    @Before
    fun setUp() {
        gameEngine = TicTacToeGameEngine()
    }

    @Test
    fun makeMove_validMove_updatesBoardAndStatus() {
        val board = Board.EMPTY
        val result = gameEngine.makeMove(board, 0, Player.X)

        assertNotNull(result)
        val (newBoard, status) = result!!
        assertEquals(Player.X, newBoard[0])
        assertEquals(GameStatus.Ongoing, status)
    }

    @Test
    fun makeMove_occupiedCell_returnsNull() {
        val board = Board.EMPTY.withMove(4, Player.X)
        val result = gameEngine.makeMove(board, 4, Player.O)

        assertNull("Moving into an occupied cell must be rejected", result)
    }

    @Test
    fun makeMove_outOfBounds_returnsNull() {
        val board = Board.EMPTY
        assertNull(gameEngine.makeMove(board, -1, Player.X))
        assertNull(gameEngine.makeMove(board, 9, Player.X))
    }

    @Test
    fun checkHorizontalWin_row0_detectedCorrectly() {
        var board = Board.EMPTY
        board = board.withMove(0, Player.X)
        board = board.withMove(3, Player.O)
        board = board.withMove(1, Player.X)
        board = board.withMove(4, Player.O)
        val result = gameEngine.makeMove(board, 2, Player.X)

        assertNotNull(result)
        val (_, status) = result!!
        assertTrue(status is GameStatus.Won)
        val won = status as GameStatus.Won
        assertEquals(Player.X, won.winner)
        assertEquals(WinningCombination.ROW_0, won.combination)
    }

    @Test
    fun checkHorizontalWin_row1_and_row2() {
        // Row 1
        var b1 = Board.EMPTY.withMove(3, Player.O).withMove(4, Player.O)
        val res1 = gameEngine.makeMove(b1, 5, Player.O)
        assertEquals(WinningCombination.ROW_1, (res1?.second as? GameStatus.Won)?.combination)

        // Row 2
        var b2 = Board.EMPTY.withMove(6, Player.X).withMove(7, Player.X)
        val res2 = gameEngine.makeMove(b2, 8, Player.X)
        assertEquals(WinningCombination.ROW_2, (res2?.second as? GameStatus.Won)?.combination)
    }

    @Test
    fun checkVerticalWin_detectedCorrectly() {
        // Col 1: indices 1, 4, 7
        var board = Board.EMPTY
            .withMove(1, Player.O)
            .withMove(0, Player.X)
            .withMove(4, Player.O)
            .withMove(2, Player.X)

        val result = gameEngine.makeMove(board, 7, Player.O)
        assertNotNull(result)
        val (_, status) = result!!
        assertTrue(status is GameStatus.Won)
        val won = status as GameStatus.Won
        assertEquals(Player.O, won.winner)
        assertEquals(WinningCombination.COL_1, won.combination)
    }

    @Test
    fun checkMainDiagonalWin_detectedCorrectly() {
        // Main diagonal: 0, 4, 8
        var board = Board.EMPTY
            .withMove(0, Player.X)
            .withMove(1, Player.O)
            .withMove(4, Player.X)
            .withMove(2, Player.O)

        val result = gameEngine.makeMove(board, 8, Player.X)
        assertNotNull(result)
        val won = result!!.second as GameStatus.Won
        assertEquals(Player.X, won.winner)
        assertEquals(WinningCombination.MAIN_DIAGONAL, won.combination)
    }

    @Test
    fun checkAntiDiagonalWin_detectedCorrectly() {
        // Anti diagonal: 2, 4, 6
        var board = Board.EMPTY
            .withMove(2, Player.O)
            .withMove(0, Player.X)
            .withMove(4, Player.O)
            .withMove(1, Player.X)

        val result = gameEngine.makeMove(board, 6, Player.O)
        assertNotNull(result)
        val won = result!!.second as GameStatus.Won
        assertEquals(Player.O, won.winner)
        assertEquals(WinningCombination.ANTI_DIAGONAL, won.combination)
    }

    @Test
    fun checkDraw_detectedWhenBoardFullWithoutWinner() {
        // Board layout for draw:
        // X O X
        // X X O
        // O X O
        val b = Board.EMPTY
            .withMove(0, Player.X)
            .withMove(1, Player.O)
            .withMove(2, Player.X)
            .withMove(3, Player.X)
            .withMove(5, Player.O)
            .withMove(6, Player.O)
            .withMove(7, Player.X)
            .withMove(8, Player.O)

        val result = gameEngine.makeMove(b, 4, Player.X)
        assertNotNull(result)
        assertEquals(GameStatus.Draw, result!!.second)
    }
}
