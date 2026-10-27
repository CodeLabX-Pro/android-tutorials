package com.codingtutorials.emailintent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Writes an email and hands it to the phone's email app with an implicit Intent.
 * The app never needs your email password: the email app does the sending.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        TextInputLayout toLayout = findViewById(R.id.toLayout);
        TextInputEditText to = findViewById(R.id.to);
        TextInputEditText subject = findViewById(R.id.subject);
        TextInputEditText message = findViewById(R.id.message);
        Button send = findViewById(R.id.btnSend);

        send.setOnClickListener(v -> {
            String address = String.valueOf(to.getText()).trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(address).matches()) {
                toLayout.setError(getString(R.string.error_email));
                return;
            }
            toLayout.setError(null);
            // ACTION_SENDTO with a mailto: address opens only email apps, not every sharing app.
            Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{address})
                    .putExtra(Intent.EXTRA_SUBJECT, String.valueOf(subject.getText()))
                    .putExtra(Intent.EXTRA_TEXT, String.valueOf(message.getText()));
            try {
                startActivity(Intent.createChooser(email, getString(R.string.choose_app)));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_LONG).show();
            }
        });
    }
}
