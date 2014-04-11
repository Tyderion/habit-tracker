package ch.isageek.tyderion.habittracker.habit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.dummy.DummyContent;
import ch.isageek.tyderion.habittracker.model.DaoSession;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurence;

/**
 * A fragment representing a single Habit detail screen.
 * This fragment is either contained in a {@link HabitListActivity}
 * in two-pane mode (on tablets) or a {@link HabitDetailActivity}
 * on handsets.
 */
public class HabitDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";

    /**
     * The dummy content this fragment is presenting.
     */
    private Long mHabitID;
    private String mItemName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mHabitID = getArguments().getLong(ARG_ITEM_ID);
            mItemName = getArguments().getString(ARG_ITEM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit_detail, container, false);
        TextView view = ((TextView) rootView.findViewById(R.id.habit_detail));
        EditText occurrences = (EditText)rootView.findViewById(R.id.habit_detail_occurrences);
        if (mItemName != null) {
            view.setText(mItemName);
        }else {
            view.setText("Habit not found");
        }
        // Show the dummy content as text in a TextView.
        if (mHabitID != null) {
            new OccurenceLoader(getActivity(), view, occurrences).execute(mHabitID);
//            }
        }

        return rootView;
    }


    private static class OccurenceLoader extends AsyncTask<Long,Void,List<Occurence>> {
        private EditText editText;
        private TextView titleView;
        private Context context;

        private HabitDao habitDao;


        public OccurenceLoader(Context context, TextView titleView, EditText text) {
            this.editText = text;
            this.titleView = titleView;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DaoSession session = Database.getDaoSession(context);
            habitDao = session.getHabitDao();
        }

        @Override
        protected List<Occurence> doInBackground(Long... longs) {
            Long habitID = longs[0];
            Habit habit = habitDao.load(habitID);
            List<Occurence> occurrences = null;
            if (habit != null) {
                titleView.setText(habit.getName());
                occurrences = habit.getOccurenceList();
            }
            return occurrences;
        }

        @Override
        protected void onPostExecute(List<Occurence> occurences) {
            super.onPostExecute(occurences);
            StringBuilder builder = new StringBuilder("");
            for (Occurence occ : occurences) {
                builder.append(occ.toString() + "\n");
            }
            editText.setText(builder.toString());
        }
    }
}
