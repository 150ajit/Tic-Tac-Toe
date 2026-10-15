package com.antigravity.tictactoe.domain.model

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD;

    val displayName: String
        get() = when (this) {
            EASY -> "Easy"
            MEDIUM -> "Medium"
            HARD -> "Hard"
        }
}
