package ch.isageek.tyderion.habittracker.habit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import ch.isageek.tyderion.habittracker.EditHabitFragment;
import ch.isageek.tyderion.habittracker.OccurrencesFragment;
import ch.isageek.tyderion.habittracker.R;

/**
 * A fragment representing a single Habit detail screen.
 * This fragment is either contained in a {@link HabitListActivity}
 * in two-pane mode (on tablets) or a {@link HabitDetailActivity}
 * on handsets.
 */
public class HabitDetailFragment extends Fragment  {
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
    private String mItemName;


    private EditText descriptionText;
    private EditText titleText;
    private CheckBox positiveBox;

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
            mItemName = getArguments().getString(ARG_ITEM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_detail, container, false);


        this.descriptionText = (EditText)view.findViewById(R.id.edit_habit_description_text);
        this.titleText = (EditText)view.findViewById(R.id.edit_habit_title_text);
        this.positiveBox = (CheckBox)view.findViewById(R.id.edit_habit_positive);

        this.occurrencesFragment = (OccurrencesFragment)getFragmentManager().findFragmentById(R.id.habit_detail_occurences_fragment);
        occurrencesFragment.setHabitId(mHabitID);


        this.habitFragment = (EditHabitFragment)getFragmentManager().findFragmentById(R.id.habit_detail_edit_fragment);
        habitFragment.setEditing(false);
        this.editing = false;
        habitFragment.setHabitID(mHabitID);
        return view;
    }

    public void toggleEditing() {
        if (editing) {
            habitFragment.save();
        }
        habitFragment.setEditing(!editing);
        editing = !editing;
    }

    public void showDetails(View view) {
        occurrencesFragment.showDetails(view);
    }


}
