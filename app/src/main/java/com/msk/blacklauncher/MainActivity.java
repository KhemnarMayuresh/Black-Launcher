package com.msk.blacklauncher;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView timeTextView, dateTextView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Initialize views
        timeTextView = findViewById(R.id.timerText);
        dateTextView = findViewById(R.id.dateText);

        // Update the date and time
        updateDateTime();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

    //  On the left swipe of home screen shows an activity where we can create 2 section. The first section is the first half of the screen with a checklist of some text items and second section is the lower half of screen with notes section. where user can enter any task and note using keyboard.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Remove callbacks to avoid memory leaks
    }
}