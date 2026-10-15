package com.antigravity.tictactoe.domain.model

data class DifficultyStats(
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
) {
    val totalGames: Int get() = wins + losses + draws
    val winRate: Float get() = if (totalGames > 0) (wins.toFloat() / totalGames) * 100f else 0f
}

data class TwoPlayerStats(
    val totalGames: Int = 0,
    val xWins: Int = 0,
    val oWins: Int = 0,
    val draws: Int = 0
)

data class Statistics(
    val totalGames: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val easyStats: DifficultyStats = DifficultyStats(),
    val mediumStats: DifficultyStats = DifficultyStats(),
    val hardStats: DifficultyStats = DifficultyStats(),
    val twoPlayerStats: TwoPlayerStats = TwoPlayerStats()
) {
    val winRate: Float
        get() = if (totalGames > 0) (wins.toFloat() / totalGames) * 100f else 0f
}
