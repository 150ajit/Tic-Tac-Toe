package com.antigravity.tictactoe.domain.model

enum class WinningCombination(
    val indices: List<Int>,
    val startRow: Int,
    val startCol: Int,
    val endRow: Int,
    val endCol: Int
) {
    ROW_0(listOf(0, 1, 2), startRow = 0, startCol = 0, endRow = 0, endCol = 2),
    ROW_1(listOf(3, 4, 5), startRow = 1, startCol = 0, endRow = 1, endCol = 2),
    ROW_2(listOf(6, 7, 8), startRow = 2, startCol = 0, endRow = 2, endCol = 2),

    COL_0(listOf(0, 3, 6), startRow = 0, startCol = 0, endRow = 2, endCol = 0),
    COL_1(listOf(1, 4, 7), startRow = 0, startCol = 1, endRow = 2, endCol = 1),
    COL_2(listOf(2, 5, 8), startRow = 0, startCol = 2, endRow = 2, endCol = 2),

    MAIN_DIAGONAL(listOf(0, 4, 8), startRow = 0, startCol = 0, endRow = 2, endCol = 2),
    ANTI_DIAGONAL(listOf(2, 4, 6), startRow = 0, startCol = 2, endRow = 2, endCol = 0);

    companion object {
        val ALL: List<WinningCombination> = entries
    }
}
