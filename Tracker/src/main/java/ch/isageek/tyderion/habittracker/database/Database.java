package ch.isageek.tyderion.habittracker.database;

import android.content.Context;

import ch.isageek.tyderion.habittracker.model.DaoMaster;
import ch.isageek.tyderion.habittracker.model.DaoSession;

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
}
