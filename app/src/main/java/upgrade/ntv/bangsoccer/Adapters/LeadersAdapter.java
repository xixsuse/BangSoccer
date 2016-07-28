package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
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

import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.AppicationCore;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Schedule.Club;
import upgrade.ntv.bangsoccer.Schedule.Players;


/**
 * Created by jfrom on 3/22/2016.
 */
public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.TeamHolder> {

    public List<Players> mPlayersLeader = new ArrayList<Players>();;
    private Context mContext;
    private Query queryLeaders;

    public LeadersAdapter(Context context) {

        this.mContext = context;
        queryLeaders = AppicationCore.mPlayersDeftailsRef;
        queryLeaders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Players firebaseRequest = dataSnapshot.getValue(Players.class);
                firebaseRequest.setmFireBaseKey(dataSnapshot.getKey());
                mPlayersLeader.add(0, firebaseRequest);
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
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        if(mPlayersLeader.size() >0) {
            holder.vPlayerName.setText(mPlayersLeader.get(position).getName());
            holder.vPlayerNumber.setText(String.valueOf((int) (Math.random() * ((5) + 1))));
            holder.vPlayerAvatar.setImageResource(R.drawable.ic_player_name_icon);
            Club club = new Club();
            holder.vPlayerClub.setText(club.getName());
            holder.Id = mPlayersLeader.get(position).getTeamid();
        }
    }

    // Provides a reference to the views for each data item
    public static class TeamHolder extends RecyclerView.ViewHolder {
        TextView vPlayerName;
        CircleImageView vPlayerAvatar;
        TextView vPlayerNumber;
        TextView vPlayerClub;
        String Id;

        public TeamHolder(View v) {
            super(v);
            vPlayerName = (TextView) v.findViewById(R.id.leaders_player_name);
            vPlayerAvatar = (CircleImageView) v.findViewById(R.id.leaders_player_avatar);
            vPlayerNumber = (TextView) v.findViewById(R.id.leaders_player_number);
            vPlayerClub = (TextView) v.findViewById(R.id.leaders_player_club);
        }
    }
}
