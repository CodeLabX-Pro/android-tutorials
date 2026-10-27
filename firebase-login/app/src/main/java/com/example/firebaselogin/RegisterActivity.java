package com.example.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/** Creates an account with email and password. Firebase stores it in the cloud. */
public class RegisterActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    TextInputLayout emailLayout, passwordLayout, confirmLayout;
    TextInputEditText email, password, confirm;
    Button registerButton;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_register);

        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmLayout = findViewById(R.id.confirmLayout);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        registerButton = findViewById(R.id.buttonRegister);
        progress = findViewById(R.id.progress);
        TextView login = findViewById(R.id.textLogin);

        login.setOnClickListener(v -> finish());
        registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String mail = String.valueOf(email.getText()).trim();
        String pass = String.valueOf(password.getText());
        emailLayout.setError(Patterns.EMAIL_ADDRESS.matcher(mail).matches() ? null : getString(R.string.error_email));
        // Firebase needs at least 6 characters; checking here gives a clearer message.
        passwordLayout.setError(pass.length() >= 6 ? null : getString(R.string.error_short));
        confirmLayout.setError(pass.equals(String.valueOf(confirm.getText())) ? null : getString(R.string.error_match));
        if (emailLayout.getError() != null || passwordLayout.getError() != null || confirmLayout.getError() != null) return;

        progress.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
        auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, task -> {
            progress.setVisibility(View.GONE);
            registerButton.setEnabled(true);
            if (task.isSuccessful()) {
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                emailLayout.setError(getString(R.string.error_exists));
            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                passwordLayout.setError(getString(R.string.error_short));
            } else {
                emailLayout.setError(getString(R.string.error_generic));
            }
        });
    }
}
