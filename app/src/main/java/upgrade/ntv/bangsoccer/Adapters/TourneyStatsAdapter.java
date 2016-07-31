package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;


/**
 * Created by jfrom on 3/22/2016.
 */
public class TourneyStatsAdapter extends RecyclerView.Adapter<TourneyStatsAdapter.TeamStatsHolder>{

    List<Club> mClubList;
    Context mContext;


    public TourneyStatsAdapter(List<Club> list, Context context) {
        this.mClubList = list;
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return this.mClubList.size();
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
        holder.vStatsTeamName.setText(mClubList.get(position).getName());
        Picasso.with(mContext).
                load("https://firebasestorage.googleapis.com/v0/b/bangsoccer-1382.appspot.com/o/MediaCancha%2Fprimera%2FTest_MediaCancha%252Fprimera%252Flogo-Inter-SD-100.jpg?alt=media&token=e3b35af3-691a-4f0b-8e11-f1c3707be92e").
                placeholder(R.drawable.ic_open_game_icon).
                into(holder.vTeamAvatar);
       // holder.vTeamAvatar.setImageResource(mClubList.get(position).getTeam_image());
        holder.vPJ.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vPG.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vPE.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vPP.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vGF.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vGC.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vDG.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vPoints.setText(String.valueOf((int) (Math.random() * ((9) + 1))));


        holder.Id = mClubList.get(position).getFirebasekey();

    }
        // Provides a reference to the views for each data item

    public static class TeamStatsHolder extends RecyclerView.ViewHolder{
        TextView vStatsTeamName;
        ImageView vTeamAvatar;
        TextView vPJ;
        TextView vPG;
        TextView vPE;
        TextView vPP;
        TextView vGF;
        TextView vGC;
        TextView vDG;
        TextView vPoints;
        String Id;

        public TeamStatsHolder(View v){
            super(v);
            vStatsTeamName = (TextView) v.findViewById(R.id.stats_team_name_text);
            vTeamAvatar = (ImageView) v.findViewById(R.id.stats_team_icon);
            vPJ = (TextView) v.findViewById(R.id.stats_pj_text);
            vPG = (TextView) v.findViewById(R.id.stats_pg_text);
            vPE = (TextView) v.findViewById(R.id.stats_pe_text);
            vPP = (TextView) v.findViewById(R.id.stats_pp_text);
            vGF = (TextView) v.findViewById(R.id.stats_gf_text);
            vGC = (TextView) v.findViewById(R.id.stats_gc_text);
            vDG = (TextView) v.findViewById(R.id.stats_dg_text);
            vPoints = (TextView) v.findViewById(R.id.stats_ptos_text);
        }
    }
}
