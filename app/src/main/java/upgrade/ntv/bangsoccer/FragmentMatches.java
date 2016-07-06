package upgrade.ntv.bangsoccer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upgrade.ntv.bangsoccer.Adapters.MatchAdapter;

/**
 * Created by Jose on 3/14/2015.
 */
public class FragmentMatches extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    MatchAdapter mMatchAdapter;

    public static FragmentMatches newInstance(int sectionNumber) {
        FragmentMatches fragment = new FragmentMatches();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMatches() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_matches_content, container, false);
        Bundle arg = getArguments();
        int index = arg.getInt(ARG_SECTION_NUMBER);

       // mScheduleText.setText("Semana " + index);
        //  mListView = (ListView) rootView.findViewById(R.id.schedule_listview);
     //   mWeeklyAdapter = new ScheduleAdapter(index, getActivity());
     //   mListView.setAdapter(mWeeklyAdapter);
        mMatchAdapter = new MatchAdapter(index, getActivity());


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.matches_recycleview);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mMatchAdapter);/*
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/

        return rootView;
    }
}
