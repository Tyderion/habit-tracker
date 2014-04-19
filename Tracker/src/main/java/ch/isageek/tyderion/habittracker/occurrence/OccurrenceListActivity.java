package ch.isageek.tyderion.habittracker.occurrence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;

public class OccurrenceListActivity extends FragmentActivity {

    private OccurrenceListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_list);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(OccurrenceListFragment.ARG_HABIT, getIntent().getParcelableExtra(OccurrenceListFragment.ARG_HABIT));
            this.mListFragment = new OccurrenceListFragment();
            mListFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.occurrence_list_fragment, mListFragment)
                    .commit();
        } else {
            if (getIntent() != null) {
                Habit habit = getIntent().getParcelableExtra(OccurrenceListFragment.ARG_HABIT);
                OccurrenceListFragment fragment = (OccurrenceListFragment)getSupportFragmentManager().findFragmentById(R.id.occurrence_list_fragment);
                fragment.setHabit(habit);
            }
        }
    }
}
