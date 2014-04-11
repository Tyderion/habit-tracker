package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Occurence;

public class OccurenceDetailActivity extends Activity implements OccurencesDetailFragment.OccurencesDetailDataSource{

    public static String ARG_HABIT_ID = "habit_name";

    private List<Occurence> occurrenceList;

    private Long habitID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurence_detail);

        habitID = getIntent().getExtras().getLong(ARG_HABIT_ID);
//        if (habitID != null) {
//            Database.asyncOccurrences(getApplicationContext(), habitID, new Database.DBCallback<List<Occurence>>() {
//                @Override
//                public void onFinish(List<Occurence> argument) {
//                    occurrenceList = argument;
//                }
//            });
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.occurence_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public List<Occurence> getOccurrenceList() {
        return this.occurrenceList;
    }

}
