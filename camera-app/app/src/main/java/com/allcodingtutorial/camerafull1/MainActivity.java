package com.allcodingtutorial.camerafull1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

/**
 * Takes a full-resolution photo with the phone's camera app, shows it, and shares it.
 * The camera app writes straight into a file we own, shared safely through FileProvider.
 */
public class MainActivity extends AppCompatActivity {
    Button btnpicture, btnShare;
    ImageView imageView;
    TextView placeholder;
    private Uri photoUri;

    // TakePicture saves the full photo to photoUri and reports true when a photo was taken.
    private final ActivityResultLauncher<Uri> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), saved -> {
                if (saved) {
                    imageView.setImageURI(null); // forget the previous photo at the same address
                    imageView.setImageURI(photoUri);
                    placeholder.setVisibility(View.GONE);
                    btnShare.setEnabled(true);
                } else {
                    Toast.makeText(this, R.string.cancelled, Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        btnpicture = findViewById(R.id.btncamera_id);
        btnShare = findViewById(R.id.btnshare_id);
        imageView = findViewById(R.id.image);
        placeholder = findViewById(R.id.placeholder);

        photoUri = newPhotoUri();
        btnpicture.setOnClickListener(v -> {
            try {
                takePicture.launch(photoUri);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.no_camera, Toast.LENGTH_SHORT).show();
            }
        });
        btnShare.setOnClickListener(v -> sharePhoto());
    }

    /** A file in our private cache, plus a content:// address the camera app is allowed to write to. */
    private Uri newPhotoUri() {
        File folder = new File(getCacheDir(), "images");
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();
        File file = new File(folder, "captured_image.jpg");
        return FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
    }

    private void sharePhoto() {
        Intent share = new Intent(Intent.ACTION_SEND)
                .setType("image/jpeg")
                .putExtra(Intent.EXTRA_STREAM, photoUri)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, getString(R.string.share_with)));
    }
}
