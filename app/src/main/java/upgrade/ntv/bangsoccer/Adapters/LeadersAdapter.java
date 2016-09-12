package upgrade.ntv.bangsoccer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Players;


/**
 * Created by jfrom on 3/22/2016.
 */
public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.TeamHolder> {

    public List<Players> mPlayersLeader = new ArrayList<>();
    private Query queryLeaders;

    public LeadersAdapter() {

        queryLeaders = ActivityMain.mPlayersDeftailsRef;
        queryLeaders.orderByChild("goals");
        queryLeaders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Players firebaseRequest = dataSnapshot.getValue(Players.class);
                firebaseRequest.setmFireBaseKey(dataSnapshot.getKey());
                mPlayersLeader.add(firebaseRequest);
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
        });
    }


    @Override
    public int getItemCount() {
        return this.mPlayersLeader.size();
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
        if(mPlayersLeader.size() >0) {
            Players player = mPlayersLeader.get(position);
            holder.vPlayerName.setText(player.getName());
            holder.vPlayerGoals.setText(String.valueOf(player.getGoals()));
            holder.vPlayerAvatar.setImageResource(R.drawable.ic_player_name_icon);
            holder.vPlayerClub.setText(player.getTeam());
            holder.Id = mPlayersLeader.get(position).getTeamid();
        }
    }

    // Provides a reference to the views for each data item
    public static class TeamHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leaders_player_name)
        TextView vPlayerName;
        @BindView(R.id.leaders_player_avatar)
        CircleImageView vPlayerAvatar;
        @BindView(R.id.leaders_player_goal)
        TextView vPlayerGoals;
        @BindView(R.id.leaders_player_club)
        TextView vPlayerClub;
        String Id;

        public TeamHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
