package com.msk.blacklauncher.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msk.blacklauncher.R;
import com.msk.blacklauncher.adapters.ChecklistAdapter;

import java.util.ArrayList;

public class ChecklistAndNotesFragment extends Fragment {

    private RecyclerView checklistRecyclerView;
    private ChecklistAdapter checklistAdapter;
    private EditText notesEditText;
    private ImageButton addChecklistItemButton;

    private ArrayList<String> checklistItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_and_notes, container, false);

        // Initialize UI components
        checklistRecyclerView = view.findViewById(R.id.checklistRecyclerView);
        notesEditText = view.findViewById(R.id.notesEditText);
        addChecklistItemButton = view.findViewById(R.id.addChecklistItemButton);

        checklistItems = new ArrayList<>();

        // Set up the RecyclerView for the checklist
        checklistAdapter = new ChecklistAdapter(checklistItems);
        checklistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checklistRecyclerView.setAdapter(checklistAdapter);

        // Add new item to the checklist
        addChecklistItemButton.setOnClickListener(v -> {
            String newItem = notesEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newItem)) {
                checklistItems.add(newItem);
                checklistAdapter.notifyItemInserted(checklistItems.size() - 1);
                notesEditText.setText(""); // Clear the input field
            }
        });

        return view;
    }
}
