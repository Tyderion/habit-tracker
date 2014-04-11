package ch.isageek.tyderion.habittracker.habit;

import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import ch.isageek.tyderion.habittracker.EditHabit;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.DaoMaster;
import ch.isageek.tyderion.habittracker.model.DaoSession;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurence;
import ch.isageek.tyderion.habittracker.model.OccurenceDao;


/**
 * An activity representing a list of Habits. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HabitDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link HabitListFragment} and the item details
 * (if present) is a {@link HabitDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link HabitListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class HabitListActivity extends FragmentActivity
        implements HabitListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private HabitListFragment mfragment;

    private static void generateData(Context context) {
        DaoMaster.DevOpenHelper helper = Database.getDevOpenHelper(context);
        if (context.getResources().getBoolean(R.bool.resetDatabase)) {
            DaoMaster.dropAllTables(helper.getWritableDatabase(), true);
            DaoMaster.createAllTables(helper.getWritableDatabase(), true);
            DaoSession session = new DaoMaster(helper.getWritableDatabase()).newSession();
            HabitDao habit = session.getHabitDao();
            OccurenceDao occdao = session.getOccurenceDao();
            long nowmilis = new Date().getTime();
            long millisInADay = 24*60*60*1000;
            int maxOccurences = 500;
            for (int i = 0; i < 20; i++) {
                Habit curHabit = new Habit(null, new Date(nowmilis-(maxOccurences+1)*millisInADay), "Habit " + i, i % 2 == 0, "Description for Habit " + i);
                habit.insert(curHabit);
                for (int j = 0; j < maxOccurences; j++) {
                    Occurence occ = new Occurence(null);
                    occ.setHabit(curHabit);
                    Date date = new Date(nowmilis-j*millisInADay);
                    occ.setDate(date);
                    occdao.insert(occ);
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        generateData(this);

        if (findViewById(R.id.habit_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.

            getFragment().setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }


    private HabitListFragment getFragment() {
        if (mfragment == null) {
            mfragment = (HabitListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.habit_list);
        }
        return mfragment;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    getFragment().filter(s);
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_habit_add:
                this.showEditHabit();
                Toast.makeText(this, "Add Habit", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditHabit() {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
//            arguments.putLong(EditHabit.ARG_HABIT_ID, id);
//            arguments.putString(EditHabit.ARG_HABIT_NAME, "");
            EditHabit fragment = new EditHabit();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.habit_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EditHabitActivity.class);
//            detailIntent.putExtra(EditHabit.ARG_HABIT_ID, id);
//            detailIntent.putExtra(EditHabit.ARG_HABIT_NAME, "");
            startActivity(detailIntent);
        }
    }



    /**
     * Callback method from {@link HabitListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Habit habit) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(HabitDetailFragment.ARG_ITEM_ID, habit.getId());
            arguments.putString(HabitDetailFragment.ARG_ITEM_NAME, habit.getName());
            HabitDetailFragment fragment = new HabitDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.habit_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, HabitDetailActivity.class);
            detailIntent.putExtra(HabitDetailFragment.ARG_ITEM_ID, habit.getId());
            detailIntent.putExtra(HabitDetailFragment.ARG_ITEM_NAME, habit.getName());
            startActivity(detailIntent);
        }
    }
}
