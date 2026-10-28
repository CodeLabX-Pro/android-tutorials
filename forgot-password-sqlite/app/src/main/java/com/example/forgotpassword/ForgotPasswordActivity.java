package com.example.forgotpassword;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Reset a forgotten password: prove it's you with the security answer you chose when you signed up,
 * then pick a new password.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    EditText username, answer, newPassword, confirmPassword;
    Button resetButton;
    TextView backText;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_forgot_password);

        username = findViewById(R.id.userNameText);
        answer = findViewById(R.id.answerText);
        newPassword = findViewById(R.id.newPasswordText);
        confirmPassword = findViewById(R.id.confirmPasswordText);
        resetButton = findViewById(R.id.resetButton);
        backText = findViewById(R.id.textBack);
        databaseHelper = new DatabaseHelper(this);

        // Pre-fill the username if the login screen passed one.
        username.setText(getIntent().getStringExtra("USERNAME"));
        // The login screen is still open underneath, so just close this one.
        backText.setOnClickListener(v -> finish());

        resetButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String secret = answer.getText().toString();
            String pass = newPassword.getText().toString();
            if (user.isEmpty() || secret.trim().isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_all, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(confirmPassword.getText().toString())) {
                confirmPassword.setError(getString(R.string.error_mismatch));
                return;
            }
            if (databaseHelper.resetPassword(user, secret, pass)) {
                Toast.makeText(this, R.string.reset_done, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, R.string.error_reset, Toast.LENGTH_LONG).show();
            }
        });
    }
}
