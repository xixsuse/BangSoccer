package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.LeadersAdapter;
import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.Schedule.Team;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentLeaders extends Fragment {


    private List<Team> clubItems = new ArrayList<>();

    // TODO: Customize parameter argument names
    private static final String TEAM_ID = "team-id";
    private int mTeamID;
    private LeadersAdapter mLeadersAdapter;
   private Context mContext;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentLeaders() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentLeaders newInstance() {
        FragmentLeaders fragment = new FragmentLeaders();
     /*   Bundle args = new Bundle();
        args.putInt(TEAM_ID, teamId);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateDummyClubsItems();
        if (getArguments() != null) {
            mTeamID = getArguments().getInt(TEAM_ID);
        }


    }

    //dummy data for the global news feed
    public void populateDummyClubsItems() {
        for (int i = 0; i < AppConstant.mTeamArrayList.length; i++) {
            Team myTeam = new Team(i);
            clubItems.add(myTeam);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaders, container, false);

        mLeadersAdapter = new LeadersAdapter(clubItems, getActivity());


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mLeadersAdapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(getContext(), recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // mPlayerAdapter.getPlayerID(position);
                mListener.onListFragmentInteraction();


            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}