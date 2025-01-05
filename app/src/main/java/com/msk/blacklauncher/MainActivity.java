package com.msk.blacklauncher;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.msk.blacklauncher.adapters.ViewPagerAdapter;
import com.msk.blacklauncher.fragments.AppsFragment;
import com.msk.blacklauncher.fragments.ChecklistAndNotesFragment;
import com.msk.blacklauncher.fragments.HomeFragment;
import com.msk.blacklauncher.model.AppModel;
import com.msk.blacklauncher.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(this);

        //----------------- Fetch installed apps and set adapter-----------------
        List<AppModel> appsList = AppUtils.getInstalledApps(getApplicationContext(), true);
        setupViewPager(viewPager, adapter, appsList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //----------------- In MainActivity or the fragment manager for handling the swipe -----------------
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //----------------- Fetch installed apps and set adapter -----------------
        List<AppModel> appsList = AppUtils.getInstalledApps(getApplicationContext(), true);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("apps_list", new ArrayList<>(appsList));

        AppsFragment appsFragment = new AppsFragment();
        appsFragment.setArguments(bundle);
        //----------------- overwrite app fragment -----------------
        adapter.updateFragments(2, appsFragment);

        viewPager.setAdapter(adapter);

        //----------------- In MainActivity or the fragment manager for handling the swipe -----------------
        viewPager.setCurrentItem(1);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //----------------- In MainActivity on backpress do nothing -----------------
    }

    private void setupViewPager(ViewPager2 viewPager, ViewPagerAdapter adapter, List<AppModel> appsList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("apps_list", new ArrayList<>(appsList));

        ChecklistAndNotesFragment notesFragment = new ChecklistAndNotesFragment();
        HomeFragment homeFragment = new HomeFragment();
        AppsFragment appsFragment = new AppsFragment();
        appsFragment.setArguments(bundle);

        adapter.addFragment(notesFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(appsFragment);

        viewPager.setAdapter(adapter);
    }

}