package com.antigravity.tictactoe.ui.screens.singleplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.model.GameDifficulty
import com.antigravity.tictactoe.domain.model.GameMode
import com.antigravity.tictactoe.domain.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SinglePlayerSetupState(
    val selectedSymbol: Player = Player.X,
    val selectedDifficulty: GameDifficulty = GameDifficulty.HARD
)

class SinglePlayerSetupViewModel : ViewModel() {

    private val preferencesRepository = TicTacToeApp.instance.preferencesRepository

    private val _state = MutableStateFlow(SinglePlayerSetupState())
    val state: StateFlow<SinglePlayerSetupState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.lastDifficultyFlow.collect { difficulty ->
                _state.update { it.copy(selectedDifficulty = difficulty) }
            }
        }
    }

    fun selectSymbol(symbol: Player) {
        _state.update { it.copy(selectedSymbol = symbol) }
    }

    fun selectDifficulty(difficulty: GameDifficulty) {
        _state.update { it.copy(selectedDifficulty = difficulty) }
        viewModelScope.launch {
            preferencesRepository.setLastDifficulty(difficulty)
        }
    }

    fun buildGameMode(): GameMode.SinglePlayer {
        return GameMode.SinglePlayer(
            humanSymbol = _state.value.selectedSymbol,
            difficulty = _state.value.selectedDifficulty
        )
    }
}
