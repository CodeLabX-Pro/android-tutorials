package com.codingtutorials.switchactivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Opens SecondActivity with an Intent, passing the name along, and gets a reply back when it closes.
 */
public class MainActivity extends AppCompatActivity {
    TextView reply;

    // Starts SecondActivity and receives its reply when the user comes back.
    private final ActivityResultLauncher<Intent> openSecond = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    reply.setText(getString(R.string.reply, result.getData().getStringExtra(SecondActivity.EXTRA_REPLY)));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        TextInputEditText name = findViewById(R.id.name);
        reply = findViewById(R.id.textReply);
        Button open = findViewById(R.id.btnOpen);

        open.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);
            // Extras carry small pieces of data to the next screen.
            intent.putExtra(SecondActivity.EXTRA_NAME, String.valueOf(name.getText()).trim());
            openSecond.launch(intent);
        });
    }
}
