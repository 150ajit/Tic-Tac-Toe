package com.antigravity.tictactoe.domain.model

data class Board(
    val cells: List<Player?> = List(9) { null }
) {
    init {
        require(cells.size == 9) { "Board must contain exactly 9 cells." }
    }

    operator fun get(index: Int): Player? = cells[index]

    operator fun get(row: Int, col: Int): Player? = cells[row * 3 + col]

    val emptyIndices: List<Int>
        get() = cells.indices.filter { cells[it] == null }

    val isFull: Boolean
        get() = cells.none { it == null }

    fun withMove(index: Int, player: Player): Board {
        require(index in 0..8) { "Cell index out of range: $index" }
        require(cells[index] == null) { "Cell at index $index is already occupied." }
        val newCells = cells.toMutableList()
        newCells[index] = player
        return Board(newCells)
    }

    fun checkWinner(): Pair<Player, WinningCombination>? {
        for (combination in WinningCombination.ALL) {
            val (c1, c2, c3) = combination.indices
            val p = cells[c1]
            if (p != null && p == cells[c2] && p == cells[c3]) {
                return Pair(p, combination)
            }
        }
        return null
    }

    val isTerminal: Boolean
        get() = checkWinner() != null || isFull

    companion object {
        val EMPTY = Board()
    }
}
