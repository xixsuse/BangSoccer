package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Divisions;
import upgrade.ntv.bangsoccer.Utils.Preferences;


/**
 * Created by jfrom on 3/22/2016.
 */
public class DivisionsAdapter extends RecyclerView.Adapter<DivisionsAdapter.TeamHolder> {

    private List<Divisions> mDivisions = ActivityMain.getDivisionsList();
    private Context mContext;
    private int currentPos = 0;

    public DivisionsAdapter(Context context) {
        this.mContext = context;
    }


    public String getDivisionID(int position) {
        return ActivityMain.getDivisionsList().get(position).getFirebasekey();
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
    public void onBindViewHolder(final DivisionsAdapter.TeamHolder holder, final int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vDivisionName.setText(mDivisions.get(position).getName());
        holder.Id = mDivisions.get(position).getFirebasekey();
        holder.cCheckBox.setChecked(Preferences.getPreferredDivisions(mContext, mDivisions.get(position).getNode()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cCheckBox.setChecked(!holder.cCheckBox.isChecked());
                if (holder.cCheckBox.isChecked() == true) {

                    mDivisions.get(currentPos).setSelected(true);
                    //saves the divisions to the shared preferences
                    Preferences.setPreferredDivisions(mContext, mDivisions.get(position).getNode());

                } else {
                    mDivisions.get(currentPos).setSelected(false);
                    //removes the divisions from the shared preferences
                    Preferences.removePreferredDivisions(mContext,mDivisions.get(position).getNode());
                }

            }
        });
    }

    public static class TeamHolder extends FavoritesViewHolder {
        @BindView(R.id.division_name_row)
        TextView vDivisionName;
        String Id;
        @BindView(R.id.checkBox)
        CheckBox cCheckBox;
        @BindView(R.id.division_row_cardview)
        CardView cardView;

        public TeamHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }
}
