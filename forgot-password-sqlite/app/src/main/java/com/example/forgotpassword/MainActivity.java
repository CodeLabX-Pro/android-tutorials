package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** The login screen. Opens HomeActivity when the username and password match, or the reset screen from "Forgot password?". */
public class MainActivity extends AppCompatActivity {
    EditText username, password;
    TextView signupText;
    Button loginButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.textSignup);
        databaseHelper = new DatabaseHelper(this);

        signupText.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        // Pass on the username, so the reset screen is already half filled in.
        findViewById(R.id.textForgot).setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)
                .putExtra("USERNAME", username.getText().toString().trim())));

        loginButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            String loggedInUserName = databaseHelper.checkLogin(user, pass);
            if (loggedInUserName != null) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("USERNAME", loggedInUserName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
