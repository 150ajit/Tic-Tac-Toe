package com.antigravity.tictactoe.domain.model

enum class Player {
    X,
    O;

    val opponent: Player
        get() = if (this == X) O else X
}
