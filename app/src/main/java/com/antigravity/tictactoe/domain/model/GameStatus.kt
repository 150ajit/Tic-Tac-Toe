package com.antigravity.tictactoe.domain.model

sealed interface GameStatus {
    data object Ongoing : GameStatus

    data class Won(
        val winner: Player,
        val combination: WinningCombination
    ) : GameStatus

    data object Draw : GameStatus

    val isFinished: Boolean
        get() = this !is Ongoing
}
