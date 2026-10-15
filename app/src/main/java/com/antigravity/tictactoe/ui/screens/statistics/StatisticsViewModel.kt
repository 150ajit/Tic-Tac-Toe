package com.antigravity.tictactoe.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.model.Statistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatisticsViewModel : ViewModel() {

    private val statisticsRepository = TicTacToeApp.instance.statisticsRepository

    val statistics: StateFlow<Statistics> = statisticsRepository.statisticsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Statistics()
        )

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    fun showResetConfirmation() {
        _showResetDialog.value = true
    }

    fun dismissResetConfirmation() {
        _showResetDialog.value = false
    }

    fun resetStatistics() {
        viewModelScope.launch {
            statisticsRepository.resetStatistics()
            _showResetDialog.value = false
        }
    }
}
