package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;


/**
 * Created by jfrom on 3/22/2016.
 */
public class ClubsToFollowAdapter extends RecyclerView.Adapter<ClubsToFollowAdapter.TeamHolder> {

    private List<Club> mClubList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private Query query;

    public ClubsToFollowAdapter(List<Club> list, Context context) {
        this.mClubList = list;
        this.mContext = context;
        this.query = ActivityMain.mTeamsRef;
        this.query.addChildEventListener(new ClubsToFollowAdapter.TeamEvenetListener());

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
    public String getClubID(int position) {
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
                .inflate(R.layout.row_follow_clubs_list, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new TeamHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClubsToFollowAdapter.TeamHolder holder,final int position) {
        // - get element from your dataset at this vTeamPosition
        // - replace the contents of the view with that element
        holder.vClubName.setText(mClubList.get(position).getName());

        Picasso.with(mContext).
                load(mClubList.get(position).getTeam_image()).
                placeholder(R.drawable.ic_upgraden).
                into(holder.vClubAvatar);

        holder.Id = mClubList.get(position).getFirebasekey();
        holder.cCheckBox.setChecked(mClubList.get(position).isFavorite());
        holder.vCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                holder.cCheckBox.setChecked(!holder.cCheckBox.isChecked());
                if(holder.cCheckBox.isChecked() == true){
                    mClubList.get(position).setFavorite(true);
                }else{
                    mClubList.get(position).setFavorite(false);
                }

            }
        });



    }
    // Provides a reference to the views for each data item

    public static class TeamHolder extends FavoritesViewHolder {
        private TextView vClubName;
        private ImageView vClubAvatar;
        private CheckBox cCheckBox;
        private CardView vCardView;
        private String Id;

        public TeamHolder(View v) {
            super(v);
            cCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
            vClubName = (TextView) v.findViewById(R.id.club_name_row);
            vClubAvatar = (ImageView) v.findViewById(R.id.club_image_row);
            vCardView = (CardView) v.findViewById(R.id.follow_club_row_cardview);
            //  v.setTag();

        }

        public CheckBox getCheckBox() {
            return this.cCheckBox;
        }

        public void setCheckBoxState(boolean set) {
            cCheckBox.setChecked(set);
        }
    }
}
