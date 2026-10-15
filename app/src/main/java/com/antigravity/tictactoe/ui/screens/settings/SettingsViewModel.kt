package com.antigravity.tictactoe.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antigravity.tictactoe.TicTacToeApp
import com.antigravity.tictactoe.domain.model.AppSettings
import com.antigravity.tictactoe.domain.model.AppThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val preferencesRepository = TicTacToeApp.instance.preferencesRepository

    val appSettings: StateFlow<AppSettings> = preferencesRepository.appSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSoundEnabled(enabled)
        }
    }

    fun toggleHaptics(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setHapticsEnabled(enabled)
        }
    }

    fun toggleAnimations(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setAnimationsEnabled(enabled)
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }
}
