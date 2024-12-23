package com.msk.blacklauncher.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.msk.blacklauncher.R;
import com.msk.blacklauncher.SettingsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView timeTextView, dateTextView;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Initialize views
        timeTextView = view.findViewById(R.id.timerText);
        dateTextView = view.findViewById(R.id.dateText);

        // Update the date and time
        updateDateTime();

        // Add long-press listener to open settings
        timeTextView.setOnLongClickListener(v -> {
            openSettings();
            return true;
        });
        return view;
    }

    private void updateDateTime() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Format date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                // Get current date and time
                Date now = new Date();
                String currentDate = dateFormat.format(now);
                String currentTime = timeFormat.format(now);

                // Update TextViews
                dateTextView.setText(currentDate);
                timeTextView.setText(currentTime);

                // Update every second
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void openSettings() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}