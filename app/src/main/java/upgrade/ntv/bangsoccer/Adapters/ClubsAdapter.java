package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Schedule.Team;


/**
 * Created by jfrom on 3/22/2016.
 */
public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.TeamHolder>{

    List<Team> mTeamList;
    Context mContext;
    LayoutInflater inflater ;

    public ClubsAdapter(List<Team> list, Context context) {
        this.mTeamList = list;
        this.mContext = context;

    }

    private List<Team> TeamItems;

    public ClubsAdapter(List<Team> teamItems){


        this.TeamItems = teamItems;
    }

    public int getClubID(int position){
        return mTeamList.get(position).getTeamid();
    }

    @Override
    public int getItemCount() {
        return (this.mTeamList.size());
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_clubs_list, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(ClubsAdapter.TeamHolder holder, int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vClubName.setText(mTeamList.get(position).getName());
        holder.vClubAvatar.setImageResource(mTeamList.get(position).getTeam_image());
        holder.Id = mTeamList.get(position).getTeamid();


    }
        // Provides a reference to the views for each data item

    public static class TeamHolder extends RecyclerView.ViewHolder{
        TextView vClubName;
        ImageView vClubAvatar;
        int Id;

        public TeamHolder(View v){
            super(v);

            vClubName = (TextView) v.findViewById(R.id.club_name_row);
            vClubAvatar = (ImageView) v.findViewById(R.id.club_image_row);

        }
    }
}
