package ch.isageek.tyderion.habittracker.item;

import android.content.ClipData;
import android.content.Context;
import android.widget.ArrayAdapter;

import ch.isageek.tyderion.habittracker.model.Habit;

/**
 * Created by Archie on 18.04.2014.
 */
public class ItemAdapter extends ArrayAdapter<Habit> {


    public ItemAdapter(Context context, int resource, Habit[] objects) {
        super(context, resource, objects);
    }
}
