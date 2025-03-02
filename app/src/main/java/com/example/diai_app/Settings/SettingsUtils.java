package com.example.diai_app.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtils {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static void setDarkMode(Context context, boolean isEnabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_DARK_MODE, isEnabled);
        editor.apply();
    }

    public static boolean isDarkModeEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false); // Mặc định là Light Mode
    }
}
