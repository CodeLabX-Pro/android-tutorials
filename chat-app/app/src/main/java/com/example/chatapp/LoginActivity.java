package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Sign in, or create an account, on one screen. Signed-in users skip straight to the chat list.
 */
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private boolean creatingAccount;

    private TextView title, switchMode;
    private TextInputLayout nameLayout, emailLayout, passwordLayout;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            openChats();
            return;
        }
        Screens.setUp(this, R.layout.activity_login);
        title = findViewById(R.id.textTitle);
        switchMode = findViewById(R.id.textSwitch);
        nameLayout = findViewById(R.id.layoutName);
        emailLayout = findViewById(R.id.layoutEmail);
        passwordLayout = findViewById(R.id.layoutPassword);
        submit = findViewById(R.id.btnSubmit);

        switchMode.setOnClickListener(v -> {
            creatingAccount = !creatingAccount;
            updateMode();
        });
        submit.setOnClickListener(v -> submit());
        updateMode();
    }

    /** Shows the name box and changes the text when creating an account. */
    private void updateMode() {
        nameLayout.setVisibility(creatingAccount ? View.VISIBLE : View.GONE);
        title.setText(creatingAccount ? R.string.title_create : R.string.title_sign_in);
        submit.setText(creatingAccount ? R.string.create_account : R.string.sign_in);
        switchMode.setText(creatingAccount ? R.string.switch_to_sign_in : R.string.switch_to_create);
    }

    private void submit() {
        String name = text(nameLayout);
        String email = text(emailLayout);
        String password = text(passwordLayout);
        nameLayout.setError(creatingAccount && name.isEmpty() ? getString(R.string.error_name) : null);
        emailLayout.setError(Patterns.EMAIL_ADDRESS.matcher(email).matches() ? null : getString(R.string.error_email));
        passwordLayout.setError(password.length() >= 6 ? null : getString(R.string.error_password));
        if (nameLayout.getError() != null || emailLayout.getError() != null || passwordLayout.getError() != null) return;

        submit.setEnabled(false);
        if (creatingAccount) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> saveUser(Objects.requireNonNull(result.getUser()), name, email))
                    .addOnFailureListener(this::showError);
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> openChats())
                    .addOnFailureListener(this::showError);
        }
    }

    /** Saves the new user's name, so other people can find them in the chat list. */
    private void saveUser(FirebaseUser user, String name, String email) {
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                .setValue(new User(name, email))
                .addOnSuccessListener(unused -> openChats())
                .addOnFailureListener(this::showError);
    }

    private void showError(Exception e) {
        submit.setEnabled(true);
        passwordLayout.setError(e.getLocalizedMessage());
    }

    private void openChats() {
        startActivity(new Intent(this, UsersActivity.class));
        finish();
    }

    private static String text(TextInputLayout layout) {
        return layout.getEditText() == null ? "" : layout.getEditText().getText().toString().trim();
    }
}
