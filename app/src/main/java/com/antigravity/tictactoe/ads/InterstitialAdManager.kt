package com.antigravity.tictactoe.ads

import android.app.Activity
import android.content.Context
import com.antigravity.tictactoe.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Manages loading, caching, and showing AdMob Interstitial Ads.
 *
 * AdMob Policy Compliance:
 * - Uses Google's official sample Interstitial Ad Unit ID for DEBUG builds.
 * - Uses the user's production Interstitial Ad Unit ID for RELEASE builds.
 * - Shows ads only at natural transition points (after match completion).
 * - Enforces frequency capping (e.g. every 2 matches) to avoid spamming the user.
 */
class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var completedMatchesCount = 0

    // Show an interstitial ad after every 2 completed matches
    private val matchesBetweenAds = 2

    private val adUnitId: String
        get() = if (BuildConfig.DEBUG) {
            // Official Google Sample Interstitial Ad Unit ID
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            // User's Production Interstitial Ad Unit ID
            "ca-app-pub-3991312474055332/4657674073"
        }

    init {
        loadAd()
    }

    /**
     * Preloads an interstitial ad in the background.
     */
    fun loadAd() {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    /**
     * Called when a match finishes and the player takes an action (Play Again / New Match / Home).
     * Automatically applies frequency capping and triggers the ad callback.
     */
    fun onMatchFinishedAction(activity: Activity?, onComplete: () -> Unit) {
        completedMatchesCount++
        if (completedMatchesCount >= matchesBetweenAds) {
            completedMatchesCount = 0
            showAd(activity, onComplete)
        } else {
            onComplete()
        }
    }

    /**
     * Shows the preloaded interstitial ad if available.
     */
    fun showAd(activity: Activity?, onComplete: () -> Unit) {
        val ad = interstitialAd
        if (activity != null && ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                    onComplete()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    onComplete()
                }
            }
            ad.show(activity)
        } else {
            // If ad is not loaded or activity is null, proceed immediately
            loadAd()
            onComplete()
        }
    }
}
