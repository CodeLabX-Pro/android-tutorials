package com.codingtutorials.animatedsplash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Uses the official SplashScreen API, so there is no separate splash activity.
 * 1. installSplashScreen() shows the animated icon set in the theme.
 * 2. setKeepOnScreenCondition() keeps it up until the app is ready.
 * 3. setOnExitAnimationListener() animates it away.
 */
public class MainActivity extends AppCompatActivity {
    private volatile boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Must be called before super.onCreate().
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Keep the splash on screen while the app "loads". Replace the delay with your real loading work.
        splashScreen.setKeepOnScreenCondition(() -> !ready);
        new Handler(Looper.getMainLooper()).postDelayed(() -> ready = true, 1500);

        // When the splash leaves, shrink the icon and fade the splash, then remove it.
        splashScreen.setOnExitAnimationListener(provider -> {
            View icon = provider.getIconView();
            AnimatorSet exit = new AnimatorSet();
            exit.playTogether(
                    ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 0.4f),
                    ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 0.4f),
                    ObjectAnimator.ofFloat(provider.getView(), View.ALPHA, 1f, 0f));
            exit.setInterpolator(new AnticipateInterpolator());
            exit.setDuration(400);
            exit.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    provider.remove();
                }
            });
            exit.start();
        });
    }
}
