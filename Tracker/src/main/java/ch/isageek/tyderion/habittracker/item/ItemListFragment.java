package ch.isageek.tyderion.habittracker.item;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;


import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;


public class ItemListFragment extends ListFragment implements ItemAdapter.AmountChanged{

   private static final String STATE_ACTIVATED_POSITION = "activated_position";
   private static final String STATE_QUERY_STRING = "query_string";

    public ItemAdapter mAdapter;

    private Callbacks mCallbacks = sDummyCallbacks;

   private int mActivatedPosition = ListView.INVALID_POSITION;
   private Habit selectedHabit;

    private View headerView;
    @InjectView(R.id.item_search_view) SearchView searchView;

    public interface Callbacks {
        public void onItemSelected(Habit habit);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Habit habit) {
        }
    };

    public ItemListFragment() {
    }

    public void addHabit(Habit habit) {
        mAdapter.add(habit);
    }


    public void updateHabit(Habit habit) {
        //TODO: Make a real adapter with an update function
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headerView = getActivity().getLayoutInflater().inflate(R.layout.item_search_header, null);
        ButterKnife.inject(this, headerView);

        if (searchView != null) {
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    filter(s);
                    return true;
                }
            });
        }

        mAdapter = new ItemAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                Database.getDaoSession(getActivity()).getHabitDao().loadAll(), this);

        setListAdapter(mAdapter);
    }

    @Override
    public void onAmountChanged(int newAmount) {
        if (selectedHabit != null) {
            int newPos = mAdapter.getPosition(selectedHabit);
            if (newPos != ListView.INVALID_POSITION) {
                setActivatedPosition(newPos+1);
            }
        }
    }

    private void filter(String string) {
        mAdapter.getFilter().filter(string);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (headerView != null) {
            getListView().addHeaderView(headerView);
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
                setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            }
            if (savedInstanceState.containsKey(STATE_QUERY_STRING)) {
                searchView.setQuery(savedInstanceState.getString(STATE_QUERY_STRING), true);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        if (position > 0) {
            setActivatedPosition(position);
            selectedHabit = mAdapter.getItem(position - 1);
            mCallbacks.onItemSelected(mAdapter.getItem(position - 1));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        if (searchView.getQuery().length() > 0) {
            outState.putString(STATE_QUERY_STRING, searchView.getQuery().toString());
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;

    }



}
