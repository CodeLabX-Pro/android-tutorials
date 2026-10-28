package com.example.admobinterstitial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/**
 * A tiny level game that shows a full-screen interstitial ad between levels.
 * The ad is loaded early, so it's ready the moment it's needed, and shown only at a natural break:
 * after every second level, never in the middle of play.
 */
public class MainActivity extends AppCompatActivity {
    private static final int LEVELS_BETWEEN_ADS = 2;

    private InterstitialAd ad;
    private int level = 1;
    private TextView levelText, adStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        levelText = findViewById(R.id.textLevel);
        adStatus = findViewById(R.id.textAdStatus);
        if (savedInstanceState != null) level = savedInstanceState.getInt("level", 1);
        showLevel();

        // Start the Mobile Ads SDK once, off the main thread, so the app opens quickly.
        new Thread(() -> MobileAds.initialize(this, status -> {})).start();
        loadAd();

        findViewById(R.id.btnFinish).setOnClickListener(v -> finishLevel());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", level);
    }

    /**
     * Loads the next ad in the background. An ad can be shown only once, so load a new one after each.
     * The ad unit ID comes from app/build.gradle.kts: a test ID while you develop, yours in release.
     */
    private void loadAd() {
        adStatus.setText(R.string.ad_loading);
        InterstitialAd.load(this, BuildConfig.INTERSTITIAL_AD_UNIT_ID, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd loaded) {
                ad = loaded;
                adStatus.setText(R.string.ad_ready);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError error) {
                // No ad this time; the game simply carries on without one.
                ad = null;
                adStatus.setText(getString(R.string.ad_failed, error.getMessage()));
            }
        });
    }

    private void finishLevel() {
        boolean adBreak = level % LEVELS_BETWEEN_ADS == 0;
        if (adBreak && ad != null) {
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    nextLevel();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                    nextLevel();
                }
            });
            ad.show(this);
            ad = null; // Used up: load a fresh one for the next break.
            loadAd();
        } else {
            nextLevel();
        }
    }

    private void nextLevel() {
        level++;
        showLevel();
    }

    private void showLevel() {
        levelText.setText(getString(R.string.level, level));
    }
}
