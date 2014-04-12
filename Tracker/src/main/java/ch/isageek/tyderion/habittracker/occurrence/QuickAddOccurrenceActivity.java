package ch.isageek.tyderion.habittracker.occurrence;


import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurrence;

public class QuickAddOccurrenceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_occurrence);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            String uuid = new String(((NdefMessage)rawMsgs[0]).getRecords()[0].getPayload());
            new CreateOccurrenceFromUUID(this).execute(uuid);
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quick_add_occurrence, menu);
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


    private static class CreateOccurrenceFromUUID extends AsyncTask<String,Void,Void> {

        Context context;

        String result;

        public  CreateOccurrenceFromUUID(Context context) {
            this.context = context;
        }
        @Override
        protected Void doInBackground(String... strings) {
            String uuid = strings[0];
            HabitDao habitDao = Database.getDaoSession(context).getHabitDao();
            List<Habit> habits = habitDao.queryBuilder().where(HabitDao.Properties.Uuid.eq(uuid)).build().list();
            Habit habit = null;
            if (habits.size() > 0) {
                habit = habits.get(0);
            }
            if (habit != null) {
                Occurrence occ = Database.createOccurrence(context, new Date(), habit.getId());
                result =  "Created : " + occ.toString() + " for habit " + habit.getName();
            } else {
                result = "Could not find Habit associated with this NFC Tag.";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context,result, Toast.LENGTH_LONG).show();
        }
    }

}
