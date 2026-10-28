package com.example.forgotpassword;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Shared setup for every screen: draw edge to edge, but keep content clear of the system bars. */
final class Screens {
    private Screens() {}

    static void setUp(AppCompatActivity activity, int layout) {
        EdgeToEdge.enable(activity);
        activity.setContentView(layout);
        ViewCompat.setOnApplyWindowInsetsListener(activity.findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}
