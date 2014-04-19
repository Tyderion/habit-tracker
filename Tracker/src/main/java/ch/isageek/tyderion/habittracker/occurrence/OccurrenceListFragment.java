package ch.isageek.tyderion.habittracker.occurrence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class OccurrenceListFragment extends ListFragment {

    public static final String ARG_HABIT = "habit";

    private Habit mHabit;

    private OccurrenceAdapter mAdapter;

    public static OccurrenceListFragment newInstance(Long habitID) {
        OccurrenceListFragment fragment = new OccurrenceListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT, habitID);
        fragment.setArguments(args);
        return fragment;
    }

    public void setHabit(Habit mHabit) {
        this.mHabit = mHabit;
        mAdapter.setHabit(mHabit);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OccurrenceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_HABIT)) {
            mHabit = getArguments().getParcelable(ARG_HABIT);
        }
        mAdapter = new OccurrenceAdapter(getActivity(), R.layout.occerrences_detail_list_item_row, mHabit);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView view = getListView();
        if (view != null) {
            view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                    removeItemFromList(position);
                    return true;
                }
            });
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    protected void removeItemFromList(final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(getResources().getString(R.string.delete));
        alert.setMessage(getResources().getString(R.string.delete_occurrence));
        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(mAdapter.getItem(position));
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }
}
