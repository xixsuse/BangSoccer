package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Schedule.Team;


/**
 * Created by jfrom on 3/22/2016.
 */
public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.TeamHolder>{

    private List<Team> mTeamList;
    private Context mContext;


    public LeadersAdapter(List<Team> list, Context context) {
        this.mTeamList = list;
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return this.mTeamList.size();
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_leaders, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters
        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(LeadersAdapter.TeamHolder holder, int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vPlayerName.setText(mTeamList.get(2).getPlayer_list().get(1).getName());
        holder.vPlayerNumber.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
        holder.vPlayerAvatar.setImageResource(mTeamList.get(2).getPlayer_list().get(1).getAvatar());
        holder.vPlayerClub.setText(mTeamList.get(position).getName());

        holder.Id = mTeamList.get(position).getTeamid();

    }
        // Provides a reference to the views for each data item

    public static class TeamHolder extends RecyclerView.ViewHolder{
        TextView vPlayerName;
        CircleImageView vPlayerAvatar;
        TextView vPlayerNumber;
        TextView vPlayerClub;
        int Id;

        public TeamHolder(View v){
            super(v);
            vPlayerName = (TextView) v.findViewById(R.id.leaders_player_name);
            vPlayerAvatar = (CircleImageView) v.findViewById(R.id.leaders_player_avatar);
            vPlayerNumber = (TextView) v.findViewById(R.id.leaders_player_number);
            vPlayerClub = (TextView) v.findViewById(R.id.leaders_player_club);
        }
    }
}
