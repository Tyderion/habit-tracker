package ch.isageek.tyderion.habittracker.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;

/**
 * Created by tzhnaga1 on 23/04/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            String settings = getArguments().getString("settings");
            if ("notifications".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_wifi);
            } else if ("sync".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_sync);
            }
        }
    }
}
