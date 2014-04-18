package ch.isageek.tyderion.habittracker.item;

import android.app.Activity;
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
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onHabitCreated(Habit habit);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onHabitCreated(Habit habit) {
        }
    };


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
        mHabit.setId(Database.getDaoSession(getActivity()).getHabitDao().insertOrReplace(mHabit));
        Database.getDevOpenHelper(getActivity()).close();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.save_item_button)
    public void saveHabit(View view) {
        save();
        mCallbacks.onHabitCreated(mHabit);
    }
}
