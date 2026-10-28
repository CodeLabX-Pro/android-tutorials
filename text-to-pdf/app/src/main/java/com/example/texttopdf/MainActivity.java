package com.example.texttopdf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Type a title and some text, then turn them into a PDF and open it in the viewer. */
public class MainActivity extends AppCompatActivity {
    /** Writing a long PDF can take a moment, so it happens off the main thread. */
    private final ExecutorService background = Executors.newSingleThreadExecutor();
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    private TextInputLayout titleLayout, bodyLayout;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        titleLayout = findViewById(R.id.layoutTitle);
        bodyLayout = findViewById(R.id.layoutBody);
        create = findViewById(R.id.btnCreate);
        create.setOnClickListener(v -> create());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        background.shutdown();
    }

    private void create() {
        String title = text(titleLayout);
        String body = text(bodyLayout);
        titleLayout.setError(title.isEmpty() ? getString(R.string.error_title) : null);
        bodyLayout.setError(body.isEmpty() ? getString(R.string.error_body) : null);
        if (title.isEmpty() || body.isEmpty()) return;

        // Files in getFilesDir() belong to the app: no storage permission is needed.
        File file = new File(new File(getFilesDir(), "pdfs"), fileName(title));
        String date = DateFormat.getLongDateFormat(this).format(new Date());

        create.setEnabled(false);
        background.execute(() -> {
            boolean ok = writePdf(title, date, body, file);
            mainThread.post(() -> {
                create.setEnabled(true);
                if (ok) {
                    startActivity(new Intent(this, PdfViewerActivity.class).putExtra("path", file.getAbsolutePath()));
                } else {
                    Toast.makeText(this, R.string.error_create, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private static boolean writePdf(String title, String date, String body, File file) {
        File folder = file.getParentFile();
        if (folder == null || !(folder.isDirectory() || folder.mkdirs())) return false;
        try {
            PdfMaker.write(title, date, body, file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** "My notes: day 1" becomes "My-notes-day-1.pdf". Only letters, digits and dashes are safe in every file system. */
    static String fileName(String title) {
        String safe = title.replaceAll("[^A-Za-z0-9]+", "-").replaceAll("^-|-$", "");
        return (safe.isEmpty() ? "document" : safe) + ".pdf";
    }

    private static String text(TextInputLayout layout) {
        return layout.getEditText() == null ? "" : layout.getEditText().getText().toString().trim();
    }
}
