package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Occurence;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class OccurenceListFragment extends ListFragment {

    public static final String ARG_HABIT_ID = "habit_id";

    private Long mHabitID;

    private OccurrenceAdapter mAdapter;

    public static OccurenceListFragment newInstance(Long habitID) {
        OccurenceListFragment fragment = new OccurenceListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OccurenceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);



            Database.asyncOccurrences(getActivity(), mHabitID, new Database.DBCallback<List<Occurence>>() {
                @Override
                public void onFinish(List<Occurence> argument) {
                    mAdapter = new OccurrenceAdapter(getActivity(), R.layout.occerrences_detail_list_item_row, argument);
                    setListAdapter(mAdapter);
                }
            });
        }
        setListAdapter(new OccurrenceAdapter(getActivity(), R.layout.occerrences_detail_list_item_row, new ArrayList<Occurence>(0)));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
