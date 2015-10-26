package com.example.dell.splitthebill;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dell on 10/26/2015.
 */
public class UserSettings extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
    }
}
