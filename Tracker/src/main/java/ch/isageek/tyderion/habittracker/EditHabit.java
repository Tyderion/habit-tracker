package ch.isageek.tyderion.habittracker;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;

import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditHabit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditHabit#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EditHabit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_HABIT_ID = "habit_id";
    public static final String ARG_HABIT_NAME = "habit_name";

    // TODO: Rename and change types of parameters
    private Long mHabitID;
    private String mHabitName;

    private Habit mHabit;

    private EditText descriptionText;
    private EditText titleText;
    private CheckBox positiveBox;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitID The ID of the habit
     * @param habitName the Name of the habit
     * @return A new instance of fragment EditHabit.
     */
    // TODO: Rename and change types and number of parameters
    public static EditHabit newInstance(Long habitID, String habitName) {
        EditHabit fragment = new EditHabit();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        args.putString(ARG_HABIT_NAME, habitName);
        fragment.setArguments(args);
        return fragment;
    }
    public EditHabit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);
            mHabitName = getArguments().getString(ARG_HABIT_NAME);
        }

        Database.asyncHabit(getActivity(), mHabitID, new Database.DBCallback<Habit>() {
            @Override
            public void onFinish(Habit argument) {
                mHabit = argument;
                updateDisplay();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_edit_habit, container, false);

        this.descriptionText = (EditText)view.findViewById(R.id.edit_habit_description_text);
        this.titleText = (EditText)view.findViewById(R.id.edit_habit_title_text);
        this.positiveBox = (CheckBox)view.findViewById(R.id.edit_habit_positive);

        updateDisplay();

        return view;
    }

    public void updateDisplay() {
        if (mHabit != null) {
            this.descriptionText.setText(this.mHabit.getDescription());
            this.titleText.setText(this.mHabit.getName());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public void save() {
        if (this.mHabit == null) {
            this.mHabit = new Habit(mHabitID);
        }
        this.mHabit.setName(this.titleText.getText().toString());
        this.mHabit.setDateCreated(new Date());
        this.mHabit.setDescription(this.descriptionText.getText().toString());
        this.mHabit.setIsPositive(this.positiveBox.isActivated());
        Database.getDaoSession(getActivity()).getHabitDao().insertOrReplace(mHabit);

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
