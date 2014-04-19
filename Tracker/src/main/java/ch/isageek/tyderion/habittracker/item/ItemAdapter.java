package ch.isageek.tyderion.habittracker.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.isageek.tyderion.habittracker.model.Habit;

/**
 * Created by Archie on 18.04.2014.
 */
public class ItemAdapter extends ArrayAdapter<Habit> {
    Context myContext;
    int layoutResourceId;
    int textViewResourceID;
    ArrayList<Habit> unfilteredResultList = null;
    ArrayList<Habit> filteredResultList = null;
    private Filter filter;

    private String filterString = "";


    private boolean isFiltering() {
        return !filterString.equals("");
    }

    private ArrayList<Habit> getCurrentData() {
        return isFiltering() ? filteredResultList : unfilteredResultList;
    }

    public ItemAdapter(Context context, int resource, int textViewResourceId, List<Habit> objects) {
        super(context,resource, textViewResourceId, objects);
        this.myContext = context;
        this.layoutResourceId = resource;
        this.textViewResourceID = textViewResourceId;

        this.unfilteredResultList = new ArrayList<Habit>(objects);
        this.filteredResultList = new ArrayList<Habit>(objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);
        Habit habit = getCurrentData().get(position);
        ((TextView)view.findViewById(textViewResourceID)).setText(habit.getName());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ResultFilter();
        }
        return filter;
    }

    @Override
    public Habit getItem(int position) {
        return getCurrentData().get(position);
    }

    @Override
    public int getCount() {
        return getCurrentData().size();
    }

    private class ResultFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.count = unfilteredResultList.size();
                results.values = unfilteredResultList;
            } else {
                String filterText = constraint.toString().toLowerCase();
                ArrayList<Habit> filteredHabits = new ArrayList<Habit>();
                for (Habit habit : unfilteredResultList) {
                    if (habit.getName().toLowerCase().contains(filterText)) {
                        filteredHabits.add(habit);
                    }
                }
                results.count = filteredHabits.size();
                results.values = filteredHabits;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Habit> list = (ArrayList<Habit>)filterResults.values;
            if (charSequence != null && charSequence.length() > 0) {
                filteredResultList = list;
            }
            filterString = charSequence != null ? charSequence.toString() : "";
            notifyDataSetChanged();
        }
    }
}
