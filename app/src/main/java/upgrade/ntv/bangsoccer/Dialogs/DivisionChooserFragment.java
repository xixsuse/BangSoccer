package upgrade.ntv.bangsoccer.Dialogs;

        import android.app.DialogFragment;
        import android.content.Context;
        import android.os.Bundle;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import java.util.ArrayList;
        import java.util.List;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.Unbinder;
        import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
        import upgrade.ntv.bangsoccer.Decorators.DividerItemDecoration;
        import upgrade.ntv.bangsoccer.FragmentViewPagerContainer;
        import upgrade.ntv.bangsoccer.R;
        import upgrade.ntv.bangsoccer.RecyclerItemClickLister;
        import upgrade.ntv.bangsoccer.TournamentObjects.Divisions;
        import upgrade.ntv.bangsoccer.Utils.Preferences;

        import static upgrade.ntv.bangsoccer.ActivityMain.mDivisions;

/**
 * Created by jfro on 8/25/16.
 */

public class DivisionChooserFragment extends DialogFragment  {

    //TAG
    private static final String TAG = DivisionChooserFragment.class.getSimpleName();

    private Unbinder unbinder;
    @BindView(R.id.divisions_recyclerview)
    RecyclerView recyclerView;

    private DivisionsAdapter divisionsAdapter;
    private GridLayoutManager lLayout;


    private onDivisionFragmentInteractionListener mListener ;

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_division_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lLayout = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(lLayout);

        divisionsAdapter = new DivisionsAdapter(getActivity());
        recyclerView.setAdapter(divisionsAdapter);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));


        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(getActivity(), recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // mPlayerAdapter.getPlayerID(position);
                //  String x =  mPlayerAdapter.getPlayerId(position);
                if(!Preferences.getPreferredDivisions(getActivity(), mDivisions.get(position).getNode())){
                    mListener.onDivisionSelected(divisionsAdapter.getDivisionNode(position));
                }else {
                    mListener.onDivisionUnselected(divisionsAdapter.getDivisionID(position));
                }



            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));


        return view;
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof onDivisionFragmentInteractionListener) {
            mListener = (onDivisionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDivisionDialogListener");
        }
    }




    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }


    public interface onDivisionFragmentInteractionListener {
        void onDivisionSelected(String node);
        void onDivisionUnselected(String divisionKey);
    }
}