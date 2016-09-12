package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.AppicationCore;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Players;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

/**
 * Created by jfrom on 3/22/2016.
 */
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.TeamHolder> {

    private Club mClub;
    //private Firebase mPlayerFireBase;

    private String mTeamID;
    private Query query;

    public PlayersAdapter(String teamid) {

        this.mTeamID = teamid;
        this.mClub = new Club();
        query = ActivityMain.mPlayersDeftailsRef;
        query.addChildEventListener(new PlayerEvenetListener());
    }

    //firebase event listener
    private class PlayerEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Players firebaseRequest = dataSnapshot.getValue(Players.class);

            String playerTeamId = firebaseRequest.getTeamid();
            if (playerTeamId.equals(mTeamID)) {

                firebaseRequest.setmFireBaseKey(dataSnapshot.getKey());
                mClub.getPlayer_list().add(0, firebaseRequest);
            }
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

    public String getPlayerId(int position) {
        return this.mClub.getPlayer(position).getmFireBaseKey();
    }

    @Override
    public int getItemCount() {
        return (this.mClub.getPlayer_list().size());
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_player, parent, false);
        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.TeamHolder holder, int position) {

        //get player
        Players player = mClub.getPlayer_list().get(position);
        //populate holder
        holder.vPlayerName.setText(player.getName());
        holder.vPlayerNumber.setText(String.valueOf(player.getNumber()));
        holder.vPlayerAvatar.setImageResource(R.drawable.ic_player_icon);
        holder.vPlayerPosition.setText(player.getPosition());
        holder.vPlayerTeamDivision.setText(player.getDivision());
        holder.vPlayerAlias.setText(player.getAlias());
    }

    // Provides a reference to the views for each data item
    public static class TeamHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.player_name)
        TextView vPlayerName;
        @BindView(R.id.player_avatar)
        CircleImageView vPlayerAvatar;
        @BindView(R.id.player_number)
        TextView vPlayerNumber;
        @BindView(R.id.player_text_position)
        TextView vPlayerPosition;
        @BindView(R.id.player_text_division)
        TextView vPlayerTeamDivision;
        @BindView(R.id.player_text_alias)
        TextView vPlayerAlias;

        public TeamHolder(View v) {
            super(v);
            //butter knife bind
            ButterKnife.bind(this, v);
        }
    }


}
