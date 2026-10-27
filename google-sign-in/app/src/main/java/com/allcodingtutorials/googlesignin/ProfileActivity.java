package com.allcodingtutorials.googlesignin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.ClearCredentialException;

import com.bumptech.glide.Glide;

/** Shows the Google account's name, email and photo. Sign out clears the remembered account. */
public class ProfileActivity extends AppCompatActivity {
    static final String EXTRA_NAME = "name";
    static final String EXTRA_EMAIL = "email";
    static final String EXTRA_PHOTO = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_profile);

        TextView name = findViewById(R.id.textName);
        TextView email = findViewById(R.id.textEmail);
        ImageView photo = findViewById(R.id.imagePhoto);
        name.setText(getIntent().getStringExtra(EXTRA_NAME));
        email.setText(getIntent().getStringExtra(EXTRA_EMAIL));

        // Glide downloads the photo, caches it, and crops it into a circle.
        Glide.with(this)
                .load(getIntent().getStringExtra(EXTRA_PHOTO))
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(photo);

        Button signOut = findViewById(R.id.buttonSignOut);
        signOut.setOnClickListener(v -> CredentialManager.create(this).clearCredentialStateAsync(
                new ClearCredentialStateRequest(), null, ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<Void, ClearCredentialException>() {
                    @Override
                    public void onResult(Void result) {
                        finish();
                    }

                    @Override
                    public void onError(ClearCredentialException e) {
                        finish();
                    }
                }));
    }
}
