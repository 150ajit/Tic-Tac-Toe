package com.antigravity.tictactoe

import android.app.Application
import com.antigravity.tictactoe.data.preferences.PreferencesRepository
import com.antigravity.tictactoe.data.statistics.StatisticsRepository
import com.antigravity.tictactoe.domain.ai.TicTacToeAiEngine
import com.antigravity.tictactoe.domain.game.TicTacToeGameEngine
import com.antigravity.tictactoe.haptics.HapticsManager
import com.antigravity.tictactoe.sound.SoundManager

class TicTacToeApp : Application() {

    lateinit var preferencesRepository: PreferencesRepository
        private set

    lateinit var statisticsRepository: StatisticsRepository
        private set

    lateinit var soundManager: SoundManager
        private set

    lateinit var hapticsManager: HapticsManager
        private set

    lateinit var gameEngine: TicTacToeGameEngine
        private set

    lateinit var aiEngine: TicTacToeAiEngine
        private set

    lateinit var interstitialAdManager: com.antigravity.tictactoe.ads.InterstitialAdManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferencesRepository = PreferencesRepository(this)
        statisticsRepository = StatisticsRepository(this)
        soundManager = SoundManager(this)
        hapticsManager = HapticsManager(this)
        gameEngine = TicTacToeGameEngine()
        aiEngine = TicTacToeAiEngine()

        // Initialize Google Mobile Ads (AdMob) SDK
        com.google.android.gms.ads.MobileAds.initialize(this) {}
        interstitialAdManager = com.antigravity.tictactoe.ads.InterstitialAdManager(this)
    }

    companion object {
        lateinit var instance: TicTacToeApp
            private set
    }
}
