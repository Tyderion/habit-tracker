package ch.isageek.tyderion.habittracker.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.github.tyderion.nfcwriter.NFCRecordHelper;
import com.github.tyderion.nfcwriter.NFCWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.graphrender.SalesStackedBarChart;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurrence;
import ch.isageek.tyderion.habittracker.occurrence.OccurrenceListActivity;
import ch.isageek.tyderion.habittracker.occurrence.OccurrenceListFragment;

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
    public static final String ARG_DUALPANE = "dualpane";

    public static final int REQUEST_EDIT_HABIT = 11;

    private Habit mHabit;
    private boolean mDualpane;

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

    @InjectView(R.id.item_detail_occurrences_total) TextView totalTextView;
    @InjectView(R.id.item_detail) TextView titleTextView;
    @InjectView(R.id.item_detail_description) TextView descriptionTextView;
    @InjectView(R.id.chart) LinearLayout chartLayout;
    View chart;


    private Callbacks mCallbacks = sDummyCallbacks;

    private void setHabit(Habit mHabit) {
        this.mHabit = mHabit;
        reloadOccurencesAndView();
    }

    public Habit getHabit() {
        return mHabit;
    }

    public interface Callbacks {
        public void finishedHabitEdit(Habit habit);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void finishedHabitEdit(Habit habit) {
        }
    };

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

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

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
            setHabit((Habit) getArguments().getParcelable(ARG_HABIT));
        }
        if (getArguments().containsKey(ARG_DUALPANE)) {
            mDualpane = getArguments().getBoolean(ARG_DUALPANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        //TODO add bar-chart with max/min/median values for each weekday
        ButterKnife.inject(this, rootView);
        if (mHabit != null) {
            reloadOccurencesAndView();
            setHasOptionsMenu(true);
        }
        return rootView;
    }

    private void createChart() {

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        final Context context = getActivity();
        new AsyncTask<Void, Void, ChartData>() {
            @Override
            protected ChartData doInBackground(Void... voids) {
                List<Occurrence> list= Database.getDaoSession(context).getHabitDao().load(mHabit.getId()).getOccurrenceList();
                Map<String,DayData> dayMap = new HashMap<String, DayData>();
                for (Occurrence occurrence : list) {
                    String day = dateFormatter.format(occurrence.getDate());
                    if (dayMap.containsKey(day)) {
                        dayMap.get(day).incr();
                    } else {
                        DayData data = new DayData(occurrence.getDate());
                        dayMap.put(day, data);
                    }
                }
                WeekdayData[] weekList = new WeekdayData[7];
                for (int i = 0; i < 7; i++) {
                    weekList[i] = new WeekdayData();
                }
                for (DayData data : dayMap.values()) {
                    weekList[data.date.getDay()].add(data);
                }
                ChartData data = new ChartData();
                for (int i = 0; i < 7; i++) {
                    WeekdayData weekdayData = weekList[i];
                    data.avg[i] = weekdayData.getAvg();
                    data.max[i] = weekdayData.getMax();
                    data.min[i] = weekdayData.getMin();
                }
                for (int i = 0; i < 7; i++) {
                    data.maxmax = (data.maxmax > weekList[i].max) ? data.maxmax : weekList[i].max;
                }
                return data;
            }

            @Override
            protected void onPostExecute(ChartData chartData) {
                chart = new SalesStackedBarChart().getView(getActivity(),chartData.max,chartData.avg,chartData.min, chartData.maxmax);
                chartLayout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }.execute();

    }

    private class DayData {
        Date date;
        int occurrences;
        public DayData(Date date) {
            this.date = date;
            occurrences = 1;
        }
        public void incr() {
            occurrences++;
        }
    }

    private class WeekdayData {
        private double max;
        private double min;
        private double avg;
        private int count;
        public WeekdayData() {
            max  = avg = count = 0;
            min = Integer.MAX_VALUE;
        }
        public void add(DayData data) {
            this.min = Math.min(data.occurrences, this.min);
            this.max = Math.max(data.occurrences, this.max);
            this.avg += data.occurrences;
            this.count++;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min == Integer.MAX_VALUE ? 0 : min;
        }

        public double getAvg() {
            return count > 0 ? avg / count : 0;
        }

        public int getCount() {
            return count;
        }
    }

    private class ChartData {
        double[] max;
        double[] min;
        double[] avg;
        double maxmax;
        public ChartData() {
            max = new double[7];
            min = new double[7];
            avg = new double[7];
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_detail_menu, menu);
        if (mDualpane) {
            setShowWithText(menu.findItem( R.id.edit_habit));
            setShowWithText(menu.findItem( R.id.action_item_detail_add_occurrence));
            setShowWithText(menu.findItem(R.id.action_item_detail_nfc));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setShowWithText(MenuItem item) {
        if (item != null) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_item_detail_add_occurrence:
                if (this.mHabit != null) {
                    showDatePicker();
                }
                break;
            case R.id.action_item_detail_nfc:
                if (this.mHabit != null) {
                    NFCWriter.writeRecords(getActivity(), NFCRecordHelper.createMime(getString(R.string.mimeTypeNdef), mHabit.getUuid()));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.not_valid_habit), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_habit:
                Bundle bundle = new Bundle();
                bundle.putParcelable(AddItemFragment.ARG_HABIT, mHabit);
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_EDIT_HABIT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_HABIT && data != null && data.getExtras() != null) {
            Habit habit= data.getExtras().getParcelable(AddItemFragment.ARG_HABIT);
            setHabit(habit);
            mCallbacks.finishedHabitEdit(habit);
        }
        super.onActivityResult(requestCode, resultCode, data);
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


    @OnClick(R.id.item_detail_open_occurrences_list)
    public void showOccurrencesList(Button button) {
        Intent detailIntent = new Intent(getActivity(), OccurrenceListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(OccurrenceListFragment.ARG_HABIT, mHabit);
        detailIntent.putExtras(bundle);
        startActivity(detailIntent);
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
        reloadOccurencesAndView();
        Toast.makeText(getActivity(), "New date: " + occ.toString(), Toast.LENGTH_SHORT).show();
    }

    private void reloadOccurencesAndView() {
        if (mHabit != null && titleTextView != null) {
            titleTextView.setText(mHabit.getName());
            descriptionTextView.setText(mHabit.getDescription());
            Database.asyncOccurrences(getActivity(), mHabit.getId(), new Database.DBCallback<List<Occurrence>>() {
                @Override
                public void onFinish(List<Occurrence> argument) {
                    int size = argument.size();
                    updateView(size, size > 0 ? argument.get(0) : null);
                    if (size > 0) {
                        createChart();
                    }
                }
            });
        }
    }

    private void updateView(int size, Occurrence occurrence) {
        if (occurrence != null) {
            totalTextView.setText(size + " (Last "+occurrence.toString()+")");
        } else {
            totalTextView.setText("0");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadOccurencesAndView();
    }
}
