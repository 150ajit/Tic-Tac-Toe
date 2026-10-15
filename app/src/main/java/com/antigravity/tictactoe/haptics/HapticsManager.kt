package com.antigravity.tictactoe.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticsManager(private val context: Context) {

    var isHapticsEnabled: Boolean = true

    private val vibrator: Vibrator? by lazy {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        }.getOrNull()
    }

    fun vibrateButtonClick() {
        if (!isHapticsEnabled || vibrator?.hasVibrator() != true) return
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }
    }

    fun vibrateMove() {
        if (!isHapticsEnabled || vibrator?.hasVibrator() != true) return
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else {
                vibrator?.vibrate(VibrationEffect.createOneShot(35, 180))
            }
        }
    }

    fun vibrateWin() {
        if (!isHapticsEnabled || vibrator?.hasVibrator() != true) return
        runCatching {
            // Strong celebration pattern: tick, tick, strong burst
            val timings = longArrayOf(0, 50, 70, 60, 90, 220)
            val amplitudes = intArrayOf(0, 160, 0, 200, 0, 255)
            vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        }
    }

    fun vibrateDraw() {
        if (!isHapticsEnabled || vibrator?.hasVibrator() != true) return
        runCatching {
            val timings = longArrayOf(0, 80, 80, 80)
            val amplitudes = intArrayOf(0, 120, 0, 120)
            vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        }
    }
}
