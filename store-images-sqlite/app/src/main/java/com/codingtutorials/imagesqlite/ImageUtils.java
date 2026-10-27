package com.codingtutorials.imagesqlite;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Phone photos are often 5–10 MB. SQLite can only read about 2 MB from one row at a time,
 * so we shrink each photo before saving it. Run this off the main thread.
 */
final class ImageUtils {
    private static final int MAX_SIDE = 1024; // pixels on the longest side
    private static final int JPEG_QUALITY = 80;

    private ImageUtils() {}

    static byte[] shrinkToJpeg(ContentResolver resolver, Uri uri) throws IOException {
        // 1. Read only the size, without loading the pixels.
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        try (InputStream in = resolver.openInputStream(uri)) {
            BitmapFactory.decodeStream(in, null, bounds);
        }
        // 2. Load a smaller copy: every inSampleSize step halves width and height.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        while (Math.max(bounds.outWidth, bounds.outHeight) / (options.inSampleSize * 2) >= MAX_SIDE) {
            options.inSampleSize *= 2;
        }
        Bitmap bitmap;
        try (InputStream in = resolver.openInputStream(uri)) {
            bitmap = BitmapFactory.decodeStream(in, null, options);
        }
        if (bitmap == null) throw new IOException("Not an image");
        // 3. Fit exactly within MAX_SIDE, then compress to JPEG bytes.
        float scale = Math.min(1f, (float) MAX_SIDE / Math.max(bitmap.getWidth(), bitmap.getHeight()));
        if (scale < 1f) {
            bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * scale), Math.round(bitmap.getHeight() * scale), true);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out);
        return out.toByteArray();
    }
}
