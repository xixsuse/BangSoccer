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

import upgrade.ntv.bangsoccer.Adapters.HistoryAdapter;
import upgrade.ntv.bangsoccer.Adapters.MatchAdapter;
import upgrade.ntv.bangsoccer.Decorators.DividerItemDecoration;

public class FragmentHistory extends Fragment {

    // TODO: Customize parameter argument names
    private static final String CLUB_ID = "club_id";
    private static final String DIVISION_ID = "division_id";
    private String mClubId;
    private String mDivision;
    private HistoryAdapter mHistoryAdapter;
    private OnFragmentHistoryInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentHistory() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentHistory newInstance(String clubId) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        args.putString(CLUB_ID, clubId);
        args.putString(DIVISION_ID, "Div1");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mClubId = getArguments().getString(CLUB_ID);
            mDivision = getArguments().getString(DIVISION_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryAdapter = new HistoryAdapter(getContext(), mClubId, mDivision);


        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mHistoryAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentHistoryInteractionListener) {
            mListener = (OnFragmentHistoryInteractionListener) context;
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

    public interface OnFragmentHistoryInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
