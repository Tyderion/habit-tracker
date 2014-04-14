package ch.isageek.tyderion.habittracker.habit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;


import ch.isageek.tyderion.habittracker.occurrence.OccurrenceListFragment;
import ch.isageek.tyderion.habittracker.occurrence.OccurrenceListActivity;
import ch.isageek.tyderion.habittracker.occurrence.OccurrencesFragment;
import ch.isageek.tyderion.habittracker.R;

/**
 * A fragment representing a single Habit detail screen.
 * This fragment is either contained in a {@link HabitListActivity}
 * in two-pane mode (on tablets) or a {@link HabitDetailActivity}
 * on handsets.
 */
public class HabitDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";

    /**
     * The dummy content this fragment is presenting.
     */
    private Long mHabitID;
    private boolean editing;

    private OccurrencesFragment occurrencesFragment;
    private EditHabitFragment habitFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mHabitID = getArguments().getLong(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_detail, container, false);

        this.occurrencesFragment = (OccurrencesFragment)getFragmentManager().findFragmentById(R.id.habit_detail_occurences_fragment);
        occurrencesFragment.setHabitId(mHabitID);

        this.habitFragment = (EditHabitFragment)getFragmentManager().findFragmentById(R.id.habit_detail_edit_fragment);
        habitFragment.setHabitID(mHabitID);
        habitFragment.setEditing(false);
        this.editing = false;
        return view;
    }

    public boolean getEditing() {
        return editing;
    }

    public boolean toggleEditing() {
        if (editing) {
            habitFragment.save();
        }
        habitFragment.setEditing(!editing);
        editing = !editing;
        return editing;
    }

    public void showDetails(View view) {
        Intent detailIntent = new Intent(getActivity(), OccurrenceListActivity.class);
        detailIntent.putExtra(OccurrenceListFragment.ARG_HABIT_ID, mHabitID);
        startActivity(detailIntent);
    }


}
