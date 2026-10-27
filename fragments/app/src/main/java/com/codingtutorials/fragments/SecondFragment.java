package com.codingtutorials.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** This fragment also reacts to taps: onViewCreated is where you find views and add listeners. */
public class SecondFragment extends Fragment {
    private int taps = 0;

    public SecondFragment() {
        super(R.layout.fragment_second);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView counter = view.findViewById(R.id.counter);
        Button tap = view.findViewById(R.id.btnTap);
        tap.setOnClickListener(v -> {
            taps++;
            counter.setText(getResources().getQuantityString(R.plurals.taps, taps, taps));
        });
    }
}
