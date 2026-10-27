package com.allcodingtutorials.googlesignin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

/**
 * "Sign in with Google" using Credential Manager, Google's current sign-in API.
 * It shows the system account picker and returns the user's name, email and photo.
 */
public class MainActivity extends AppCompatActivity {
    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        credentialManager = CredentialManager.create(this);

        Button signIn = findViewById(R.id.buttonGoogle);
        signIn.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        // The Web client ID comes from the Google Cloud console (see the guide).
        GetSignInWithGoogleOption google = new GetSignInWithGoogleOption.Builder(getString(R.string.web_client_id)).build();
        GetCredentialRequest request = new GetCredentialRequest.Builder().addCredentialOption(google).build();

        credentialManager.getCredentialAsync(this, request, null, ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        handle(response.getCredential());
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        if (e instanceof GetCredentialCancellationException) return; // the user closed the sheet
                        // No Google account on this device: tell the user how to fix it.
                        int message = e instanceof NoCredentialException ? R.string.error_no_account : R.string.error_sign_in;
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handle(Credential credential) {
        if (credential instanceof CustomCredential
                && GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
            GoogleIdTokenCredential google = GoogleIdTokenCredential.createFrom(credential.getData());
            Intent profile = new Intent(this, ProfileActivity.class)
                    .putExtra(ProfileActivity.EXTRA_NAME, google.getDisplayName())
                    .putExtra(ProfileActivity.EXTRA_EMAIL, google.getId())
                    .putExtra(ProfileActivity.EXTRA_PHOTO, google.getProfilePictureUri() != null ? google.getProfilePictureUri().toString() : null);
            // To keep users signed in across launches, send google.getIdToken() to your backend or to Firebase Auth.
            startActivity(profile);
        } else {
            Toast.makeText(this, R.string.error_sign_in, Toast.LENGTH_LONG).show();
        }
    }
}
