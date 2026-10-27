package com.codingtutorials.fragments;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/** One screen with a container that shows either FirstFragment or SecondFragment. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        Button first = findViewById(R.id.btnFirst);
        Button second = findViewById(R.id.btnSecond);
        first.setOnClickListener(v -> show(new FirstFragment()));
        second.setOnClickListener(v -> show(new SecondFragment()));

        // Only add the first fragment on a fresh start. After rotation, Android restores the one on screen.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, new FirstFragment())
                    .commit();
        }
    }

    /** Swap the fragment in the container. addToBackStack lets Back return to the previous one. */
    private void show(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
