package com.app.smarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.smarthome.util.Constants;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class Session implements Constants {

    private static final String NAME = Session.class.getSimpleName()+ " ";
    private static final String TAG = COMMON_TAG;

    private static SharedPreferences sharedPreferences;

    public Session(Context context) {
        sharedPreferences = getDefaultSharedPreferences(context);
    }

    public void putData(String key, String value) {
        Log.i(TAG, NAME + "putData: called");
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getData(String key) {
        Log.i(TAG, NAME + "getData: called");
        return sharedPreferences.getString(key, SHARED_PREFERENCE_VALUE_ERROR);
    }

    public void clearData() {
        Log.i(TAG, NAME + "clearData: called");
        sharedPreferences.edit().clear().apply();
    }

}
