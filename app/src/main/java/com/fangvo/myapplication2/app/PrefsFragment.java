package com.fangvo.myapplication2.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.fangvo.myapplication2.app.R;

//окно настроек

public class PrefsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }

}
