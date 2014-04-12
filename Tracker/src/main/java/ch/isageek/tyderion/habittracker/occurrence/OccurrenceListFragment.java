package ch.isageek.tyderion.habittracker.occurrence;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
public class OccurrenceListFragment extends ListFragment {

    public static final String ARG_HABIT_ID = "habit_id";

    private Long mHabitID;

    private OccurrenceAdapter mAdapter;
    private List<Occurence> occurenceList;

    public static OccurrenceListFragment newInstance(Long habitID) {
        OccurrenceListFragment fragment = new OccurrenceListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        fragment.setArguments(args);
        return fragment;
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

        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);


            Database.asyncOccurrences(getActivity(), mHabitID, new Database.DBCallback<List<Occurence>>() {
                @Override
                public void onFinish(List<Occurence> argument) {
                    occurenceList = argument;
                    mAdapter = new OccurrenceAdapter(getActivity(), R.layout.occerrences_detail_list_item_row, argument);
                    setListAdapter(mAdapter);
                }
            });
        }
        setListAdapter(new OccurrenceAdapter(getActivity(), R.layout.occerrences_detail_list_item_row, new ArrayList<Occurence>(0)));
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

    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(getResources().getString(R.string.delete));
        alert.setMessage(getResources().getString(R.string.delete_occurrence));
        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Occurence removed = occurenceList.remove(deletePosition);
                removed.delete();
                mAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetInvalidated();
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
