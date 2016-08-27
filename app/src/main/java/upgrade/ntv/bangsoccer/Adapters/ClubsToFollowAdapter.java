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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;


/**
 * Created by jfrom on 3/22/2016.
 */
public class ClubsToFollowAdapter extends RecyclerView.Adapter<ClubsToFollowAdapter.TeamHolder> {

    private List<Club> mClubList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private List<View> itemHolder = new ArrayList<>();

    public ClubsToFollowAdapter(List<Club> list, Context context) {
        this.mClubList = list;
        this.mContext = context;

    }


    public View getItemHolder(int pos) {
        return itemHolder.get(pos);
    }

    private void addItemHolder(View itemHolder) {
        this.itemHolder.add(itemHolder);
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
                load("https://firebasestorage.googleapis.com/v0/b/bangsoccer-1382.appspot.com/o/MediaCancha%2Fprimera%2FTest_MediaCancha%252Fprimera%252Flogo-Inter-SD-100.jpg?alt=media&token=e3b35af3-691a-4f0b-8e11-f1c3707be92e").
                placeholder(R.drawable.ic_open_game_icon).
                into(holder.vClubAvatar); //holder.vClubAvatar.setImageResource(mClubList.get(position).getTeam_image());
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
