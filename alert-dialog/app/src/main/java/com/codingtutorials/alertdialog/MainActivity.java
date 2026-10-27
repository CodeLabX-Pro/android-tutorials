package com.codingtutorials.alertdialog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Three common dialogs: a Yes/No confirmation, a single-choice list, and "Exit the app?" on Back.
 * MaterialAlertDialogBuilder gives them the Material 3 look.
 */
public class MainActivity extends AppCompatActivity {
    private static final String[] COLOURS = {"Purple", "Coral", "Sky blue", "Mint"};
    private int chosenColour = 0;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        result = findViewById(R.id.textResult);

        Button confirm = findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setIcon(R.drawable.ic_warning)
                .setNegativeButton(R.string.cancel, (dialog, which) -> result.setText(R.string.result_kept))
                .setPositiveButton(R.string.delete, (dialog, which) -> result.setText(R.string.result_deleted))
                .show());

        Button choose = findViewById(R.id.btnChoose);
        choose.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.choose_title)
                // The radio list remembers the current choice; OK confirms it.
                .setSingleChoiceItems(COLOURS, chosenColour, (dialog, which) -> chosenColour = which)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, (dialog, which) -> result.setText(getString(R.string.result_colour, COLOURS[chosenColour])))
                .show());

        // Ask before leaving the app with the Back button.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle(R.string.exit_title)
                        .setMessage(R.string.exit_message)
                        .setCancelable(true)
                        .setNegativeButton(R.string.stay, null)
                        .setPositiveButton(R.string.exit, (dialog, which) -> finish())
                        .show();
            }
        });
    }
}
