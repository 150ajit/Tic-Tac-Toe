package com.antigravity.tictactoe.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.antigravity.tictactoe.domain.model.AppSettings
import com.antigravity.tictactoe.domain.model.AppThemeMode
import com.antigravity.tictactoe.domain.model.GameDifficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_preferences")

class PreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LAST_DIFFICULTY = stringPreferencesKey("last_difficulty")
    }

    val appSettingsFlow: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        val sound = preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        val haptics = preferences[PreferencesKeys.HAPTICS_ENABLED] ?: true
        val animations = preferences[PreferencesKeys.ANIMATIONS_ENABLED] ?: true
        val themeString = preferences[PreferencesKeys.THEME_MODE] ?: AppThemeMode.DARK.name
        val theme = runCatching { AppThemeMode.valueOf(themeString) }.getOrDefault(AppThemeMode.DARK)

        AppSettings(
            soundEnabled = sound,
            hapticsEnabled = haptics,
            animationsEnabled = animations,
            themeMode = theme
        )
    }

    val lastDifficultyFlow: Flow<GameDifficulty> = context.dataStore.data.map { preferences ->
        val difficultyString = preferences[PreferencesKeys.LAST_DIFFICULTY] ?: GameDifficulty.HARD.name
        runCatching { GameDifficulty.valueOf(difficultyString) }.getOrDefault(GameDifficulty.HARD)
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun setAnimationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANIMATIONS_ENABLED] = enabled
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setLastDifficulty(difficulty: GameDifficulty) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_DIFFICULTY] = difficulty.name
        }
    }
}
