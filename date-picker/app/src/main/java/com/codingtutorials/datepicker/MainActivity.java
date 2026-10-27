package com.codingtutorials.datepicker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Tapping the date box opens a calendar. The picked date fills the box, formatted for the phone's language.
 * The box itself can't be typed into, so the date is always valid.
 */
public class MainActivity extends AppCompatActivity {
    private final Calendar selected = Calendar.getInstance();
    TextInputEditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        date = findViewById(R.id.date);
        date.setOnClickListener(v -> openPicker());
        if (savedInstanceState != null && savedInstanceState.containsKey("date")) {
            selected.setTimeInMillis(savedInstanceState.getLong("date"));
            showDate();
        }
    }

    private void openPicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            selected.set(year, month, day);
            showDate();
        }, selected.get(Calendar.YEAR), selected.get(Calendar.MONTH), selected.get(Calendar.DAY_OF_MONTH));
        // Only allow today or later, for example for a booking.
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void showDate() {
        // DateFormat writes the date the way this phone's language expects, e.g. 27 Oct 2026 or Oct 27, 2026.
        date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(selected.getTime()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (date.getText() != null && date.getText().length() > 0) outState.putLong("date", selected.getTimeInMillis());
    }
}
