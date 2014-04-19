package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.database.Database;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurrence;

/**
 * Created by Archie on 11.04.2014.
 */
public class OccurrenceAdapter extends ArrayAdapter<Occurrence>{

    private Context context;
    private int layoutResourceId;
    private List<Occurrence> unfilteredResultList;
    private Habit habit;
//

    public OccurrenceAdapter(Context context, int resource, Habit habit) {
        super(context, resource, new ArrayList<Occurrence>());
        this.layoutResourceId = resource;
        this.context = context;
        setHabit(habit);
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
        if (habit != null) {
            reload();
        }
    }

    public void reload() {
        Database.asyncOccurrences(context,habit.getId(), new Database.DBCallback<List<Occurrence>>() {
            @Override
            public void onFinish(List<Occurrence> argument) {
                unfilteredResultList = new ArrayList<Occurrence>(argument);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return unfilteredResultList != null ?  unfilteredResultList.size() : 0;
    }

    @Override
    public Occurrence getItem(int position) {
        return unfilteredResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        OccurrenceHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new OccurrenceHolder(row);
            row.setTag(holder);
        } else {
            holder = (OccurrenceHolder) row.getTag();
        }
        Occurrence occurrence = this.getItem(position);
        holder.date.setText(occurrence.toString());
        return row;
    }



    @Override
    public void remove(Occurrence object) {
        this.unfilteredResultList.remove(object);
        notifyDataSetChanged();
        object.delete();
    }

    static class OccurrenceHolder {
        @InjectView(R.id.occurrence_item_date) TextView date;
        public OccurrenceHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
