package com.msk.blacklauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// import fragments
import androidx.viewpager2.widget.ViewPager2;

import com.msk.blacklauncher.adapters.ViewPagerAdapter;
import com.msk.blacklauncher.fragments.AppsFragment;
import com.msk.blacklauncher.fragments.ChecklistAndNotesFragment;
import com.msk.blacklauncher.fragments.HomeFragment;
import com.msk.blacklauncher.model.AppModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView timeTextView, dateTextView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        // Fetch installed apps and set adapter
        List<AppModel> appsList = getInstalledApps(getApplicationContext());
        setupViewPager(viewPager, adapter, appsList);
        // Log the app names
        StringBuilder appNames = new StringBuilder("Installed Apps: ");
        for (AppModel app : appsList) {
            appNames.append(app.getAppName()).append(", ");
        }
        Log.i("AppsFragment", appNames.toString());

        adapter.addFragment(new ChecklistAndNotesFragment()); // Add apps list fragment here
        adapter.addFragment(new HomeFragment()); // Add your home fragment here
        adapter.addFragment(new AppsFragment()); // Add apps list fragment here

        viewPager.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // In MainActivity or the fragment manager for handling the swipe
        viewPager.setCurrentItem(1); // Or use a gesture detector to trigger this fragment

        // Replace "custom_font.ttf" with the actual font file in the assets folder
//        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/caveat.ttf");
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

    private List<AppModel> getInstalledApps(Context context) {
        List<AppModel> appsList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> hiddenAppsList= preferences.getStringSet("hiddenAppsList",null);

        // Get a list of all installed applications
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : applications) {
            // Filter for user-installed apps (non-system apps)
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 ||
                (appInfo.loadLabel(packageManager).toString().equals("Settings"))) {
                String appName = appInfo.loadLabel(packageManager).toString();
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String packageName = appInfo.packageName;

                appsList.add(new AppModel(appName, appIcon, packageName));
            }
        }

        // Sort the apps list alphabetically by app name
        appsList.sort((app1, app2) -> app1.getAppName().compareToIgnoreCase(app2.getAppName()));

        return appsList;
    }
}