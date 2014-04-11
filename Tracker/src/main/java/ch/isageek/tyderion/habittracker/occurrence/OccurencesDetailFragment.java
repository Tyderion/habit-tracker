package ch.isageek.tyderion.habittracker.occurrence;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ch.isageek.tyderion.habittracker.R;
import ch.isageek.tyderion.habittracker.model.Occurence;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class OccurencesDetailFragment extends Fragment {

    private OccurencesDetailDataSource dataSource;
    private List<Occurence> occurenceList;
    private ListView occurencesListView;

    public OccurencesDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dataSource = (OccurencesDetailDataSource) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OccurencesDetailDataSource");
        }

        this.occurenceList = dataSource.getOccurrenceList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_occurences_detail, container, false);
        occurencesListView = (ListView)view.findViewById(R.id.occurences_listview);
        occurencesListView.setAdapter(new OccurrenceAdapter(getActivity(),R.layout.occerrences_detail_list_item_row, this.occurenceList));
        return view;
    }


    public interface OccurencesDetailDataSource {
        public List<Occurence> getOccurrenceList();
    }


}
