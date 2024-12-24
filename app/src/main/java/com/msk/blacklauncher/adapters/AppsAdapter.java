package com.msk.blacklauncher.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        holder.itemView.setOnClickListener(v -> {
            //----------------- Launch app on click -----------------
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            if (launchIntent != null) {
                context.startActivity(launchIntent);
            }
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