package ch.isageek.tyderion.habittracker.model;


import de.greenrobot.dao.DaoException;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
// KEEP INCLUDES END
/**
 * Entity mapped to table HABIT.
 */
abstract public class HabitBase implements Parcelable {

    protected Long id;
    protected java.util.Date dateCreated;
    protected String name;
    protected Boolean isPositive;
    protected String description;
    protected String uuid;




    /** Used to resolve relations */
    protected transient DaoSession daoSession;

    /** Used for active entity operations. */
    protected transient HabitDao myDao;

    protected List<Occurrence> occurrenceList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public HabitBase() {
    }

    public HabitBase(Long id) {
        this.id = id;
    }

    public HabitBase(Long id, java.util.Date dateCreated, String name, Boolean isPositive, String description, String uuid) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.name = name;
        this.isPositive = isPositive;
        this.description = description;
        this.uuid = uuid;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHabitDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(Boolean isPositive) {
        this.isPositive = isPositive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public synchronized List<Occurrence> getOccurrenceList() {
        if (occurrenceList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OccurrenceDao targetDao = daoSession.getOccurrenceDao();
            occurrenceList = targetDao._queryHabit_OccurrenceList(id);
        }
        return occurrenceList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetOccurrenceList() {
        occurrenceList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete((Habit)this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update((Habit)this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh((Habit)this);
    }

    public void updateNotNull(Habit other) {
        if(this == other) {
            return;//both came from db, no need to run this.
        }

        if(other.id != null) {
            this.id = other.id;
        }


        if(other.dateCreated != null) {
            this.dateCreated = other.dateCreated;
        }


        if(other.name != null) {
            this.name = other.name;
        }


        if(other.isPositive != null) {
            this.isPositive = other.isPositive;
        }


        if(other.description != null) {
            this.description = other.description;
        }


        if(other.uuid != null) {
            this.uuid = other.uuid;
        }

        // relationships
        if(other.getOccurrenceList() != null) {
            occurrenceList = (other.getOccurrenceList());
        }
    }


    // KEEP METHODS - put your custom methods here


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Habit> CREATOR  = new Parcelable.Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel parcel) {
            return new Habit(parcel);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    public HabitBase(Parcel parcel) {
        id = parcel.readLong();
        dateCreated = new Date(parcel.readLong());
        name = parcel.readString();
        isPositive = parcel.readInt() == 1;
        description = parcel.readString();
        uuid = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeLong(dateCreated.getTime());
        parcel.writeString(name);
        parcel.writeInt(isPositive ? 1 : 0);
        parcel.writeString(description);
        parcel.writeString(uuid);
    }



    // KEEP METHODS END


    public void onBeforeSave() {
        //you can override this method and do some stuff if you want to :)

    }
}
