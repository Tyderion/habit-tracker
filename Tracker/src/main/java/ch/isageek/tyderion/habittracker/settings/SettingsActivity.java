package ch.isageek.tyderion.habittracker.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;

/**
 * Created by tzhnaga1 on 23/04/14.
 */
public class SettingsActivity extends PreferenceActivity {

    static boolean showingSubScreen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    @Override
         public boolean onOptionsItemSelected(MenuItem item) {
        if (!showingSubScreen && item.getItemId() == android.R.id.home) {
            Intent intent = getParentActivityIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {


        public SettingsFragment() {}
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            showingSubScreen = true;
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);
            String settings = getArguments().getString("settings");
            if ("notifications".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_wifi);
            } else if ("sync".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_sync);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(getActivity(), intent);
                showingSubScreen = false;
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
