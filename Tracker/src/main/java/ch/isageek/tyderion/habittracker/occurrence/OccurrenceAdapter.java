package ch.isageek.tyderion.habittracker.occurrence;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;
import ch.isageek.tyderion.habittracker.model.Occurence;

/**
 * Created by Archie on 11.04.2014.
 */
public class OccurrenceAdapter extends ArrayAdapter<Occurence>{

    private Context context;
    private int layoutResourceId;
//

    public OccurrenceAdapter(Context context, int resource, List<Occurence> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
    }

    public OccurrenceAdapter(Context context, int resource, int textViewResourceId, Occurence[] objects) {
        super(context, resource, textViewResourceId, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        OccurrenceHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new OccurrenceHolder();
            holder.date = (TextView) row.findViewById(R.id.occurrence_item_date);

            row.setTag(holder);
        } else {
            holder = (OccurrenceHolder) row.getTag();
        }
        Occurence occurence = this.getItem(position);
        holder.date.setText(occurence.toString());
        return row;
    }



    static class OccurrenceHolder {
        TextView date;
    }
}
