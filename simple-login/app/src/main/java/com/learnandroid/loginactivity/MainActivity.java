package com.learnandroid.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A first login screen: checks the username and password against fixed values.
 * Real apps check against a database or a server; see the SQLite and Firebase login tutorials.
 */
public class MainActivity extends AppCompatActivity {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "123456";

    TextInputLayout loginLayout, passwordLayout;
    TextInputEditText loginText, passwordText;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        loginLayout = findViewById(R.id.loginLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginText = findViewById(R.id.login);
        passwordText = findViewById(R.id.password);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(view -> {
            String username = String.valueOf(loginText.getText()).trim();
            String password = String.valueOf(passwordText.getText());

            // Show problems right under the box they belong to.
            loginLayout.setError(username.isEmpty() ? getString(R.string.error_username) : null);
            passwordLayout.setError(password.isEmpty() ? getString(R.string.error_password) : null);
            if (username.isEmpty() || password.isEmpty()) return;

            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                Intent intent = new Intent(this, NewActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                passwordText.setText("");
            } else {
                passwordLayout.setError(getString(R.string.error_invalid));
            }
        });
    }
}
