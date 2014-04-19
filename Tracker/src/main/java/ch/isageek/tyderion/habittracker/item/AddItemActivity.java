package ch.isageek.tyderion.habittracker.item;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;


public class AddItemActivity extends FragmentActivity implements AddItemFragment.Callbacks{

    public static int RESULT_CODE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item); //TODO: add a 'two-pane' layout for screens which are not high enough.
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(AddItemFragment.ARG_HABIT)) {
            Habit habit = getIntent().getExtras().getParcelable(AddItemFragment.ARG_HABIT);
            ((AddItemFragment)getSupportFragmentManager().findFragmentById(R.id.add_item_fragment)).setHabit(habit);
        }
    }

    @Override
    public void onHabitCreated(Habit habit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddItemFragment.ARG_HABIT, habit);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_CODE_OK, intent);
        finish();
    }
}
