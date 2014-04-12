package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.os.Bundle;

import ch.isageek.tyderion.habittracker.R;

public class OccurrenceListActivity extends Activity {

    private OccurrenceListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(OccurrenceListFragment.ARG_HABIT_ID, getIntent().getLongExtra(OccurrenceListFragment.ARG_HABIT_ID, 0));
            this.mListFragment = new OccurrenceListFragment();
            mListFragment.setArguments(arguments);
            getFragmentManager().beginTransaction().add(R.id.occurrence_list_fragment, mListFragment)
                    .commit();
        }
    }
}
