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

import java.util.Date;

import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link EditHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EditHabitFragment extends Fragment {
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

    private boolean editing;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitID The ID of the habit
     * @param habitName the Name of the habit
     * @return A new instance of fragment EditHabitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditHabitFragment newInstance(Long habitID, String habitName) {
        EditHabitFragment fragment = new EditHabitFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_HABIT_ID, habitID);
        args.putString(ARG_HABIT_NAME, habitName);
        fragment.setArguments(args);
        return fragment;
    }
    public EditHabitFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabitID = getArguments().getLong(ARG_HABIT_ID);
            mHabitName = getArguments().getString(ARG_HABIT_NAME);
        }
        this.getHabit();

    }

    private void getHabit() {
        Database.asyncHabit(getActivity(), mHabitID, new Database.DBCallback<Habit>() {
            @Override
            public void onFinish(Habit argument) {
                mHabit = argument;
                updateDisplay();
            }
        });
    }


    public void setHabitID(Long habitID) {
        this.mHabitID = habitID;
        this.getHabit();
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

    public void setEditing(boolean editing) {
        this.editing = editing;
        this.descriptionText.setEnabled(editing);
        this.titleText.setEnabled(editing);
        this.positiveBox.setEnabled(editing);
    }

    public void updateDisplay() {
        if (mHabit != null) {
            this.descriptionText.setText(this.mHabit.getDescription());
            this.titleText.setText(this.mHabit.getName());
            this.positiveBox.setActivated(this.mHabit.getIsPositive());
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
    }


}
