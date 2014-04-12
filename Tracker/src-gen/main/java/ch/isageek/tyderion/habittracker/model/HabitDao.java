package ch.isageek.tyderion.habittracker.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import ch.isageek.tyderion.habittracker.model.Habit;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table HABIT.
*/
public class HabitDao extends AbstractDao<Habit, Long> {

    public static final String TABLENAME = "HABIT";

    /**
     * Properties of entity Habit.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id =new Property(0, Long.class , "id", true, "_id");
        public final static Property DateCreated =new Property(1, java.util.Date.class , "dateCreated", false, "DATE_CREATED");
        public final static Property Name =new Property(2, String.class , "name", false, "NAME");
        public final static Property IsPositive =new Property(3, Boolean.class , "isPositive", false, "IS_POSITIVE");
        public final static Property Description =new Property(4, String.class , "description", false, "DESCRIPTION");
        public final static Property Uuid =new Property(5, String.class , "uuid", false, "UUID");
    };

    private DaoSession daoSession;


    public HabitDao(DaoConfig config) {
        super(config);
    }

    public HabitDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HABIT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DATE_CREATED' INTEGER," + // 1: dateCreated
                "'NAME' TEXT UNIQUE ," + // 2: name
                "'IS_POSITIVE' INTEGER," + // 3: isPositive
                "'DESCRIPTION' TEXT," + // 4: description
                "'UUID' TEXT);"); // 5: uuid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HABIT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Habit entity) {
        stmt.clearBindings();
        entity.onBeforeSave();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);

        }
 
        java.util.Date dateCreated = entity.getDateCreated();
        if (dateCreated != null) {
            stmt.bindLong(2, dateCreated.getTime());

        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);

        }
 
        Boolean isPositive = entity.getIsPositive();
        if (isPositive != null) {
            stmt.bindLong(4, isPositive ? 1l: 0l);

        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(5, description);

        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(6, uuid);

        }
    }

    @Override
    protected void attachEntity(Habit entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Habit readEntity(Cursor cursor, int offset) {
        Habit entity = new Habit( //

            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0) , // id
            cursor.isNull(offset + 1) ? null : new java.util.Date( cursor.getLong(offset + 1) ) , // dateCreated
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) , // name
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0 , // isPositive
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) , // description
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // uuid
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Habit entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0) );
        entity.setDateCreated(cursor.isNull(offset + 1) ? null : new java.util.Date( cursor.getLong(offset + 1) ) );
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) );
        entity.setIsPositive(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0 );
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) );
        entity.setUuid(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) );
     }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Habit entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(Habit entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
