package upgrade.ntv.bangsoccer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.AppicationCore;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;

/**
 * Created by jfrom on 3/19/2016.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ScheduleHolder>{

   private List<Match> mClubsMatches;
    private Context mContext;

    private Query query;
    public MatchAdapter( Context context) {

        this.mClubsMatches = new ArrayList<>();
        this.mContext = context;
        this.query = AppicationCore.mMatchRef;
        this.query.addChildEventListener(new MatchEvenetListener());

    }

    public List<Match> getMatchList() {
        return mClubsMatches;
    }

    private class MatchEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Match firebaseRequest = dataSnapshot.getValue(Match.class);
            Club a = dataSnapshot.child("clubida").getValue(Club.class);
            Club b = dataSnapshot.child("clubidb").getValue(Club.class);
            firebaseRequest.getClubIdA().setFirebasekey(a.getFirebasekey());
            firebaseRequest.getClubIdB().setFirebasekey(b.getFirebasekey());
            firebaseRequest.setMatchId(dataSnapshot.getKey());
            getMatchList().add(0, firebaseRequest);

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


@Override
public int getItemCount() {
    return (this.mClubsMatches.size());
        }

@Override
public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_match, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new ScheduleHolder(v);
        }

@Override
public void onBindViewHolder(ScheduleHolder holder, final int position) {
        // - get element from your dataset at this vPlayerPosition
        // - replace the contents of the view with that element

    holder.day.setText(mClubsMatches.get(position).getDate());

    //     holder.team2.setText( mTeam.getmWeeklyMatch().get(vPlayerPosition).getTeamName(2));
    Picasso.with(mContext).
            load(mClubsMatches.get(position).getClubIdA().getTeam_image()).
            placeholder(R.drawable.ic_upgraden).
            into(holder.teamLogo1);
    //holder.teamLogo1.setImageResource(mClubsMatches.getmWeeklyMatch().get(position).getTeamImage(1));
    holder.teamLogo1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //calls the Team1 Screen based on the teamID
            Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
            String clubid=  mClubsMatches.get(position).getClubIdA().getFirebasekey();
            intent.putExtra("CLUBID", clubid);

            if (intent != null) {

               mContext.startActivity(intent);
               // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }

    });
    Picasso.with(mContext).
            load(mClubsMatches.get(position).getClubIdB().getTeam_image()).
            placeholder(R.drawable.ic_upgraden).
            into(holder.teamLogo2);

    holder.teamLogo2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //calls the Team2 Screen based on the teamID
            Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
            String clubid=  mClubsMatches.get(position).getClubIdA().getFirebasekey();
            intent.putExtra("CLUBID", clubid);

            if (intent != null) {

                mContext.startActivity(intent);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }

    });
    holder.day.setText(mClubsMatches.get(position).getDate());
    holder.stadium.setText(mClubsMatches.get(position).getStadium());

        }

// Provides a reference to the views for each data item
    public static class ScheduleHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView team2;
        ImageView teamLogo1;
        ImageView teamLogo2;
        TextView day;
        TextView stadium;

    public ScheduleHolder(View v){
        super(v);


        time = (TextView) v.findViewById(R.id.match_club_b_name);
       // team2 = (TextView) v.findViewById(R.id.team_2_id);
        teamLogo1= (ImageView) v.findViewById(R.id.image_team1);
        teamLogo2= (ImageView) v.findViewById(R.id.image_team2);
        day = (TextView) v.findViewById(R.id.match_club_a_name);
        stadium = (TextView) v.findViewById(R.id.stadium);

    }
}
}
