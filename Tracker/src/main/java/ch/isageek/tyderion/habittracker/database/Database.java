package ch.isageek.tyderion.habittracker.database;

import android.content.Context;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.isageek.tyderion.habittracker.model.DaoMaster;
import ch.isageek.tyderion.habittracker.model.DaoSession;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.HabitDao;
import ch.isageek.tyderion.habittracker.model.Occurrence;

/**
 * Created by Archie on 11.04.2014.
 */
public class Database {

    public static DaoMaster.DevOpenHelper getDevOpenHelper(Context context) {
        return new DaoMaster.DevOpenHelper(context, "habits-db", null);
    }

    public static DaoSession getDaoSession(Context context) {
        return new DaoMaster(getDevOpenHelper(context).getWritableDatabase()).newSession();
    }


    public static void asyncHabit(Context context, Long id, DBCallback<Habit> cb) {
        new HabitLoader(context, cb).execute(id);
    }

    public static void asyncOccurrences(Context context, Long habiId, DBCallback<List<Occurrence>> cb) {
        new OccurenceLoader(context, cb).execute(habiId);
    }

    public static Occurrence createOccurrence(Context context, Date date, long habitID) {
        if (habitID != 0 && date != null) {
            Occurrence occurrence = new Occurrence(null, date, habitID);
            getDaoSession(context).getOccurrenceDao().insert(occurrence);
            return occurrence;
        }
        return null;
    }


    public static interface DBCallback<T> {
        public void onFinish(T argument);
    }



    private static class OccurenceLoader extends AsyncTask<Long,Void,List<Occurrence>> {
        private DBCallback<List<Occurrence>> callback;
        private Context context;
        private HabitDao dao;

        public OccurenceLoader(Context context, DBCallback<List<Occurrence>> callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            dao = Database.getDaoSession(context).getHabitDao();
            super.onPreExecute();
        }

        @Override
        protected List<Occurrence> doInBackground(Long... longs) {
            if (longs.length == 1) {
                Habit habit = dao.load(longs[0]);
                if (habit != null) {
                    return habit.getOccurrenceList();
                }
            }
            return new ArrayList<Occurrence>(0);
        }

        @Override
        protected void onPostExecute(List<Occurrence> occurrences) {
            if (callback != null) {
                callback.onFinish(occurrences);
            }
        }


    }



      private static class HabitLoader extends AsyncTask<Long, Void, Habit> {
        private Context context;
        private DBCallback<Habit> cb;

        public HabitLoader(Context context, DBCallback<Habit> cb) {
            this.context = context;
            this.cb = cb;
        }

        @Override
        protected Habit doInBackground(Long... longs) {
            return Database.getDaoSession(context).getHabitDao().load(longs[0]);
        }

        @Override
        protected void onPostExecute(Habit habit) {
            cb.onFinish(habit);
        }
    }
}
