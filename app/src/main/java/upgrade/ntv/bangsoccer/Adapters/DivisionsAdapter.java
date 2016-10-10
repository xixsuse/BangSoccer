package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Entities.Divisions;
import upgrade.ntv.bangsoccer.Utils.Preferences;

import static upgrade.ntv.bangsoccer.ActivityMain.mDivisions;


/**
 * Created by jfrom on 3/22/2016.
 */
public class DivisionsAdapter extends RecyclerView.Adapter<DivisionsAdapter.TeamHolder> {

    private List<Divisions> mDivisions = ActivityMain.getDivisionsList();
    private Context mContext;
    private int currentPos = 0;

    private onDivisionFragmentInteractionListener mListener ;
    public DivisionsAdapter(Context context) {
        this.mContext = context;
        mListener = (onDivisionFragmentInteractionListener) context;
    }


    public String getDivisionID(int position) {
        return ActivityMain.getDivisionsList().get(position).getFirebasekey();
    }

    public String getDivisionNode(int position) {
        return mDivisions.get(position).getNode();
    }

    @Override
    public int getItemCount() {
        return (this.mDivisions.size());
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_division_list, parent, false);
        return new TeamHolder(v);
    }


    @Override
    public void onBindViewHolder(final DivisionsAdapter.TeamHolder holder, int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vDivisionName.setText(mDivisions.get(position).getName());
        holder.Id = mDivisions.get(position).getFirebasekey();
        holder.cCheckBox.setChecked(Preferences.getPreferredDivisions(mContext, mDivisions.get(position).getNode()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAllDivisionChecks();
               // holder.cCheckBox.setChecked(!holder.cCheckBox.isChecked());
                String node = mDivisions.get(holder.getAdapterPosition()).getNode();
                String key =mDivisions.get(holder.getAdapterPosition()).getFirebasekey();
                mDivisions.get(currentPos).setSelected(true);

                if (!holder.cCheckBox.isChecked()) {

                    holder.cCheckBox.setChecked(true);
                    //saves the divisions to the shared preferences
                    Preferences.setPreferredDivisions(mContext, node);
                    mListener.onDivisionSelected(node);

                }else{
                    mListener.onDivisionUnselected(key);
                }

                notifyDataSetChanged();
            }
        });
    }

    public  void cleanAllDivisionChecks(){
        for (int i = 0 ; i < mDivisions.size() ; i++){
            mDivisions.get(i).setSelected(false);
            Preferences.removePreferredDivisions(mContext, mDivisions.get(i).getNode());
        }
    }

    public static class TeamHolder extends FavoritesViewHolder {

        String Id;
        @BindView(R.id.division_name_row)
        TextView vDivisionName;

        @BindView(R.id.checkBox)
        CheckBox cCheckBox;
        @BindView(R.id.division_row_cardview)
        CardView cardView;

        public TeamHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }
    public interface onDivisionFragmentInteractionListener {
        void onDivisionSelected(String node);
        void onDivisionUnselected(String divisionKey);
    }
}
