package com.example.texttopdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Turns a title and some text into an A4 PDF with Android's built-in PdfDocument, so no library is needed.
 * Long text flows onto as many pages as it needs.
 */
final class PdfMaker {
    /** PDF pages are measured in points: 1/72 of an inch. A4 is 595 × 842 points. */
    private static final int PAGE_WIDTH = 595;
    private static final int PAGE_HEIGHT = 842;
    private static final int MARGIN = 56;
    private static final int TEXT_WIDTH = PAGE_WIDTH - 2 * MARGIN;
    private static final int BRAND = Color.rgb(226, 67, 47);

    private PdfMaker() {
    }

    static void write(String title, String date, String body, File file) throws IOException {
        TextPaint titlePaint = paint(26, BRAND, Typeface.BOLD);
        TextPaint datePaint = paint(11, Color.GRAY, Typeface.NORMAL);
        TextPaint bodyPaint = paint(13, Color.rgb(28, 27, 34), Typeface.NORMAL);
        Paint rule = new Paint();
        rule.setColor(Color.rgb(230, 230, 236));

        // StaticLayout wraps the text into lines that fit the page width.
        StaticLayout titleLines = lines(title, titlePaint, 1f);
        StaticLayout bodyLines = lines(body, bodyPaint, 1.35f);

        PdfDocument document = new PdfDocument();
        try {
            int line = 0;
            int pageNumber = 1;
            do {
                PdfDocument.Page page = document.startPage(new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create());
                Canvas canvas = page.getCanvas();
                int top = MARGIN;

                // The first page starts with the title, the date and a thin line.
                if (pageNumber == 1) {
                    drawAt(canvas, titleLines, MARGIN, top);
                    top += titleLines.getHeight() + 6;
                    canvas.drawText(date, MARGIN, top + 11, datePaint);
                    top += 28;
                    canvas.drawRect(MARGIN, top, PAGE_WIDTH - MARGIN, top + 1, rule);
                    top += 22;
                }

                // Fit as many whole lines as there's room for, leaving space for the page number.
                int room = PAGE_HEIGHT - MARGIN - 24 - top;
                int firstTop = bodyLines.getLineTop(line);
                int last = line;
                while (last < bodyLines.getLineCount() && bodyLines.getLineBottom(last) - firstTop <= room) last++;
                if (last == line) last++; // A single line taller than the page still moves on.

                canvas.save();
                canvas.clipRect(MARGIN, top, PAGE_WIDTH - MARGIN, top + bodyLines.getLineBottom(last - 1) - firstTop);
                drawAt(canvas, bodyLines, MARGIN, top - firstTop);
                canvas.restore();

                String number = String.valueOf(pageNumber);
                canvas.drawText(number, (PAGE_WIDTH - datePaint.measureText(number)) / 2, PAGE_HEIGHT - MARGIN / 2f, datePaint);
                document.finishPage(page);

                line = last;
                pageNumber++;
            } while (line < bodyLines.getLineCount());

            try (FileOutputStream out = new FileOutputStream(file)) {
                document.writeTo(out);
            }
        } finally {
            document.close();
        }
    }

    private static TextPaint paint(float size, int colour, int style) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(colour);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, style));
        return paint;
    }

    private static StaticLayout lines(String text, TextPaint paint, float spacing) {
        return StaticLayout.Builder.obtain(text, 0, text.length(), paint, TEXT_WIDTH)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0, spacing)
                .build();
    }

    private static void drawAt(Canvas canvas, StaticLayout layout, float x, float y) {
        canvas.save();
        canvas.translate(x, y);
        layout.draw(canvas);
        canvas.restore();
    }
}
