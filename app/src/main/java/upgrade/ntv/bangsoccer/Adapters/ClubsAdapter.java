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

import upgrade.ntv.bangsoccer.AppicationCore;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;


/**
 * Created by jfrom on 3/22/2016.
 */
public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.TeamHolder>{

    private List<Club> mClubList = new ArrayList<>();
    private Context mContext;
    private Query query;

    public ClubsAdapter(Context context) {
        this.mContext = context;
        this.query = AppicationCore.mTeamsRef;
        this.query.addChildEventListener(new ClubsAdapter.TeamEvenetListener());

    }

    public List<Club> getClubList() {
        return mClubList;
    }

    private class TeamEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Club firebaseRequest = dataSnapshot.getValue(Club.class);

            firebaseRequest.setFirebasekey(dataSnapshot.getKey());
            getClubList().add(0, firebaseRequest);

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


    public String getClubID(int position){
        return mClubList.get(position).getFirebasekey();
    }

    @Override
    public int getItemCount() {
        return (this.mClubList.size());
    }


    @Override
    public TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_clubs_list, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters
//TODO: create listener in the activity and pass it to the adapter


        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(ClubsAdapter.TeamHolder holder, int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vClubName.setText(mClubList.get(position).getName());

        Picasso.with(mContext).
                load(mClubList.get(position).getTeam_image()).
                placeholder(R.drawable.ic_upgraden).
                into(holder.vClubAvatar);

        holder.Id = mClubList.get(position).getFirebasekey();


    }
        // Provides a reference to the views for each data item

    public static class TeamHolder extends RecyclerView.ViewHolder{
        TextView vClubName;
        ImageView vClubAvatar;
        String Id;

        public TeamHolder(View v){
            super(v);

            vClubName = (TextView) v.findViewById(R.id.club_name_row);
            vClubAvatar = (ImageView) v.findViewById(R.id.club_image_row);

        }
    }
}
