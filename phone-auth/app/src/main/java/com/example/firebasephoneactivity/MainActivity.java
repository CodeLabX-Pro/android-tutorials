package com.example.firebasephoneactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Step 1: enter a phone number and send a code. Step 2: enter the 6-digit code.
 * Firebase sends the SMS and checks the code; often it reads the SMS automatically.
 */
public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    TextInputLayout phoneLayout, codeLayout;
    TextInputEditText phone, code;
    Button sendButton, verifyButton;
    TextView resend, stepTitle;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Already signed in? Go straight to Home.
        if (auth.getCurrentUser() != null) {
            openHome();
            return;
        }
        Screens.setUp(this, R.layout.activity_main);

        phoneLayout = findViewById(R.id.phoneLayout);
        codeLayout = findViewById(R.id.codeLayout);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        sendButton = findViewById(R.id.buttonSend);
        verifyButton = findViewById(R.id.buttonVerify);
        resend = findViewById(R.id.textResend);
        stepTitle = findViewById(R.id.textStep);
        progress = findViewById(R.id.progress);

        sendButton.setOnClickListener(v -> sendCode(null));
        resend.setOnClickListener(v -> sendCode(resendToken));
        verifyButton.setOnClickListener(v -> {
            String typed = String.valueOf(code.getText()).trim();
            if (typed.length() != 6 || verificationId == null) {
                codeLayout.setError(getString(R.string.error_code));
                return;
            }
            signIn(PhoneAuthProvider.getCredential(verificationId, typed));
        });
    }

    private void sendCode(PhoneAuthProvider.ForceResendingToken token) {
        String number = String.valueOf(phone.getText()).replace(" ", "");
        // Firebase needs the full international number, starting with + and the country code.
        if (!number.matches("\\+\\d{8,15}")) {
            phoneLayout.setError(getString(R.string.error_phone));
            return;
        }
        phoneLayout.setError(null);
        setBusy(true);
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks);
        if (token != null) options.setForceResendingToken(token);
        PhoneAuthProvider.verifyPhoneNumber(options.build());
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    // Android read the SMS for us (or the number was verified instantly).
                    signIn(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    setBusy(false);
                    phoneLayout.setError(getString(R.string.error_send));
                }

                @Override
                public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    setBusy(false);
                    verificationId = id;
                    resendToken = token;
                    showCodeStep();
                }
            };

    private void signIn(PhoneAuthCredential credential) {
        setBusy(true);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            setBusy(false);
            if (task.isSuccessful()) openHome();
            else codeLayout.setError(getString(R.string.error_code));
        });
    }

    private void showCodeStep() {
        stepTitle.setText(R.string.step_code);
        phoneLayout.setEnabled(false);
        sendButton.setVisibility(View.GONE);
        codeLayout.setVisibility(View.VISIBLE);
        verifyButton.setVisibility(View.VISIBLE);
        resend.setVisibility(View.VISIBLE);
        code.requestFocus();
    }

    private void setBusy(boolean busy) {
        progress.setVisibility(busy ? View.VISIBLE : View.GONE);
        sendButton.setEnabled(!busy);
        verifyButton.setEnabled(!busy);
    }

    private void openHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
