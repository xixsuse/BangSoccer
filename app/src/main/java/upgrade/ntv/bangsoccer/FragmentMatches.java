package upgrade.ntv.bangsoccer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import upgrade.ntv.bangsoccer.Adapters.MatchAdapter;
import upgrade.ntv.bangsoccer.TournamentObjects.Day;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;

/**
 * Created by Jose on 3/14/2015.
 */
public class FragmentMatches extends Fragment {

    private static final String ARG_GAMES = "games";

    private MatchAdapter mMatchAdapter;
    private Day gamesOfTheDay = new Day();

    public static FragmentMatches newInstance(Day games) {
        FragmentMatches fragment = new FragmentMatches();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAMES, games);
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

            gamesOfTheDay = arg.getParcelable(ARG_GAMES);

            mMatchAdapter = new MatchAdapter(getActivity(), gamesOfTheDay);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.matches_recycleview);
            recyclerView.setHasFixedSize(true);


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(mMatchAdapter);



        return rootView;
    }
}
