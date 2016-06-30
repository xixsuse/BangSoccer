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
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.TeamHolder> {

    private Team mTeam;
    private Context mContext;
    private LayoutInflater inflater;
    int mTeamID;

    public PlayersAdapter(int teamID, Context context) {

        this.mTeamID = teamID;
        this.mTeam = new Team(teamID);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

    }

    private List<TeamHolder> TeamItems;

    public PlayersAdapter(List<TeamHolder> teamItems) {
        this.TeamItems = teamItems;
    }

    @Override
    public int getItemCount() {
        return (this.mTeam.getmPlayersList().size());
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_player, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new TeamHolder(v);
    }

    public int getPlayerID(int id){
       return Integer.parseInt(mTeam.getmPlayersList().get(id).getmPlayerNum());
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.TeamHolder holder, int position) {
        // - get element from your dataset at this vPlayerPosition
        // - replace the contents of the view with that element

        holder.vPlayerName.setText(mTeam.getmPlayersList().get(position).getmPlayerName());
        holder.vPlayerNumber.setText(mTeam.getmPlayersList().get(position).getmPlayerNum());
        holder.vPlayerAvatar.setImageResource(mTeam.getmPlayersList().get(position).getmPlayer_Avatar());
        holder.vPlayerPosition.setText(mTeam.getmPlayersList().get(position).getmPosition());
        //holder.vPlayerTeamDivision.setText(mTeam.getmPlayersList().get(position).getmDivition());
        holder.vPlayerAlias.setText(mTeam.getmPlayersList().get(position).getmPlayerAlias());
    }

    // Provides a reference to the views for each data item


    public static class TeamHolder extends RecyclerView.ViewHolder {
        TextView vPlayerName;
        CircleImageView vPlayerAvatar;
        TextView vPlayerNumber;
        TextView vPlayerPosition;
        TextView vPlayerTeamDivision;
        TextView vPlayerAlias;

        public TeamHolder(View v) {
            super(v);

            vPlayerName = (TextView) v.findViewById(R.id.player_name);
            vPlayerAvatar = (CircleImageView) v.findViewById(R.id.player_avatar);
            vPlayerNumber = (TextView) v.findViewById(R.id.player_number);
            vPlayerPosition = (TextView) v.findViewById(R.id.player_text_position);
            vPlayerTeamDivision = (TextView) v.findViewById(R.id.player_text_division);
            vPlayerAlias = (TextView) v.findViewById(R.id.player_text_alias);
        }
    }
}
