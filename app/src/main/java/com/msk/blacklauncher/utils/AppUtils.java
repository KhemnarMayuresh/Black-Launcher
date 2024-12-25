package com.msk.blacklauncher.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.msk.blacklauncher.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    @SuppressLint("NewApi")
    public static List<AppModel> getInstalledApps(Context context, boolean excludeHidden) {
        //----------------- Combine logic from MainActivity and SettingsActivity -----------------
        List<AppModel> appsList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //----------------- Get a list of all installed applications -----------------
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : applications) {
            //----------------- Filter for user-installed apps (non-system apps) -----------------
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                if (excludeHidden && preferences.getBoolean("isAppHidden:" + appInfo.packageName, false)) {
                    continue;
                }

                String appName = appInfo.loadLabel(packageManager).toString();
                String packageName = appInfo.packageName;
                Drawable appIcon = appInfo.loadIcon(packageManager);

                appsList.add(
                        new AppModel(
                                appName,
                                appIcon,
                                packageName,
                                preferences.getBoolean("isLogoVisible:" + appInfo.packageName, false),
                                preferences.getBoolean("isDelayApp:" + appInfo.packageName, false)
                        )
                );
            }
        }

        //----------------- Sort the apps list alphabetically by app name -----------------
        appsList.sort((app1, app2) -> app1.getAppName().compareToIgnoreCase(app2.getAppName()));

        return appsList;
    }
}
