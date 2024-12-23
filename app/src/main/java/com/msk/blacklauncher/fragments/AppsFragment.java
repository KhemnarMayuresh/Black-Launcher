package com.msk.blacklauncher.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msk.blacklauncher.model.AppModel;
import com.msk.blacklauncher.R;
import com.msk.blacklauncher.adapters.AppsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppsAdapter adapter;
    private EditText searchBar;
    private List<AppModel> appsList = new ArrayList<>();
    private List<AppModel> filteredAppsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apps, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchBar = view.findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the apps list from arguments
        Bundle args = getArguments();
        if (args != null) {
            appsList = args.getParcelableArrayList("apps_list");
            if (appsList != null) {
                adapter = new AppsAdapter(appsList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }

        // Set up a TextWatcher for the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter apps list as the user types
                filterApps(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });

        return view;
    }

    // Filter the apps list based on the search query
    private void filterApps(String query) {
        filteredAppsList.clear();
        if (query.isEmpty()) {
            // If the query is empty, show all apps
            filteredAppsList.addAll(appsList);
        } else {
            // Otherwise, filter the apps list
            for (AppModel app : appsList) {
                if (app.getAppName().toLowerCase().contains(query.toLowerCase())) {
                    filteredAppsList.add(app);
                }
            }
        }
        // Notify the adapter that the list has been updated
        adapter.notifyDataSetChanged();
    }
}
