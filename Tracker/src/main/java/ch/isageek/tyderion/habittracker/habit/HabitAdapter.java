package ch.isageek.tyderion.habittracker.habit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Habit;

/**
 * Created by Archie on 11.04.2014.
 */
public class HabitAdapter extends ArrayAdapter<Habit>{

    Context context;
    int layoutResourceId;
    List<Habit> data;


    public HabitAdapter(Context context, int resource, List<Habit> objects) {
        super(context, resource, objects);
        this.data = objects;
        this.layoutResourceId = resource;
        this.context = context;
    }

    public HabitAdapter(Context context, int resource, int textViewResourceId, Habit[] objects) {
        super(context, resource, textViewResourceId, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HabitHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HabitHolder();
            holder.title = (TextView) row.findViewById(R.id.habit_item_text_title);
            holder.description = (TextView) row.findViewById(R.id.habit_item_text_description);
            holder.image = (ImageView) row.findViewById(R.id.habit_item_image);

            row.setTag(holder);
        } else {
            holder = (HabitHolder) row.getTag();
        }

        Habit habit = data.get(position);
        holder.title.setText( habit.getName());
        holder.description.setText(habit.getDescription());
        int color = this.context.getResources().getColor(habit.getIsPositive() ? R.color.green : R.color.red);
        holder.image.setBackgroundColor(color);
//        holder.description.setBackgroundColor(color);
        return row;
    }


    public Habit getHabit(int position) {
        if (this.data.size() > position)  {
            return this.data.get(position);
        } else {
            return null;
        }
    }


    static class HabitHolder {
        TextView title;
        TextView description;
        ImageView image;
    }
}
