package com.example.chesstimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

 // Keys:
    public static final String KEY_START_TIME_IN_SECONDS = "startTimeInSeconds";
    public static final String KEY_SAME_START_TIME = "startWithDifferentTime";
    public static final String KEY_P1_START_TIME = "p1StartTime";
    public static final String KEY_P2_START_TIME = "p2StartTime";
    public static final String KEY_SOUND_ON = "soundOn";
    public static final String KEY_VIBRATE_ON = "vibrateOn";
    public static final String KEY_MATCH_FINISH_SOUND = "finishSound";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_START_TIME_IN_MILLIS, "600000");
//        editor.apply();


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}