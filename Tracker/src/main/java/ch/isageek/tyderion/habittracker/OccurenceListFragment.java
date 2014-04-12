package ch.isageek.tyderion.habittracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import ch.isageek.tyderion.habittracker.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class OccurenceListFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_HABIT_ID = "habit_id";

    // TODO: Rename and change types of parameters
    private Long mHabitID;
    


    // TODO: Rename and change types of parameters
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
        }

        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }


}
