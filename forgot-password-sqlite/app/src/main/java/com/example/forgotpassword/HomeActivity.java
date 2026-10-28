package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

/** Shown after login. Log out (or press Back) to return to the login screen. */
public class HomeActivity extends AppCompatActivity {
    TextView userName;
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_home);

        userName = findViewById(R.id.textUserName);
        logOut = findViewById(R.id.logOutButton);
        String user = getIntent().getStringExtra("USERNAME");
        userName.setText(getString(R.string.welcome, user));

        logOut.setOnClickListener(v -> logOut());
        // Back logs out too, instead of leaving the user on an empty screen.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logOut();
            }
        });
    }

    private void logOut() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
