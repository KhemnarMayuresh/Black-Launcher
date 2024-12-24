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
    private LinearLayout hiddenAppsLayout, hiddenAppsLogoLayout, delayAppsLayout;
    private TextView hideAppsTextView, hideAppsLogoTextView, delayAppsTextView;

    private void initializeViews() {
        //----------------- Initialize views-----------------
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        fontStyleSpinner = findViewById(R.id.fontStyleSpinner);
        // -----------------Hide App components -----------------
        hiddenAppsLayout = findViewById(R.id.hiddenAppsLayout);
        hiddenAppsLayout.setVisibility(View.GONE); //Hide Apps default not visible
        hideAppsTextView = findViewById(R.id.hideAppsTextView);
        //----------------- Hide App Logo components -----------------
        hiddenAppsLogoLayout = findViewById(R.id.hiddenAppsLogoLayout);
        hiddenAppsLogoLayout.setVisibility(View.GONE); //Hide Apps Logo default not visible
        hideAppsLogoTextView = findViewById(R.id.hideAppsLogoTextView);
        //----------------- Delay App Logo components -----------------
        delayAppsLayout = findViewById(R.id.delayAppsLayout);
        delayAppsLayout.setVisibility(View.GONE); //Hide Apps Logo default not visible
        delayAppsTextView = findViewById(R.id.delayAppsTextView);

        //----------------- Default Launcher button -----------------
        Button setDefaultLauncherButton = findViewById(R.id.setDefaultLauncherButton);

        setDefaultLauncherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultLauncher();
            }
        });

        //----------------- Set up button to save preferences-----------------
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> savePreferences());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_settings);

        // Initialize views
        initializeViews();

        //----------------- Fetch and display apps dynamically-----------------
        List<AppModel> appsList = AppUtils.getInstalledApps(this, false);
        setupCheckboxes(appsList, hiddenAppsLayout, "isAppHidden");

        List<AppModel> VisibleAppsList = AppUtils.getInstalledApps(this, false);
        setupCheckboxes(VisibleAppsList, hiddenAppsLogoLayout, "isLogoVisible");
        setupCheckboxes(VisibleAppsList, delayAppsLayout, "isDelayApp");
        //----------------- Load current preferences-----------------
        loadPreferences();
        //----------------- Set up font size spinner-----------------
        setupSpinner(fontSizeSpinner, R.array.font_size_options);
        //----------------- Set up font style spinner-----------------
        setupSpinner(fontStyleSpinner, R.array.font_style_options);

        //----------------- Set up toggle for hidden apps -----------------
        setupToggle(hideAppsTextView, hiddenAppsLayout);
        //----------------- Set up toggle for hidden apps Logo -----------------
        setupToggle(hideAppsLogoTextView, hiddenAppsLogoLayout);
        //----------------- Set up toggle for hidden apps Logo -----------------
        setupToggle(delayAppsTextView, delayAppsLayout);
    }

    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                arrayResource,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupToggle(TextView textView, LinearLayout layout) {
        textView.setOnClickListener(v -> {
            boolean isVisible = layout.getVisibility() == View.VISIBLE;
            layout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, isVisible ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up, 0
            );
        });
    }

    private void setupCheckboxes(List<AppModel> appsList, LinearLayout layout, String preferenceKeyPrefix) {
        for (AppModel app : appsList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(app.getAppName());
            checkBox.setChecked(preferences.getBoolean(preferenceKeyPrefix + ":" + app.getPackageName(), false));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    saveAppHiddenState(preferenceKeyPrefix + ":" + app.getPackageName(), isChecked)
            );

            layout.addView(checkBox);
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
