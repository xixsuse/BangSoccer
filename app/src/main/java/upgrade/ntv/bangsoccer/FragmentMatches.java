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
import upgrade.ntv.bangsoccer.Entities.Day;

/**
 * Created by Jose on 3/14/2015.
 */
public class FragmentMatches extends Fragment {

    private static final String ARG_GAMES = "games";
    private static final String ARG_IS_CURRENT = "Selected";

    private MatchAdapter mMatchAdapter;
    private Day gamesOfTheDay = new Day();
    private boolean isCurrent = false;

    public static FragmentMatches newInstance(Day games, boolean isInCurrentWeek) {
        FragmentMatches fragment = new FragmentMatches();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CURRENT, isInCurrentWeek);
        args.putParcelable(ARG_GAMES, games);

        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMatches newInstance() {
        return  new FragmentMatches();
    }


    public FragmentMatches() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_matches_content, container, false);
        Bundle arg = getArguments();

            gamesOfTheDay = arg.getParcelable(ARG_GAMES);
            isCurrent = arg.getBoolean(ARG_IS_CURRENT);

            mMatchAdapter = new MatchAdapter(getActivity(), gamesOfTheDay);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.matches_recycleview);
            recyclerView.setHasFixedSize(true);


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(mMatchAdapter);



        return rootView;
    }
}
