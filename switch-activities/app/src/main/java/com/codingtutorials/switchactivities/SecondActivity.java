package com.codingtutorials.switchactivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** The second screen. Reads the name it was given, and can send a reply back. */
public class SecondActivity extends AppCompatActivity {
    static final String EXTRA_NAME = "name";
    static final String EXTRA_REPLY = "reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_second);

        String name = getIntent().getStringExtra(EXTRA_NAME);
        TextView hello = findViewById(R.id.textHello);
        hello.setText(getString(R.string.hello, name == null || name.isEmpty() ? getString(R.string.friend) : name));

        // Back without a reply: just close this screen.
        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());

        // Back with a reply: set a result, then close.
        Button wave = findViewById(R.id.btnWave);
        wave.setOnClickListener(v -> {
            setResult(RESULT_OK, new Intent().putExtra(EXTRA_REPLY, getString(R.string.wave_text)));
            finish();
        });
    }
}
