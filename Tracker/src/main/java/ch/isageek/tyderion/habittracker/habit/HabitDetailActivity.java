package ch.isageek.tyderion.habittracker.habit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import ch.isageek.tyderion.habittracker.EditHabitFragment;
import ch.isageek.tyderion.habittracker.OccurrencesFragment;
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
public class HabitDetailActivity extends FragmentActivity {
    private HabitDetailFragment detailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

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
            Bundle arguments = new Bundle();
            arguments.putLong(HabitDetailFragment.ARG_ITEM_ID,getIntent().getLongExtra(HabitDetailFragment.ARG_ITEM_ID, 0));
            arguments.putString(HabitDetailFragment.ARG_ITEM_NAME, getIntent().getStringExtra(HabitDetailFragment.ARG_ITEM_NAME));
            this.detailFragment = new HabitDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.habit_detail_container, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
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
        if (id == R.id.detail_save_habit) {
            detailFragment.toggleEditing();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDetails(View view) {
        detailFragment.showDetails(view);
    }



}
