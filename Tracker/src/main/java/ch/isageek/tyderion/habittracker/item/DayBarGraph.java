package ch.isageek.tyderion.habittracker.item;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurrence;

public class DayBarGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_HABIT = "habit";

    // TODO: Rename and change types of parameters
    private Habit mHabit;
    private List<Occurrence> habits;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habit the habit.
     * @return A new instance of fragment DayBarGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static DayBarGraph newInstance(Habit habit) {
        DayBarGraph fragment = new DayBarGraph();
        Bundle args = new Bundle();
        args.putParcelable(ARG_HABIT, habit);
        fragment.setArguments(args);
        return fragment;
    }
    public DayBarGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabit = getArguments().getParcelable(ARG_HABIT);
            Database.asyncOccurrences(getActivity(), mHabit.getId(), new Database.DBCallback<List<Occurrence>>() {
                @Override
                public void onFinish(List<Occurrence> argument) {
                    setHabits(argument);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_bar_graph, container, false);
    }
    public void setHabits(List<Occurrence> habits) {
        this.habits = habits;
    }

    private void updateGraph() {
        //TODO Implement
    }

}
