package com.antigravity.tictactoe.data.statistics

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.antigravity.tictactoe.domain.model.DifficultyStats
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.Player
import com.antigravity.tictactoe.domain.model.Statistics
import com.antigravity.tictactoe.domain.model.TwoPlayerStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_statistics")

class StatisticsRepository(private val context: Context) {

    private object Keys {
        val TOTAL_GAMES = intPreferencesKey("total_games")
        val WINS = intPreferencesKey("wins")
        val LOSSES = intPreferencesKey("losses")
        val DRAWS = intPreferencesKey("draws")

        val EASY_WINS = intPreferencesKey("easy_wins")
        val EASY_LOSSES = intPreferencesKey("easy_losses")
        val EASY_DRAWS = intPreferencesKey("easy_draws")

        val MEDIUM_WINS = intPreferencesKey("medium_wins")
        val MEDIUM_LOSSES = intPreferencesKey("medium_losses")
        val MEDIUM_DRAWS = intPreferencesKey("medium_draws")

        val HARD_WINS = intPreferencesKey("hard_wins")
        val HARD_LOSSES = intPreferencesKey("hard_losses")
        val HARD_DRAWS = intPreferencesKey("hard_draws")

        val TWO_PLAYER_GAMES = intPreferencesKey("two_player_games")
        val TWO_PLAYER_X_WINS = intPreferencesKey("two_player_x_wins")
        val TWO_PLAYER_O_WINS = intPreferencesKey("two_player_o_wins")
        val TWO_PLAYER_DRAWS = intPreferencesKey("two_player_draws")
    }

    val statisticsFlow: Flow<Statistics> = context.statsDataStore.data.map { p ->
        Statistics(
            totalGames = p[Keys.TOTAL_GAMES] ?: 0,
            wins = p[Keys.WINS] ?: 0,
            losses = p[Keys.LOSSES] ?: 0,
            draws = p[Keys.DRAWS] ?: 0,
            easyStats = DifficultyStats(
                wins = p[Keys.EASY_WINS] ?: 0,
                losses = p[Keys.EASY_LOSSES] ?: 0,
                draws = p[Keys.EASY_DRAWS] ?: 0
            ),
            mediumStats = DifficultyStats(
                wins = p[Keys.MEDIUM_WINS] ?: 0,
                losses = p[Keys.MEDIUM_LOSSES] ?: 0,
                draws = p[Keys.MEDIUM_DRAWS] ?: 0
            ),
            hardStats = DifficultyStats(
                wins = p[Keys.HARD_WINS] ?: 0,
                losses = p[Keys.HARD_LOSSES] ?: 0,
                draws = p[Keys.HARD_DRAWS] ?: 0
            ),
            twoPlayerStats = TwoPlayerStats(
                totalGames = p[Keys.TWO_PLAYER_GAMES] ?: 0,
                xWins = p[Keys.TWO_PLAYER_X_WINS] ?: 0,
                oWins = p[Keys.TWO_PLAYER_O_WINS] ?: 0,
                draws = p[Keys.TWO_PLAYER_DRAWS] ?: 0
            )
        )
    }

    suspend fun recordSinglePlayerResult(
        difficulty: GameDifficulty,
        isHumanWin: Boolean,
        isAiWin: Boolean,
        isDraw: Boolean
    ) {
        context.statsDataStore.edit { p ->
            p[Keys.TOTAL_GAMES] = (p[Keys.TOTAL_GAMES] ?: 0) + 1
            if (isHumanWin) {
                p[Keys.WINS] = (p[Keys.WINS] ?: 0) + 1
            } else if (isAiWin) {
                p[Keys.LOSSES] = (p[Keys.LOSSES] ?: 0) + 1
            } else if (isDraw) {
                p[Keys.DRAWS] = (p[Keys.DRAWS] ?: 0) + 1
            }

            when (difficulty) {
                GameDifficulty.EASY -> {
                    if (isHumanWin) p[Keys.EASY_WINS] = (p[Keys.EASY_WINS] ?: 0) + 1
                    if (isAiWin) p[Keys.EASY_LOSSES] = (p[Keys.EASY_LOSSES] ?: 0) + 1
                    if (isDraw) p[Keys.EASY_DRAWS] = (p[Keys.EASY_DRAWS] ?: 0) + 1
                }
                GameDifficulty.MEDIUM -> {
                    if (isHumanWin) p[Keys.MEDIUM_WINS] = (p[Keys.MEDIUM_WINS] ?: 0) + 1
                    if (isAiWin) p[Keys.MEDIUM_LOSSES] = (p[Keys.MEDIUM_LOSSES] ?: 0) + 1
                    if (isDraw) p[Keys.MEDIUM_DRAWS] = (p[Keys.MEDIUM_DRAWS] ?: 0) + 1
                }
                GameDifficulty.HARD -> {
                    if (isHumanWin) p[Keys.HARD_WINS] = (p[Keys.HARD_WINS] ?: 0) + 1
                    if (isAiWin) p[Keys.HARD_LOSSES] = (p[Keys.HARD_LOSSES] ?: 0) + 1
                    if (isDraw) p[Keys.HARD_DRAWS] = (p[Keys.HARD_DRAWS] ?: 0) + 1
                }
            }
        }
    }

    suspend fun recordTwoPlayerResult(winner: Player?) {
        context.statsDataStore.edit { p ->
            p[Keys.TOTAL_GAMES] = (p[Keys.TOTAL_GAMES] ?: 0) + 1
            p[Keys.TWO_PLAYER_GAMES] = (p[Keys.TWO_PLAYER_GAMES] ?: 0) + 1

            when (winner) {
                Player.X -> {
                    p[Keys.WINS] = (p[Keys.WINS] ?: 0) + 1
                    p[Keys.TWO_PLAYER_X_WINS] = (p[Keys.TWO_PLAYER_X_WINS] ?: 0) + 1
                }
                Player.O -> {
                    p[Keys.LOSSES] = (p[Keys.LOSSES] ?: 0) + 1
                    p[Keys.TWO_PLAYER_O_WINS] = (p[Keys.TWO_PLAYER_O_WINS] ?: 0) + 1
                }
                null -> {
                    p[Keys.DRAWS] = (p[Keys.DRAWS] ?: 0) + 1
                    p[Keys.TWO_PLAYER_DRAWS] = (p[Keys.TWO_PLAYER_DRAWS] ?: 0) + 1
                }
            }
        }
    }

    suspend fun resetStatistics() {
        context.statsDataStore.edit { it.clear() }
    }
}
