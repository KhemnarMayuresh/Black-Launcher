package com.msk.blacklauncher.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msk.blacklauncher.R;
import com.msk.blacklauncher.model.AppModel;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private final List<AppModel> appsList;
    private final Context context;

    public AppsAdapter(List<AppModel> appsList, Context context) {
        this.appsList = appsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppModel app = appsList.get(position);

        holder.appName.setText(app.getAppName());

        //----------------- Conditionally show/hide app icon -----------------
        if (!app.isIconVisible()) {
            holder.appIcon.setVisibility(View.VISIBLE);
            holder.appIcon.setImageDrawable(app.getAppIcon());
        } else {
            holder.appIcon.setVisibility(View.GONE);
        }

        //----------------- Launch app on click -----------------
        boolean lock = app.isDelay(); // lock for delay in opening app
        holder.itemView.setOnClickListener(v -> {
            if (!lock) { // Launch the app if lock is false
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                if (launchIntent != null) {
                    context.startActivity(launchIntent);
                }
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (lock) {
                Toast.makeText(context, "Hold for 20 seconds to unlock. Don't waste time!", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                boolean[] appOpened = {false}; // Flag to track if app has been opened

                Runnable runnable = new Runnable() {
                    private int secondsElapsed = 0;

                    @Override
                    public void run() {
                        secondsElapsed++;
                        if (secondsElapsed == 10) {
                            // Show toast at 10 seconds
                            Toast.makeText(context, "Are you sure you are not wasting time?", Toast.LENGTH_SHORT).show();
                        } else if (secondsElapsed == 20) {
                            // Launch app after 20 seconds
                            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                            if (launchIntent != null) {
                                context.startActivity(launchIntent);
                            }
                            appOpened[0] = true; // Mark app as opened
                            return; // Exit runnable
                        }
                        // Continue checking every second
                        handler.postDelayed(this, 1000);
                    }
                };

                // Start the timer
                handler.postDelayed(runnable, 1000);

                // Cancel if user releases the long click before 20 seconds
                holder.itemView.setOnTouchListener((view, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        if (!appOpened[0]) { // Only show toast if the app has not been opened
                            handler.removeCallbacks(runnable);
                            Toast.makeText(context, "App unlocking canceled", Toast.LENGTH_SHORT).show();
                        }
                        view.setOnTouchListener(null); // Remove listener to prevent interference
                        return true;
                    }
                    return false;
                });
            }
            return true; // Return true to consume the long-click event
        });

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
        }
    }
}