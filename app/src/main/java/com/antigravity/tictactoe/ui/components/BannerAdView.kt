package com.antigravity.tictactoe.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.antigravity.tictactoe.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Reusable AdMob Banner Ad View Composable.
 *
 * AdMob Policy Compliance:
 * - Uses Google's official Sample Ad Unit ID for DEBUG builds to prevent policy violations / invalid traffic.
 * - Uses the production Ad Unit ID for RELEASE builds.
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        // Preview placeholder
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = if (BuildConfig.DEBUG) {
                        // Official Google Test Ad Unit ID for Banner
                        "ca-app-pub-3940256099942544/6300978111"
                    } else {
                        // User's Production Ad Unit ID
                        "ca-app-pub-3991312474055332/6441529034"
                    }
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
