package com.codingtutorials.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Shows the logo for a moment, then opens MainActivity.
 * This is the classic approach from the video. On Android 12 and later the system also shows its own
 * launch splash first; the Animated Splash Screen tutorial shows the newer SplashScreen API instead.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY_MS = 2000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable openMain = () -> {
        startActivity(new Intent(this, MainActivity.class));
        finish(); // remove the splash so Back doesn't return to it
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(openMain, SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        // If the user leaves during the delay, don't open MainActivity afterwards.
        handler.removeCallbacks(openMain);
        super.onDestroy();
    }
}
