package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Occurrence;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link OccurrencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OccurrencesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_HABIT_ID = "habit_id";

    private Long mHabitID;

    private TextView count;
    private TextView last;

    private List<Occurrence> occurrenceList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitID the Habit ID of the occurences
     * @return A new instance of fragment OccurrencesFragment.
     */
    public static OccurrencesFragment newInstance(Long habitID) {
        OccurrencesFragment fragment = new OccurrencesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        fragment.setArguments(args);
        return fragment;
    }
    public OccurrencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);
            loadOccurrences();
        }
    }

    private void loadOccurrences() {
        Database.asyncOccurrences(getActivity(), mHabitID, new Database.DBCallback<List<Occurrence>>() {
            @Override
            public void onFinish(List<Occurrence> argument) {
                occurrenceList = argument;
                updateView();
            }
        });
    }

    public List<Occurrence> getOccerrences() {
        return this.occurrenceList;
    }
    public void setHabitId(Long habitID) {
        this.mHabitID = habitID;
        loadOccurrences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_occurences, container, false);
        count = (TextView)view.findViewById(R.id.occurrences_count);
        last = (TextView)view.findViewById(R.id.occurrences_last);
//        occurrencesEditText = (TextView) view.findViewById(R.id.occurrence_edit_text);
        return view;
    }

    private void updateView() {
        if (this.occurrenceList != null & this.occurrenceList.size() > 0) {
            count.setText(((Integer)occurrenceList.size()).toString());
            last.setText(occurrenceList.get(0).toString());
        } else {
            count.setText(new Integer(0).toString());
            last.setText(getActivity().getString(R.string.never));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void showDetails(View view) {

        Toast.makeText(getActivity(), "Show Details", Toast.LENGTH_SHORT).show();
    }
}
