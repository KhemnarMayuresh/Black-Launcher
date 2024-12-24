package com.msk.blacklauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msk.blacklauncher.model.AppModel;
import com.msk.blacklauncher.utils.AppUtils;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Spinner fontSizeSpinner, fontStyleSpinner;
    private LinearLayout hiddenAppsLayout, hiddenAppsLogoLayout;
    private TextView hideAppsTextView, hideAppsLogoTextView;
    private boolean isHiddenAppsVisible = false, isHiddenAppsLogoVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_settings);


        //----------------- Initialize views-----------------
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        fontStyleSpinner = findViewById(R.id.fontStyleSpinner);
        Button saveButton = findViewById(R.id.saveButton);
        // -----------------Hide App components -----------------
        hiddenAppsLayout = findViewById(R.id.hiddenAppsLayout);
        hiddenAppsLayout.setVisibility(View.GONE); //Hide Apps default not visible
        hideAppsTextView = findViewById(R.id.hideAppsTextView);
        //----------------- Hide App Logo components -----------------
        hiddenAppsLogoLayout = findViewById(R.id.hiddenAppsLogoLayout);
        hiddenAppsLogoLayout.setVisibility(View.GONE); //Hide Apps Logo default not visible
        hideAppsLogoTextView = findViewById(R.id.hideAppsLogoTextView);
        //----------------- Fetch and display apps dynamically-----------------
        List<AppModel> appsList = AppUtils.getInstalledApps(this, false);
        displayAppCheckboxes(appsList);
        displayAppLogosCheckboxes(appsList);

        //----------------- Load current preferences-----------------
        loadPreferences();

        //----------------- Default Launcher button -----------------
        Button setDefaultLauncherButton = findViewById(R.id.setDefaultLauncherButton);

        setDefaultLauncherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultLauncher();
            }
        });
        //----------------- Set up button to save preferences-----------------
        saveButton.setOnClickListener(v -> savePreferences());

        //----------------- Set up font size spinner-----------------
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_size_options,
                android.R.layout.simple_spinner_item
        );
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(fontSizeAdapter);

        //----------------- Set up font style spinner-----------------
        ArrayAdapter<CharSequence> fontStyleAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_style_options,
                android.R.layout.simple_spinner_item
        );
        fontStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontStyleSpinner.setAdapter(fontStyleAdapter);

        //----------------- Set up toggle for hidden apps -----------------
        hideAppsTextView.setOnClickListener(v -> toggleHiddenAppsSection());
        //----------------- Set up toggle for hidden apps Logo -----------------
        hideAppsLogoTextView.setOnClickListener(v -> toggleHiddenAppsLogoSection());
    }

    private void toggleHiddenAppsSection() {
        isHiddenAppsVisible = !isHiddenAppsVisible;
        hiddenAppsLayout.setVisibility(isHiddenAppsVisible ? View.VISIBLE : View.GONE);
//        hideAppsTextView.setCompoundDrawablesWithIntrinsicBounds(
//                0, 0, isHiddenAppsVisible ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down, 0
//        );
    }

    private void toggleHiddenAppsLogoSection() {
        isHiddenAppsLogoVisible = !isHiddenAppsLogoVisible;
        hiddenAppsLogoLayout.setVisibility(isHiddenAppsLogoVisible ? View.VISIBLE : View.GONE);
//        hideAppsLogoTextView.setCompoundDrawablesWithIntrinsicBounds(
//                0, 0, isHiddenAppsLogoVisible ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down, 0
//        );
    }

    private void displayAppCheckboxes(List<AppModel> appsList) {
        for (AppModel app : appsList) {
            //----------------- Create a checkbox for each app -----------------
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(app.getAppName());
            checkBox.setChecked(preferences.getBoolean("isAppHidden:" + app.getPackageName(), false)); // Load saved state

            // -----------------Set a listener to save state in SharedPreferences -----------------
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                saveAppHiddenState("isAppHidden:" + app.getPackageName(), isChecked);
            });

            hiddenAppsLayout.addView(checkBox);
        }
    }

    private void displayAppLogosCheckboxes(List<AppModel> appsList) {
        for (AppModel app : appsList) {
            //----------------- Create a checkbox for each app -----------------
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(app.getAppName());
            checkBox.setChecked(preferences.getBoolean("isLogoVisible:" + app.getPackageName(), false)); // Load saved state

            //----------------- Set a listener to save state in SharedPreferences -----------------
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                saveAppHiddenState("isLogoVisible:" + app.getPackageName(), isChecked);
            });

            hiddenAppsLogoLayout.addView(checkBox);
        }
    }

    private void saveAppHiddenState(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void loadPreferences() {
        // Load font size and style
        fontSizeSpinner.setSelection(preferences.getInt("fontSize", 0));
        fontStyleSpinner.setSelection(preferences.getInt("fontStyle", 0));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();

        //----------------- Save selected font size and style -----------------
        editor.putInt("fontSize", fontSizeSpinner.getSelectedItemPosition());
        editor.putInt("fontStyle", fontStyleSpinner.getSelectedItemPosition());

        editor.apply(); // Commit changes
        //----------------- Shoe confirmation message -----------------
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
    }

    private void setDefaultLauncher() {
        try {
            //----------------- Create an Intent to open the settings page where user can select default apps -----------------
            Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(SettingsActivity.this, "Error: Unable to open settings", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
