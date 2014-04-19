package ch.isageek.tyderion.habittracker.item;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends FragmentActivity implements ItemDetailFragment.Callbacks{

    public final static String ARG_POTENTIAL_HABIT_EDIT = "potential_habit_edit";
    private ItemDetailFragment detailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_HABIT,
                    getIntent().getParcelableExtra(ItemDetailFragment.ARG_HABIT));
            detailFragment = new ItemDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, detailFragment)
                    .commit();
        } else {
            detailFragment = (ItemDetailFragment)getSupportFragmentManager().findFragmentById(R.id.item_detail_container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_POTENTIAL_HABIT_EDIT,detailFragment.getHabit() );
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishedHabitEdit(Habit habit) {
    }
}
