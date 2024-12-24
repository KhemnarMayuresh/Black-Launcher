package com.msk.blacklauncher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msk.blacklauncher.R;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private final ArrayList<String> checklistItems;

    public ChecklistAdapter(ArrayList<String> checklistItems) {
        this.checklistItems = checklistItems;
    }

    @Override
    public ChecklistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChecklistViewHolder holder, int position) {
        String item = checklistItems.get(position);
        holder.checklistItemText.setText(item);

        //----------------- Handle item deletion -----------------
        holder.deleteButton.setOnClickListener(v -> {
            checklistItems.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        TextView checklistItemText;
        ImageButton deleteButton;

        public ChecklistViewHolder(View itemView) {
            super(itemView);
            checklistItemText = itemView.findViewById(R.id.checklistItemText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
