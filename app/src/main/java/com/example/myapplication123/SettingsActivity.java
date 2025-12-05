package com.example.myapplication123;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class SettingsActivity extends BaseActivity {

    private static final String PREFS_NAME = "NasaPrefs";
    private static final String PREF_SHOW_TIPS = "show_tips";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.menu_settings));
        setContentLayout(R.layout.activity_settings);

        CheckBox checkShowTips = findViewById(R.id.checkShowTips);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean showTips = prefs.getBoolean(PREF_SHOW_TIPS, true);
        checkShowTips.setChecked(showTips);

        checkShowTips.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(PREF_SHOW_TIPS, isChecked).apply());
    }

    @Override
    protected void showHelpDialog() {
        showSimpleHelp(getString(R.string.help_settings));
    }
}
