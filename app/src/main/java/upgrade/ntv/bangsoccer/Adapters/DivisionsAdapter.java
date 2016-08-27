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


/**
 * Created by jfrom on 3/22/2016.
 */
public class DivisionsAdapter extends RecyclerView.Adapter<DivisionsAdapter.TeamHolder> {

    private List<Divisions> mDivisions = new ArrayList<>();
    private Context mContext;
    private Query query;
    private int currentPos = 0;

    public DivisionsAdapter(Context context) {
        this.mContext = context;
        this.query = ActivityMain.mDivisionsRef;
        this.query.addChildEventListener(new DivisionEvenetListener());

    }


    public List<Divisions> getDivisionsList() {
        return mDivisions;
    }

    private class DivisionEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Divisions firebaseRequest = dataSnapshot.getValue(Divisions.class);
            firebaseRequest.setFirebasekey(dataSnapshot.getKey());
            getDivisionsList().add(0, firebaseRequest);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    public String getDivisionID(int position) {
        return mDivisions.get(position).getFirebasekey();
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
        // set the view's size, margins, paddings and app_bar_teams parameters
        //TODO: create listener in the activity and pass it to the adapter


        return new TeamHolder(v);
    }


    @Override
    public void onBindViewHolder(final DivisionsAdapter.TeamHolder holder, final int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vDivisionName.setText(mDivisions.get(position).getName());
        holder.Id = mDivisions.get(position).getFirebasekey();
        holder.cCheckBox.setChecked(mDivisions.get(position).isSelected());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cCheckBox.setChecked(!holder.cCheckBox.isChecked());
                if (holder.cCheckBox.isChecked() == true) {

                    mDivisions.get(currentPos).setSelected(true);
                } else {
                    mDivisions.get(currentPos).setSelected(false);
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
