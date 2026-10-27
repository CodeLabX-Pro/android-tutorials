package com.codingtutorials.loginsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** The sign-up screen. Saves a new user, then goes straight to HomeActivity. */
public class RegisterActivity extends AppCompatActivity {
    EditText username, password;
    TextView loginText;
    Button signupButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_register);

        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.passwordText);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.textLogin);
        databaseHelper = new DatabaseHelper(this);

        // The login screen is still open underneath, so just close this one.
        loginText.setOnClickListener(v -> finish());

        signupButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (databaseHelper.insertData(user, pass)) {
                Toast.makeText(this, R.string.registered, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("USERNAME", user);
                // Clear the login and register screens so Back doesn't return to them.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.error_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
