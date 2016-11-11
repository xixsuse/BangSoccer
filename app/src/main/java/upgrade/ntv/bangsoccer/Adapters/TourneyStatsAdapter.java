package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.Entities.StatisticTable;
import upgrade.ntv.bangsoccer.R;


/**
 * Created by jfrom on 3/22/2016.
 */
public class TourneyStatsAdapter extends RecyclerView.Adapter<TourneyStatsAdapter.TeamStatsHolder>{

    private List<StatisticTable> mStatisticsList;
    private  Context mContext;


    public TourneyStatsAdapter( Context context) {
        this.mStatisticsList = new ArrayList<>();
        this.mContext = context;
        Query query = ActivityMain.mDiv1StatsTableRef;
        query.addChildEventListener(new TableEvenetListener());
    }

    @Override
    public int getItemCount() {
        return this.mStatisticsList.size();
    }

    @Override
    public TeamStatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stats, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters
        return new TeamStatsHolder(v);
    }

    @Override
    public void onBindViewHolder(TourneyStatsAdapter.TeamStatsHolder holder, int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vStatsTeamName.setText(mStatisticsList.get(position).getName());
        Picasso.with(mContext).
                load(mStatisticsList.get(position).getImageurl()).
                placeholder(R.drawable.ic_open_game_icon).
                into(holder.vTeamAvatar);
       // holder.vTeamAvatar.setImageResource(mClubList.get(position).getTeam_image());
        holder.vPJ.setText(String.valueOf(mStatisticsList.get(position).getPj()));
        holder.vPG.setText(String.valueOf(mStatisticsList.get(position).getPg()));
        holder.vPE.setText(String.valueOf(mStatisticsList.get(position).getPe()));
        holder.vPP.setText(String.valueOf(mStatisticsList.get(position).getPp()));
        holder.vGF.setText(String.valueOf(mStatisticsList.get(position).getGf()));
        holder.vGC.setText(String.valueOf(mStatisticsList.get(position).getGc()));
        holder.vDG.setText(String.valueOf(mStatisticsList.get(position).getDg()));
        holder.vPoints.setText(String.valueOf(mStatisticsList.get(position).getPoints()));


        holder.Id = mStatisticsList.get(position).getID();

    }

    static class TeamStatsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.stats_team_name_text)
        TextView vStatsTeamName;
        @BindView(R.id.stats_team_icon)
        ImageView vTeamAvatar;
        @BindView(R.id.stats_pj_text)
        TextView vPJ;
        @BindView(R.id.stats_pg_text)
        TextView vPG;
        @BindView(R.id.stats_pe_text)
        TextView vPE;
        @BindView(R.id.stats_pp_text)
        TextView vPP;
        @BindView(R.id.stats_gf_text)
        TextView vGF;
        @BindView(R.id.stats_gc_text)
        TextView vGC;
        @BindView(R.id.stats_dg_text)
        TextView vDG;
        @BindView(R.id.stats_ptos_text)
        TextView vPoints;

        String Id;

        TeamStatsHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    //firebase event listener
    private class TableEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            StatisticTable firebaseRequest = dataSnapshot.getValue(StatisticTable.class);
            firebaseRequest.setID(dataSnapshot.getKey());
            mStatisticsList.add(firebaseRequest);
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
}
