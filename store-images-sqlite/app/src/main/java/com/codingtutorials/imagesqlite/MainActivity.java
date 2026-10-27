package com.codingtutorials.imagesqlite;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pick a photo, shrink it, save it into SQLite as a BLOB, and show every saved photo in a grid.
 * Image work and database work run on a background thread so the screen never freezes.
 */
public class MainActivity extends AppCompatActivity {
    private final ExecutorService background = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private DBHelper db;
    private ImageAdapter adapter;
    private byte[] picked;

    ImageView preview;
    TextInputEditText name;
    Button save;
    TextView empty;

    // The Photo Picker needs no storage permission; the user chooses exactly which photo to share.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::onPicked);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        db = new DBHelper(this);

        preview = findViewById(R.id.preview);
        name = findViewById(R.id.name);
        save = findViewById(R.id.btnSave);
        empty = findViewById(R.id.textEmpty);
        Button choose = findViewById(R.id.btnChoose);

        choose.setOnClickListener(v -> pickImage.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));
        save.setOnClickListener(v -> saveImage());

        RecyclerView grid = findViewById(R.id.grid);
        grid.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ImageAdapter(image -> background.execute(() -> {
            db.deleteImage(image.id);
            loadImages();
        }));
        grid.setAdapter(adapter);
        loadImages();
    }

    private void onPicked(Uri uri) {
        if (uri == null) return; // the user closed the picker
        background.execute(() -> {
            try {
                byte[] bytes = ImageUtils.shrinkToJpeg(getContentResolver(), uri);
                main.post(() -> {
                    picked = bytes;
                    preview.setImageBitmap(android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    save.setEnabled(true);
                });
            } catch (Exception e) {
                main.post(() -> Toast.makeText(this, R.string.error_read, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveImage() {
        if (picked == null) return;
        String title = String.valueOf(name.getText()).trim();
        String finalTitle = title.isEmpty() ? getString(R.string.untitled) : title;
        byte[] bytes = picked;
        background.execute(() -> {
            boolean ok = db.insertImage(finalTitle, bytes);
            main.post(() -> {
                Toast.makeText(this, ok ? R.string.saved : R.string.error_save, Toast.LENGTH_SHORT).show();
                if (ok) {
                    picked = null;
                    preview.setImageResource(R.drawable.ic_image);
                    name.setText("");
                    save.setEnabled(false);
                }
            });
            loadImages();
        });
    }

    /** Reads every image on the background thread, then updates the grid on the main thread. */
    private void loadImages() {
        background.execute(() -> {
            List<SavedImage> images = db.getImages();
            main.post(() -> {
                adapter.setImages(images);
                empty.setVisibility(images.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    protected void onDestroy() {
        background.shutdown();
        super.onDestroy();
    }
}
