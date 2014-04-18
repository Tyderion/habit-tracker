package ch.isageek.tyderion.habittracker.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link ch.isageek.tyderion.habittracker.item.AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddItemFragment extends Fragment{
    public static final String ARG_HABIT = "habit";

    private Habit mHabit;

    @InjectView(R.id.add_item_description_text) EditText descriptionText;
    @InjectView(R.id.add_item_title_text) EditText titleText;
    @InjectView(R.id.add_item_positive) CheckBox positiveBox;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habit the Habit
     * @return A new instance of fragment EditHabitFragment.
     */
    public static AddItemFragment newInstance(Habit habit) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_HABIT, habit);
        fragment.setArguments(args);
        return fragment;
    }
    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_HABIT)) {
                mHabit = savedInstanceState.getParcelable(ARG_HABIT);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHabit = getArguments().getParcelable(ARG_HABIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        ButterKnife.inject(this, view);
        updateDisplay();
        return view;
    }

    public void updateDisplay() {
        if (mHabit != null) {
            this.descriptionText.setText(this.mHabit.getDescription());
            this.titleText.setText(this.mHabit.getName());
            this.positiveBox.setActivated(this.mHabit.getIsPositive());
        }
    }

    public void save() {
        if (this.mHabit == null) {
            this.mHabit = new Habit();
        }
        this.mHabit.setName(this.titleText.getText().toString());
        this.mHabit.setDateCreated(new Date());
        this.mHabit.setDescription(this.descriptionText.getText().toString());
        this.mHabit.setIsPositive(this.positiveBox.isActivated());
        Database.getDaoSession(getActivity()).getHabitDao().insertOrReplace(mHabit);
        Database.getDevOpenHelper(getActivity()).close();

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.save_habit_button)
    public void saveHabit(View view) {
        save();
        getActivity().finish();
    }
}
