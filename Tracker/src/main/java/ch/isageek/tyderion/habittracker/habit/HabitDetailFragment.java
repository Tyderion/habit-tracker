package ch.isageek.tyderion.habittracker.habit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;


import ch.isageek.tyderion.habittracker.EditHabitFragment;
import ch.isageek.tyderion.habittracker.OccurencesFragment;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;

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
    private String mItemName;


    private EditText descriptionText;
    private EditText titleText;
    private CheckBox positiveBox;

    private OccurencesFragment occurencesFragment;
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

        this.occurencesFragment = (OccurencesFragment)getFragmentManager().findFragmentById(R.id.habit_detail_occurences_fragment);
        occurencesFragment.setHabitId(mHabitID);


        this.habitFragment = (EditHabitFragment)getFragmentManager().findFragmentById(R.id.habit_detail_edit_fragment);
        habitFragment.setEditing(false);
        habitFragment.setHabitID(mHabitID);
//                (OccurencesFragment)view.findViewById(R.id.habit_detail_occurences_fragment);

//        TextView view = ((TextView) rootView.findViewById(R.id.habit_detail));
//
//        EditText occurrences = (EditText)rootView.findViewById(R.id.habit_detail_occurrences);
//        if (mItemName != null) {
//            view.setText(mItemName);
//        }else {
//            view.setText("Habit not found");
//        }
//        // Show the dummy content as text in a TextView.
//        if (mHabitID != null) {
//            new OccurenceLoader(getActivity(), view, occurrences).execute(mHabitID);
////            }
//        }

        return view;
    }

    private void updateDisplay() {
        Database.asyncHabit(getActivity(), mHabitID, new Database.DBCallback<Habit>() {
            @Override
            public void onFinish(Habit argument) {
                descriptionText.setText(argument.getDescription());
                titleText.setText(argument.getName());
                positiveBox.setActivated(argument.getIsPositive());
                Bundle args = new Bundle();
                args.putLong(OccurencesFragment.ARG_HABIT_ID, argument.getId());
                occurencesFragment.setArguments(args);
            }
        });

    }
}
