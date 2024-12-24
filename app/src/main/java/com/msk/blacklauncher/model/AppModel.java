package com.msk.blacklauncher.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppModel implements Parcelable {
    public static final Creator<AppModel> CREATOR = new Creator<AppModel>() {
        @Override
        public AppModel createFromParcel(Parcel in) {
            return new AppModel(in);
        }

        @Override
        public AppModel[] newArray(int size) {
            return new AppModel[size];
        }
    };
    private final String appName;
    private final Drawable appIcon;
    private final String packageName;
    private boolean isIconVisible;
    private boolean isDelay;

    public AppModel(String appName, Drawable appIcon, String packageName, boolean isIconVisible, boolean isDelay) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.isIconVisible = isIconVisible;
        this.isDelay = isDelay;
    }

    //----------------- Parcelable implementation -----------------
    protected AppModel(Parcel in) {
        appName = in.readString();
        packageName = in.readString();
        appIcon = null; // Drawable cannot be passed; handle this separately if needed.
    }

    //----------------- Getter methods -----------------
    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isIconVisible() {
        return isIconVisible;
    }

    //----------------- Setter Methods -----------------
    public void setIconVisible(boolean iconVisible) {
        isIconVisible = iconVisible;
    }

    public boolean isDelay() {
        return isDelay;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(packageName);
        //----------------- Drawable not written due to its complex nature. -----------------
    }

    @Override
    public int describeContents() {
        return 0;
    }
}