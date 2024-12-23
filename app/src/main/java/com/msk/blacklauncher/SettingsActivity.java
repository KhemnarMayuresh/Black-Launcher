package com.msk.blacklauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msk.blacklauncher.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Spinner fontSizeSpinner, fontStyleSpinner;
    private CheckBox hideApp1, hideApp2; // Example checkboxes for hiding apps
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize views
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        fontStyleSpinner = findViewById(R.id.fontStyleSpinner);
        hideApp1 = findViewById(R.id.hideApp1);
        hideApp2 = findViewById(R.id.hideApp2);
        saveButton = findViewById(R.id.saveButton);

        // Load current preferences
        loadPreferences();

        // Set up button to save preferences
        saveButton.setOnClickListener(v -> savePreferences());

        // Set up font size spinner
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_size_options,
                android.R.layout.simple_spinner_item
        );
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(fontSizeAdapter);

        // Set up font style spinner
        ArrayAdapter<CharSequence> fontStyleAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_style_options,
                android.R.layout.simple_spinner_item
        );
        fontStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontStyleSpinner.setAdapter(fontStyleAdapter);
    }

    private List<AppModel> getInstalledApps(Context context) {
        List<AppModel> appsList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // Get a list of all installed applications
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : applications) {
            // Filter for user-installed apps (non-system apps)
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                String appName = appInfo.loadLabel(packageManager).toString();
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String packageName = appInfo.packageName;

                appsList.add(new AppModel(appName, appIcon, packageName));
            }
        }
        return appsList;
    }

    private void loadPreferences() {
        // Load font size and style
        fontSizeSpinner.setSelection(preferences.getInt("fontSize", 0));
        fontStyleSpinner.setSelection(preferences.getInt("fontStyle", 0));

        // Load which apps are hidden
        hideApp1.setChecked(preferences.getBoolean("hideApp1", false));
        hideApp2.setChecked(preferences.getBoolean("hideApp2", false));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();

        // Save selected font size and style
        editor.putInt("fontSize", fontSizeSpinner.getSelectedItemPosition());
        editor.putInt("fontStyle", fontStyleSpinner.getSelectedItemPosition());

        // Save app visibility preferences
        editor.putBoolean("hideApp1", hideApp1.isChecked());
        editor.putBoolean("hideApp2", hideApp2.isChecked());

        editor.apply(); // Commit changes
        // Shoe confirmation message
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
    }
}
