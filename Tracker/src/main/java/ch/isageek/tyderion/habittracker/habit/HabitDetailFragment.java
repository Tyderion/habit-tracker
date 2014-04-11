package ch.isageek.tyderion.habittracker.habit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.dummy.DummyContent;
import ch.isageek.tyderion.habittracker.model.DaoSession;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurence;

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

    /**
     * The dummy content this fragment is presenting.
     */
    private Long mHabitID;

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
        View rootView = inflater.inflate(R.layout.fragment_habit_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mHabitID != null) {
            DaoSession session = Database.getDaoSession(getActivity());
            HabitDao habitDao = session.getHabitDao();
            Habit habit = habitDao.load(mHabitID);
                TextView view = ((TextView) rootView.findViewById(R.id.habit_detail));

            EditText occurences = (EditText)rootView.findViewById(R.id.habit_detail_occurrences);
            if (habit != null) {
                view.setText(habit.getName());
                StringBuilder builder = new StringBuilder("");
                for (Occurence occ : habit.getOccurenceList()) {
                    builder.append(occ.toString()+"\n");
                }
                occurences.setText(builder.toString());
            } else {
                view.setText("Habit not found");
            }
        }

        return rootView;
    }
}
