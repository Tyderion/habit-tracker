package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import ch.isageek.tyderion.habittracker.R;

public class OccurrenceListActivity extends Activity {

    private OccurenceListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(OccurenceListFragment.ARG_HABIT_ID, getIntent().getLongExtra(OccurenceListFragment.ARG_HABIT_ID, 0));
            this.mListFragment = new OccurenceListFragment();
            mListFragment.setArguments(arguments);
            getFragmentManager().beginTransaction().add(R.id.occurrence_list_fragment, mListFragment)
                    .commit();
        }
    }
}
