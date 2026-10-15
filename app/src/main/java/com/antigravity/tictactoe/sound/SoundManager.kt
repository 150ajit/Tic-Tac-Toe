package com.antigravity.tictactoe.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Modular sound management layer.
 * Generates smooth, low-latency synthetic neon game audio (chimes, clicks, chords, fanfare)
 * completely dynamically without relying on external media files, ensuring 100% offline reliability
 * and zero crash risk if optional audio assets are absent.
 */
class SoundManager(private val context: Context) {

    private val audioScope = CoroutineScope(Dispatchers.Default)
    var isSoundEnabled: Boolean = true

    fun playButtonTap() {
        if (!isSoundEnabled) return
        audioScope.launch {
            playTone(frequency = 720.0, durationMs = 45, volume = 0.25f)
        }
    }

    fun playXMove() {
        if (!isSoundEnabled) return
        audioScope.launch {
            // High-tech rising dual-tone for X
            playDualTone(f1 = 523.25, f2 = 659.25, durationMs = 75, volume = 0.35f) // C5 + E5
        }
    }

    fun playOMove() {
        if (!isSoundEnabled) return
        audioScope.launch {
            // Soft resonant neon chime for O
            playDualTone(f1 = 587.33, f2 = 880.00, durationMs = 80, volume = 0.35f) // D5 + A5
        }
    }

    fun playWin() {
        if (!isSoundEnabled) return
        audioScope.launch {
            // Joyful 4-note victory arpeggio: C5 -> E5 -> G5 -> C6
            val notes = listOf(523.25, 659.25, 783.99, 1046.50)
            for (note in notes) {
                playTone(frequency = note, durationMs = 110, volume = 0.45f)
            }
        }
    }

    fun playDraw() {
        if (!isSoundEnabled) return
        audioScope.launch {
            // Descending neutral chord: G4 -> F4 -> Eb4
            val notes = listOf(392.00, 349.23, 311.13)
            for (note in notes) {
                playTone(frequency = note, durationMs = 130, volume = 0.3f)
            }
        }
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Float) {
        runCatching {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            if (numSamples <= 0) return

            val generatedSnd = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                // Apply a smooth decay envelope to prevent audio pops
                val envelope = 1.0 - (i.toDouble() / numSamples)
                val sample = sin(2.0 * PI * i / (sampleRate / frequency)) * envelope * volume
                generatedSnd[i] = (sample * Short.MAX_VALUE).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(numSamples * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, numSamples)
            audioTrack.play()
            Thread.sleep(durationMs.toLong())
            audioTrack.release()
        }
    }

    private fun playDualTone(f1: Double, f2: Double, durationMs: Int, volume: Float) {
        runCatching {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            if (numSamples <= 0) return

            val generatedSnd = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val envelope = 1.0 - (i.toDouble() / numSamples)
                val s1 = sin(2.0 * PI * i / (sampleRate / f1))
                val s2 = sin(2.0 * PI * i / (sampleRate / f2))
                val combined = ((s1 + s2) * 0.5) * envelope * volume
                generatedSnd[i] = (combined * Short.MAX_VALUE).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(numSamples * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, numSamples)
            audioTrack.play()
            Thread.sleep(durationMs.toLong())
            audioTrack.release()
        }
    }
}
