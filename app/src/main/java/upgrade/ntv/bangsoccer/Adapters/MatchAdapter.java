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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.AppConstants.FirebaseUtils;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.Club;
import upgrade.ntv.bangsoccer.TournamentObjects.Day;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;

/**
 * Created by jfrom on 3/19/2016.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ScheduleHolder> {

    private List<Match> mClubsMatches;
    private Context mContext;

    public MatchAdapter(Context context, Day games) {

        this.mClubsMatches = new ArrayList<>();
        this.mContext = context;

        //get the matches from the games of the day list
        Map<String, Match> matchMap = games.getGames();
        for (Map.Entry<String, Match> entry :
                matchMap.entrySet()) {

            String key1 = entry.getKey();
            Match value1 = entry.getValue();
            value1.setMatchId(key1);
            //add the match to the list
            mClubsMatches.add(value1);
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

        return new ScheduleHolder(v);
    }

    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        // - get element from your dataset at this vPlayerPosition
        // - replace the contents of the view with that element

        final Match match = mClubsMatches.get(position);


        holder.teamName1.setText(match.getClubIdA().getName());

        //     holder.team2.setText( mTeam.getmWeeklyMatch().get(vPlayerPosition).getTeamName(2));
        Picasso.with(mContext).
                load(match.getClubIdA().getTeam_image()).
                placeholder(R.drawable.ic_upgraden).
                into(holder.teamLogo1);
        //holder.teamLogo1.setImageResource(mClubsMatches.getmWeeklyMatch().get(position).getTeamImage(1));
        holder.teamLogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls the Team1 Screen based on the teamID
                Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
                String clubid = match.getClubIdA().getFirebasekey();
                intent.putExtra("CLUBID", clubid);

                if (intent != null) {

                    mContext.startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

        });
        Picasso.with(mContext).
                load(match.getClubIdB().getTeam_image()).
                placeholder(R.drawable.ic_upgraden).
                into(holder.teamLogo2);

        holder.teamLogo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls the Team2 Screen based on the teamID
                Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
                String clubid = match.getClubIdB().getFirebasekey();
                intent.putExtra("CLUBID", clubid);

                if (intent != null) {

                    mContext.startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

        });
        holder.teamName2.setText(match.getClubIdB().getName());
        holder.stadium.setText(match.getTime() + " @ " + match.getStadium());
        holder.vs.setText(match.getClubIdA().getGoles() + " : " + match.getClubIdB().getGoles());

    }

    // Provides a reference to the views for each data item
    public static class ScheduleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.match_club_b_name)
        TextView teamName2;
        @BindView(R.id.vs)
        TextView vs;
        @BindView(R.id.image_team1)
        ImageView teamLogo1;
        @BindView(R.id.image_team2)
        ImageView teamLogo2;
        @BindView(R.id.match_club_a_name)
        TextView teamName1;
        @BindView(R.id.place_n_time)
        TextView stadium;

        public ScheduleHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
