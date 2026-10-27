package com.learnandroid.loginactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** The screen after a successful login. Log out closes it and returns to the login screen. */
public class NewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_new);

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText(getString(R.string.welcome, getIntent().getStringExtra("USERNAME")));

        Button logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(v -> finish());
    }
}
