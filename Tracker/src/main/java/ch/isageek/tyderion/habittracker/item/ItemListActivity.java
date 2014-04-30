package ch.isageek.tyderion.habittracker.item;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.DataExportActivity;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.settings.SettingsActivity;

public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks, ItemDetailFragment.Callbacks {

    private static int REQUEST_CODE_NEW_HABIT_TAG = 0;

    private ItemListFragment mFragment;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mFragment = (ItemListFragment) getFragmentManager()
                .findFragmentById(R.id.item_list);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            mFragment.setActivateOnItemClick(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(ItemDetailActivity.ARG_POTENTIAL_HABIT_EDIT)){
                mFragment.updateHabit((Habit)extras.getParcelable(ItemDetailActivity.ARG_POTENTIAL_HABIT_EDIT));
            }
        }
    }

    @Override
    public void onItemSelected(Habit habit) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_HABIT, habit);
            arguments.putBoolean(ItemDetailFragment.ARG_DUALPANE, mTwoPane);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_HABIT, habit);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        if (mTwoPane) {
            menu.findItem(R.id.action_item_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == REQUEST_CODE_NEW_HABIT_TAG) {
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.containsKey(AddItemFragment.ARG_HABIT)) {
                Habit h = bundle.getParcelable(AddItemFragment.ARG_HABIT);
                mFragment.addHabit(h);
            }
        }
        if (data != null && requestCode == DataExportActivity.REQUEST_CODE_EXPORT_IMPORT) {
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.containsKey(DataExportActivity.ARG_DID_IMPORT)) {
               mFragment.mAdapter.reload();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_item_add:
                Intent detailIntent = new Intent(this, AddItemActivity.class);
                startActivityForResult(detailIntent, REQUEST_CODE_NEW_HABIT_TAG);
                break;
            case R.id.action_settings:
                SettingsActivity.start(this);
                break;
            case R.id.action_export_data:
                startActivityForResult(new Intent(this, DataExportActivity.class),DataExportActivity.REQUEST_CODE_EXPORT_IMPORT);
                break;
            case R.id.action_refresh_item_list:
                mFragment.mAdapter.reload();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishedHabitEdit(Habit habit) {
        mFragment.updateHabit(habit);
    }


}
