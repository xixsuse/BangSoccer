package upgrade.ntv.bangsoccer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jose on 3/14/2015.
 */
public class FragmentTeamInfo extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static FragmentTeamInfo newInstance(int sectionNumber) {
        FragmentTeamInfo fragment = new FragmentTeamInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTeamInfo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_info, container, false);
        Bundle arg = getArguments();
        int index = arg.getInt(ARG_SECTION_NUMBER);

       // mScheduleText.setText("Semana " + index);
        //  mListView = (ListView) rootView.findViewById(R.id.schedule_listview);
     //   mWeeklyAdapter = new ScheduleAdapter(index, getActivity());
     //   mListView.setAdapter(mWeeklyAdapter);
   /*     mMatchAdapter = new MatchAdapter(index, getActivity());


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.matches_recycleview);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mMatchAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/

        return rootView;
    }
}
