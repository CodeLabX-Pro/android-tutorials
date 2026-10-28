package com.example.buttons;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

/**
 * Everything about buttons on one screen: handling taps, the Material 3 styles, icons,
 * custom shapes and colours, turning a button on and off, and a group of toggle buttons.
 */
public class MainActivity extends AppCompatActivity {
    private int taps = 0;
    private boolean liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        View root = findViewById(R.id.main);

        // 1. The basic button: count taps, and reset with a long press.
        Button tapMe = findViewById(R.id.btnTap);
        TextView tapCount = findViewById(R.id.textTapCount);
        tapMe.setOnClickListener(v -> {
            taps++;
            tapCount.setText(getResources().getQuantityString(R.plurals.taps, taps, taps));
        });
        tapMe.setOnLongClickListener(v -> {
            taps = 0;
            tapCount.setText(R.string.tap_hint);
            return true; // true means "handled", so a normal click doesn't follow.
        });

        // 2. Five styles, one listener: the tapped button tells us which one it was.
        View.OnClickListener showStyle = v -> Snackbar.make(root, getString(R.string.you_tapped, ((Button) v).getText()), Snackbar.LENGTH_SHORT).show();
        for (int id : new int[]{R.id.btnFilled, R.id.btnTonal, R.id.btnElevated, R.id.btnOutlined, R.id.btnText}) {
            findViewById(id).setOnClickListener(showStyle);
        }

        // 3. An icon that changes when tapped.
        MaterialButton like = findViewById(R.id.btnLike);
        like.setOnClickListener(v -> {
            liked = !liked;
            like.setIconResource(liked ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
            like.setText(liked ? R.string.liked : R.string.like);
        });

        // 4. A custom pill button.
        findViewById(R.id.btnPill).setOnClickListener(v -> Snackbar.make(root, R.string.pill_tapped, Snackbar.LENGTH_SHORT).show());

        // 5. A button that stays off until the box is ticked.
        Button next = findViewById(R.id.btnContinue);
        CheckBox agree = findViewById(R.id.checkAgree);
        agree.setOnCheckedChangeListener((box, checked) -> next.setEnabled(checked));
        next.setOnClickListener(v -> Snackbar.make(root, R.string.continued, Snackbar.LENGTH_SHORT).show());

        // 6. A toggle group: like tabs, only one button is selected at a time.
        MaterialButtonToggleGroup range = findViewById(R.id.groupRange);
        TextView rangeText = findViewById(R.id.textRange);
        range.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            rangeText.setText(getString(R.string.showing, ((Button) findViewById(checkedId)).getText()));
        });
    }
}
