package com.example.admobbanner;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * A screen with an adaptive AdMob banner pinned to the bottom.
 * Adaptive banners pick the best height for each phone's width, so they look right everywhere.
 */
public class MainActivity extends AppCompatActivity {

    private AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        // Start the Mobile Ads SDK once, off the main thread, so the app opens quickly.
        new Thread(() -> MobileAds.initialize(this, status -> {})).start();

        banner = new AdView(this);
        // The ad unit ID comes from app/build.gradle.kts: a test ID while you develop, yours in release.
        banner.setAdUnitId(BuildConfig.BANNER_AD_UNIT_ID);
        banner.setAdSize(adaptiveSize());
        FrameLayout container = findViewById(R.id.adContainer);
        container.addView(banner);
        banner.loadAd(new AdRequest.Builder().build());
    }

    /** The banner fills the screen width; AdMob chooses the height. */
    private AdSize adaptiveSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthDp = (int) (metrics.widthPixels / metrics.density);
        return AdSize.getLargeAnchoredAdaptiveBannerAdSize(this, widthDp);
    }

    // Pause the banner when the screen is hidden, and free it when the screen closes.
    @Override
    protected void onPause() {
        banner.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.resume();
    }

    @Override
    protected void onDestroy() {
        banner.destroy();
        super.onDestroy();
    }
}
