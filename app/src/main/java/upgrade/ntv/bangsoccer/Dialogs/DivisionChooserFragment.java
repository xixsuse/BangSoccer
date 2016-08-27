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
        import upgrade.ntv.bangsoccer.R;
        import upgrade.ntv.bangsoccer.TournamentObjects.Divisions;

/**
 * Created by jfro on 8/25/16.
 */

public class DivisionChooserFragment extends DialogFragment  {

    //TAG
    private static final String TAG = DivisionChooserFragment.class.getSimpleName();

    private Unbinder unbinder;
    @BindView(R.id.divisions_recyclerview)
    RecyclerView recyclerView;

    public List<Divisions> divisionsSelect = new ArrayList<>();
    private DivisionsAdapter divisionsAdapter;
    private GridLayoutManager lLayout;

    private OnCreateClientDialogListener mListener;

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

        return view;
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateClientDialogListener) {
            mListener = (OnCreateClientDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }




    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface OnCreateClientDialogListener {
        void callDivisionsDialog();
    }
}