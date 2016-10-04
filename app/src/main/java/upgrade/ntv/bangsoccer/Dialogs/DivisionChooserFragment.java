package upgrade.ntv.bangsoccer.Dialogs;

        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.RelativeLayout;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.Unbinder;
        import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
        import upgrade.ntv.bangsoccer.Decorators.DividerItemDecoration;
        import upgrade.ntv.bangsoccer.R;
        import upgrade.ntv.bangsoccer.RecyclerItemClickLister;
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


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
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
    public void onDetach() {
        super.onDetach();
    }


}