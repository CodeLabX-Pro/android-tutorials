package com.example.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/** Signs in with email and password through Firebase Authentication. */
public class LoginActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    TextInputLayout emailLayout, passwordLayout;
    TextInputEditText email, password;
    Button loginButton;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_login);

        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.buttonLogin);
        progress = findViewById(R.id.progress);
        TextView register = findViewById(R.id.textRegister);
        TextView forgot = findViewById(R.id.textForgot);

        register.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        forgot.setOnClickListener(v -> sendReset());
        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String mail = String.valueOf(email.getText()).trim();
        String pass = String.valueOf(password.getText());
        emailLayout.setError(Patterns.EMAIL_ADDRESS.matcher(mail).matches() ? null : getString(R.string.error_email));
        passwordLayout.setError(pass.isEmpty() ? getString(R.string.error_password) : null);
        if (emailLayout.getError() != null || passwordLayout.getError() != null) return;

        setBusy(true);
        auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(this, task -> {
            setBusy(false);
            if (task.isSuccessful()) {
                // Clear the back stack so Back from Home doesn't return here.
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                passwordLayout.setError(getString(R.string.error_login));
            }
        });
    }

    private void sendReset() {
        String mail = String.valueOf(email.getText()).trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            emailLayout.setError(getString(R.string.error_email));
            return;
        }
        auth.sendPasswordResetEmail(mail).addOnCompleteListener(this, task ->
                Toast.makeText(this, R.string.reset_sent, Toast.LENGTH_LONG).show());
    }

    private void setBusy(boolean busy) {
        progress.setVisibility(busy ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!busy);
    }
}
