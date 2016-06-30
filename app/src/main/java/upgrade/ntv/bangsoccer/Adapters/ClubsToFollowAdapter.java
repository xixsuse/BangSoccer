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

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Schedule.Team;


/**
 * Created by jfrom on 3/22/2016.
 */
public class ClubsToFollowAdapter extends RecyclerView.Adapter<ClubsToFollowAdapter.TeamHolder> {

    private List<Team> mTeamList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private List<View> itemHolder = new ArrayList<>(13);

    public ClubsToFollowAdapter(List<Team> list, Context context) {
        this.mTeamList = list;
        this.mContext = context;

    }


    public View getItemHolder(int pos) {
        return itemHolder.get(pos);
    }

    private void addItemHolder(View itemHolder) {
        this.itemHolder.add(itemHolder);
    }

    public int getClubID(int position) {
        return mTeamList.get(position).getmTeamID();
    }

    @Override
    public int getItemCount() {
        return (this.mTeamList.size());
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
        holder.vClubName.setText(mTeamList.get(position).getmName());
        holder.vClubAvatar.setImageResource(mTeamList.get(position).getmTeamImage());
        holder.Id = mTeamList.get(position).getmTeamID();
        holder.cCheckBox.setChecked(mTeamList.get(position).isFavorite());
        holder.vCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                holder.cCheckBox.setChecked(!holder.cCheckBox.isChecked());
                if(holder.cCheckBox.isChecked() == true){
                    mTeamList.get(position).setFavorite(true);
                }else{
                    mTeamList.get(position).setFavorite(false);
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
        private int Id;

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
