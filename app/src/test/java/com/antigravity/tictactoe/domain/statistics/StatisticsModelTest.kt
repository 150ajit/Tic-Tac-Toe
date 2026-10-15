package com.antigravity.tictactoe.domain.statistics

import com.antigravity.tictactoe.domain.model.DifficultyStats
import com.antigravity.tictactoe.domain.model.Statistics
import com.antigravity.tictactoe.domain.model.TwoPlayerStats
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsModelTest {

    @Test
    fun defaultStatistics_hasZeroMatchesAndZeroWinRate() {
        val stats = Statistics()
        assertEquals(0, stats.totalGames)
        assertEquals(0f, stats.winRate, 0.001f)
    }

    @Test
    fun statisticsWinRate_calculatedCorrectly() {
        val stats = Statistics(
            totalGames = 10,
            wins = 7,
            losses = 2,
            draws = 1
        )
        assertEquals(70.0f, stats.winRate, 0.001f)
    }

    @Test
    fun difficultyStats_calculatesTotalAndWinRate() {
        val diffStats = DifficultyStats(
            wins = 4,
            losses = 1,
            draws = 0
        )
        assertEquals(5, diffStats.totalGames)
        assertEquals(80.0f, diffStats.winRate, 0.001f)
    }

    @Test
    fun twoPlayerStats_tracksMatchesCorrectly() {
        val twoPlayer = TwoPlayerStats(
            totalGames = 8,
            xWins = 5,
            oWins = 2,
            draws = 1
        )
        assertEquals(8, twoPlayer.totalGames)
        assertEquals(5, twoPlayer.xWins)
        assertEquals(2, twoPlayer.oWins)
        assertEquals(1, twoPlayer.draws)
    }
}
