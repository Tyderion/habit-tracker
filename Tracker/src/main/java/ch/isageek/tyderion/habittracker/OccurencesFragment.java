package ch.isageek.tyderion.habittracker;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Occurence;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OccurencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OccurencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OccurencesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_HABIT_ID = "habit_id";

    private Long mHabitID;

    private TextView occurrencesEditText;

    private List<Occurence> occurenceList = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitID the Habit ID of the occurences
     * @return A new instance of fragment OccurencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OccurencesFragment newInstance(Long habitID) {
        OccurencesFragment fragment = new OccurencesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        fragment.setArguments(args);
        return fragment;
    }
    public OccurencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);
            getOccurrences();
        }
    }

    private void getOccurrences() {
        Database.asyncOccurrences(getActivity(), mHabitID, new Database.DBCallback<List<Occurence>>() {
            @Override
            public void onFinish(List<Occurence> argument) {
                occurenceList = argument;
                updateView();
            }
        });
    }

    public void setHabitId(Long habitID) {
        this.mHabitID = habitID;
        getOccurrences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_occurences, container, false);
        occurrencesEditText = (TextView) view.findViewById(R.id.occurrence_edit_text);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateView() {
        if (this.occurenceList != null) {
            StringBuilder builder = new StringBuilder("");
            for (Occurence occ : this.occurenceList) {
                builder.append(occ.toString() + "\n");
            }
            this.occurrencesEditText.setText(builder.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
