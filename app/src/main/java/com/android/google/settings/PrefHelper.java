package com.android.google.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {

    public static final String KEY_EMAIL = "email";
    private Context context;

    private static PrefHelper instance;
    private final SharedPreferences sharedPref;

    private PrefHelper(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    static PrefHelper getInstance(Context context) {

        if (instance == null) {
            instance = new PrefHelper(context);
        } else {
            instance.setContext(context);
        }

        return instance;
    }

    String getString(String key) {
        return sharedPref.getString(key, null);
    }


    void putString(String key, String value) {
        sharedPref.edit()
                .putString(key, value)
                .apply();
    }
}
