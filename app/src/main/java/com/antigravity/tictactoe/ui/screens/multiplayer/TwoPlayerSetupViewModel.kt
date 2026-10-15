package com.antigravity.tictactoe.ui.screens.multiplayer

import androidx.lifecycle.ViewModel
import com.antigravity.tictactoe.domain.model.GameMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TwoPlayerSetupState(
    val player1Name: String = "Player 1",
    val player2Name: String = "Player 2"
)

class TwoPlayerSetupViewModel : ViewModel() {

    private val _state = MutableStateFlow(TwoPlayerSetupState())
    val state: StateFlow<TwoPlayerSetupState> = _state.asStateFlow()

    fun updatePlayer1Name(input: String) {
        val sanitized = input.take(15).trimStart()
        _state.update { it.copy(player1Name = sanitized) }
    }

    fun updatePlayer2Name(input: String) {
        val sanitized = input.take(15).trimStart()
        _state.update { it.copy(player2Name = sanitized) }
    }

    fun buildGameMode(): GameMode.TwoPlayers {
        val p1 = _state.value.player1Name.trim().ifEmpty { "Player 1" }
        val p2 = _state.value.player2Name.trim().ifEmpty { "Player 2" }
        return GameMode.TwoPlayers(player1Name = p1, player2Name = p2)
    }
}
