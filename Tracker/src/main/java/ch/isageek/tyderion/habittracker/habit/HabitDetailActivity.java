package ch.isageek.tyderion.habittracker.habit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurrence;
import com.github.tyderion.nfcwriter.NFCRecordHelper;
import  com.github.tyderion.nfcwriter.NFCWriter;


/**
 * An activity representing a single Habit detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HabitListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link HabitDetailFragment}.
 */
public class HabitDetailActivity extends FragmentActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private HabitDetailFragment detailFragment;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";


    private Long habitID;
    private Habit habit;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ContextThemeWrapper context = new ContextThemeWrapper(this, android.R.style.Theme_Holo);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), true, false);
        datePickerDialog.setYearRange(2010, 2030);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            this.habitID = getIntent().getLongExtra(HabitDetailFragment.ARG_ITEM_ID, 0);
            if (habitID != 0L) {
                Database.asyncHabit(this, habitID, new Database.DBCallback<Habit>() {
                    @Override
                    public void onFinish(Habit argument) {
                        habit = argument;
                    }
                });
            }

            arguments.putLong(HabitDetailFragment.ARG_ITEM_ID,habitID);
            arguments.putString(HabitDetailFragment.ARG_ITEM_NAME, getIntent().getStringExtra(HabitDetailFragment.ARG_ITEM_NAME));
            this.detailFragment = new HabitDetailFragment();
            detailFragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.habit_detail_container, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        this.setIcon(false, menu.findItem(R.id.detail_save_habit));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.detail_save_habit) {
            boolean editing = detailFragment.toggleEditing();
            this.setIcon(editing, item);

        }
        if (id == R.id.action_occurrence_add) {
            //TODO Start activity/Overlay
                showDatePicker();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {

        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }
    private void showTimePicker() {
        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
    }

    public void showDetails(View view) {
        if (detailFragment!= null) {
            detailFragment.showDetails(view);
        }
    }

    private void setIcon(boolean editing, MenuItem item) {
        if (item != null) {
            item.setIcon(editing ? R.drawable.ic_action_content_save : R.drawable.ic_action_content_edit);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        showTimePicker();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        this.minute = minute;
        this.hour = hourOfDay;
        this.saveOccurrence();
    }

    private void saveOccurrence() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date date = calendar.getTime();
        Occurrence occ = Database.createOccurrence(this, date, habitID);

        Toast.makeText(this, "New date: " +occ.toString(), Toast.LENGTH_SHORT).show();
    }


    public void writeTag(View view) {
        if (this.habit != null) {
            NFCWriter.writeRecords(this, NFCRecordHelper.createMime(getString(R.string.mimeTypeNdef), habit.getUuid()));
        } else {
            Toast.makeText(this, getString(R.string.not_valid_habit), Toast.LENGTH_SHORT).show();
        }
    }
}
