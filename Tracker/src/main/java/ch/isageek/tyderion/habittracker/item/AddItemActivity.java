package ch.isageek.tyderion.habittracker.item;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ch.isageek.tyderion.habittracker.R;


/**
 * An activity representing a single Habit detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ch.isageek.tyderion.habittracker.habit.HabitListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ch.isageek.tyderion.habittracker.habit.HabitDetailFragment}.
 */
public class AddItemActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
    }
}
