package ch.isageek.tyderion.habittracker.habit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ch.isageek.tyderion.habittracker.EditHabit;
import ch.isageek.tyderion.habittracker.R;


/**
 * An activity representing a single Habit detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HabitListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link HabitDetailFragment}.
 */
public class EditHabitActivity extends FragmentActivity implements EditHabit.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putLong(EditHabit.ARG_HABIT_ID,getIntent().getLongExtra(EditHabit.ARG_HABIT_ID, 0));
//            arguments.putString(EditHabit.ARG_HABIT_NAME,getIntent().getStringExtra(EditHabit.ARG_HABIT_NAME));
//            EditHabit fragment = new EditHabit();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.edit_fragment, fragment)
//                    .commit();
        }
    }

    private EditHabit mfragment;

    private EditHabit getFragment() {
        if (mfragment == null) {
            mfragment = (EditHabit) getSupportFragmentManager()
                    .findFragmentById(R.id.edit_fragment);
        }
        return mfragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, HabitListActivity.class));
            return true;
        }
        if (id == R.id.save_habit) {
            this.getFragment().save();
            NavUtils.navigateUpTo(this, new Intent(this, HabitListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_habit, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
