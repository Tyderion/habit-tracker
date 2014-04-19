package ch.isageek.tyderion.habittracker.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurrence;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements CalendarDatePickerDialog.OnDateSetListener, RadialTimePickerDialog.OnTimeSetListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_HABIT = "habit";

    /**
     * The dummy content this fragment is presenting.
     */
    private Habit mHabit;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    private CalendarDatePickerDialog datePickerDialog;
    private RadialTimePickerDialog timePickerDialog;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog  = CalendarDatePickerDialog
                .newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2010, 2030);
        datePickerDialog.setThemeDark(true);

        timePickerDialog = RadialTimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.setThemeDark(true);

        if (getArguments().containsKey(ARG_HABIT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mHabit = getArguments().getParcelable(ARG_HABIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mHabit != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mHabit.getName());
            ((TextView) rootView.findViewById(R.id.item_detail_description)).setText(mHabit.getDescription());
        }
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_detail_add_occurrence && this.mHabit != null) {
            showDatePicker();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(CalendarDatePickerDialog datePickerDialog, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        showTimePicker();
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog view, int hourOfDay, int minute) {
        this.minute = minute;
        this.hour = hourOfDay;
        this.saveOccurrence();
    }


    private void showDatePicker() {
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
    }
    private void showTimePicker() {
        timePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
    }


    private void saveOccurrence() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date date = calendar.getTime();
        Occurrence occ = Database.createOccurrence(getActivity(), date, mHabit.getId());
        Toast.makeText(getActivity(), "New date: " + occ.toString(), Toast.LENGTH_SHORT).show();
    }
}
