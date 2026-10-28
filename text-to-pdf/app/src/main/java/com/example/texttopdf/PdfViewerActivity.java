package com.example.texttopdf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Shows a PDF with Android's built-in PdfRenderer: every page is drawn into a picture.
 * Share sends it to another app; Save puts a copy wherever the user chooses, such as Downloads.
 */
public class PdfViewerActivity extends AppCompatActivity {
    private File file;

    /** Opens the system "Save to" screen. No storage permission is needed: the user picks the place. */
    private final ActivityResultLauncher<String> saveCopy = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("application/pdf"), this::copyTo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_pdf_viewer);
        file = new File(Objects.requireNonNull(getIntent().getStringExtra("path")));
        ((TextView) findViewById(R.id.textFileName)).setText(file.getName());

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnShare).setOnClickListener(v -> share());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveCopy.launch(file.getName()));

        showPages(findViewById(R.id.pages));
    }

    private void showPages(LinearLayout pages) {
        int width = getResources().getDisplayMetrics().widthPixels;
        try (ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
             PdfRenderer renderer = new PdfRenderer(descriptor)) {
            for (int i = 0; i < renderer.getPageCount(); i++) {
                try (PdfRenderer.Page page = renderer.openPage(i)) {
                    // Match the screen width and keep the page's shape.
                    Bitmap picture = Bitmap.createBitmap(width, width * page.getHeight() / page.getWidth(), Bitmap.Config.ARGB_8888);
                    picture.eraseColor(Color.WHITE); // PDF pages are transparent until something is drawn.
                    page.render(picture, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    ImageView view = new ImageView(this);
                    view.setImageBitmap(picture);
                    view.setAdjustViewBounds(true);
                    view.setContentDescription(getString(R.string.page_number, i + 1));
                    view.setElevation(4 * getResources().getDisplayMetrics().density);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = (int) (16 * getResources().getDisplayMetrics().density);
                    pages.addView(view, params);
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_open, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /** FileProvider gives the other app a temporary content:// link, because app files are private. */
    private void share() {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".files", file);
        Intent send = new Intent(Intent.ACTION_SEND)
                .setType("application/pdf")
                .putExtra(Intent.EXTRA_STREAM, uri)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(send, getString(R.string.share)));
    }

    private void copyTo(Uri destination) {
        if (destination == null) return; // The user cancelled.
        try (InputStream in = new FileInputStream(file);
             OutputStream out = getContentResolver().openOutputStream(destination)) {
            if (out == null) throw new IOException("No output stream");
            byte[] buffer = new byte[8192];
            for (int read; (read = in.read(buffer)) != -1; ) out.write(buffer, 0, read);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_save, Toast.LENGTH_LONG).show();
        }
    }
}
