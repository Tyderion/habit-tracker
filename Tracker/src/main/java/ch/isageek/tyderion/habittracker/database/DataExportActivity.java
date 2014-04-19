package ch.isageek.tyderion.habittracker.database;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.DaoSession;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurrence;
import ch.isageek.tyderion.habittracker.model.OccurrenceDao;


public class DataExportActivity extends Activity {

    static final int REQUEST_PICK_IMPORT_FILE = 1;
    static final int REQUEST_PICK_EXPORT_FILE = 2;
    public final static String ARG_DID_IMPORT = "imported_data";

    public final static int REQUEST_CODE_EXPORT_IMPORT = 1;

    private PlaceholderFragment placeholderFragment;
    private Uri fileUri;
    private Gson gson;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_export);
        this.placeholderFragment = new PlaceholderFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.data_export_container, placeholderFragment)
                    .commit();
        }
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create();
        context = getApplicationContext();
    }

    private static class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date>  {
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZZZ");
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Date date;
            try {
                date = format.parse(json.toString().replace("\"", ""));
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
            return date;
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(format.format(src));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_PICK_IMPORT_FILE || requestCode == REQUEST_PICK_EXPORT_FILE)&& resultCode == RESULT_OK) {
            Uri backupFileUri  = data.getData();
            Toast.makeText(this, backupFileUri.toString(), Toast.LENGTH_SHORT).show();
            fileUri = backupFileUri;
            if (requestCode == REQUEST_PICK_EXPORT_FILE) {
                this.exportData();
            } else if (requestCode == REQUEST_PICK_IMPORT_FILE) {
                this.importData();
            }
        }
    }

    @OnClick(R.id.backup_import_button)
    public void pickImportFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_PICK_IMPORT_FILE);
    }


    @OnClick(R.id.backup_export_button)
    public void pickExportFiles(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_PICK_EXPORT_FILE);
    }


    public void importData() {
        ParcelFileDescriptor fileDesc = null;
        ContentResolver resolver = context.getContentResolver();
        try {
            fileDesc = resolver.openAssetFileDescriptor(fileUri, "r").getParcelFileDescriptor();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        final ParcelFileDescriptor fileDescriptor = fileDesc;
        if (fileDescriptor != null) {
            new AsyncTask<Void, Void, Void>() {
                private boolean error = false;

                @Override
                protected void onPreExecute() {
                    Toast.makeText(context, context.getString(R.string.backup_import_start), Toast.LENGTH_SHORT).show();                }

                @Override
                protected Void doInBackground(Void... voids) {
                    FileReader reader = new FileReader(fileDescriptor.getFileDescriptor());
                    List<HabitBackupObject> imported = new ArrayList<HabitBackupObject>(0);
                    error = false;
                    try {
                        imported = Arrays.asList(gson.fromJson(reader, HabitBackupObject[].class));
                        reader.close();
                    } catch (IOException e) {
                        Log.d("TRACKER_BACKUP_IMPORT", e.toString());
                        e.printStackTrace();
                        error = true;
                    }
                    catch (JsonSyntaxException e) {
                        error = true;
                        Log.d("TRACKER_BACKUP_IMPORT", e.toString());
                        e.printStackTrace();
                    }
                    catch (JsonIOException e) {
                        error = true;
                        Log.d("TRACKER_BACKUP_IMPORT", e.toString());
                        e.printStackTrace();
                    }
                    if (!error) {
                        DaoSession session = Database.getDaoSession(context);
                        HabitDao habitDao = session.getHabitDao();
                        OccurrenceDao occurrenceDao = session.getOccurrenceDao();
                        for (HabitBackupObject habit : imported) {
                            habit.insert(habitDao, occurrenceDao);
                        }
                        Database.getDevOpenHelper(context).close();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if (error) {
                        Toast.makeText(context, getString(R.string.backup_import_error), Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(REQUEST_CODE_EXPORT_IMPORT, new Intent().putExtra(ARG_DID_IMPORT, true));
                        Toast.makeText(context, getString(R.string.backup_import_complete), Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    public void exportData() {
        ParcelFileDescriptor fileDesc = null;
        ContentResolver resolver = getContentResolver();
        try {
            fileDesc = resolver.openAssetFileDescriptor(fileUri, "rwt").getParcelFileDescriptor();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        final ParcelFileDescriptor fileDescriptor = fileDesc;
        if (fileDescriptor != null) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    Toast.makeText(context, context.getString(R.string.backup_export_start), Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                    DaoSession session = Database.getDaoSession(context);
                    HabitDao habitDao = session.getHabitDao();
                    List<Habit> habits = habitDao.loadAll();
                    List<HabitBackupObject> backupObjects = new ArrayList<HabitBackupObject>(habits.size());
                    for (Habit habit : habitDao.loadAll()) {
                        backupObjects.add(new HabitBackupObject(habit));
                    }
                    String flat = gson.toJson(backupObjects);
                    try {
                        outputFile.write(flat.getBytes());
                        Log.d("TRACKER_BACKUP_EXPORT", flat);
                        outputFile.close();
                    } catch (IOException e) {
                        Toast.makeText(context, context.getString(R.string.backup_write_error), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Database.getDevOpenHelper(context).close();

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(context, context.getString(R.string.backup_export_complete), Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
    }



    private static class HabitBackupObject {
        public java.util.Date dateCreated;
        public String name;
        public Boolean isPositive;
        public String description;
        public String uuid;
        public List<Date> occurrences;

        public HabitBackupObject(Habit habit) {
            this.dateCreated = habit.getDateCreated();
            this.name = habit.getName();
            this.isPositive = habit.getIsPositive();
            this.description = habit.getDescription();
            this.uuid = habit.getUuid();

            List<Occurrence> occurrencesList =  habit.getOccurrenceList();
            this.occurrences = new ArrayList<Date>(occurrencesList.size());
            for (Occurrence occurrence :occurrencesList) {
                occurrences.add(occurrence.getDate());
            }
        }

        public void insert(HabitDao habitdao, OccurrenceDao occurrenceDao) {
            Habit h  = new Habit(null, dateCreated, name, isPositive, description, uuid);
            try {
                habitdao.insert(h);
                for (Date date : occurrences) {
                    Occurrence occ = new Occurrence(date, h.getId());
                    occurrenceDao.insert(occ);
                }
            } catch (SQLiteConstraintException e) {
                Log.d("BACKUP_IMPORT_INSERT_HABIT", "Habit already exists");
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_data_export, container, false);
            ButterKnife.inject(getActivity(), rootView);
            return rootView;
        }
    }
}
