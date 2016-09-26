package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.TournamentObjects.History;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;

/**
 * Created by jfrom on 3/19/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryMatchHolder>
        implements View.OnClickListener{

    private List<Match> mMatchesHistory = new ArrayList<>();

    private Context
            mContext;

    private String
            mClubID,
            mDivision;

    public HistoryAdapter(Context context, String clubId, String division) {

        this.mClubID = clubId;
        this.mDivision = division;
        this.mContext = context;

        Query query = ActivityMain.databaseReference.child(division + "_History").child(clubId);
        query.addValueEventListener(new HistoryEventListener());

    }

    //firebase event listener
    private class HistoryEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // whenever data at this location is updated.
            for (DataSnapshot mdSnapshot : dataSnapshot.getChildren()) {

                History history = mdSnapshot.getValue(History.class);
                history.setHISTORY_ID(dataSnapshot.getKey());

                Query query = ActivityMain.databaseReference
                        .child(mDivision + "_Calendar")
                        .child(history.getDate())
                        .child(history.getVS());
                query.addValueEventListener(new MatchEventListener());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    //firebase event listener
    private class MatchEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Match match = dataSnapshot.getValue(Match.class);
            match.setMatchId(dataSnapshot.getKey());
            mMatchesHistory.add(match);
            notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @Override
    public int getItemCount() {
        return (this.mMatchesHistory.size());
    }

    @Override
    public HistoryMatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_match_v2, parent, false);

        return new HistoryMatchHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryMatchHolder holder, int position) {

        //get player
        Match match = mMatchesHistory.get(position);

        Picasso.with(mContext)
                .load(match.getClubIdA().getTeam_image())
                .placeholder(R.drawable.ic_upgraden)
                .into(holder.teamLogo1);

        Picasso.with(mContext)
                .load(match.getClubIdB().getTeam_image())
                .placeholder(R.drawable.ic_upgraden)
                .into(holder.teamLogo2);

        holder.score.setText(
                match.getClubIdA().getGoles()
                        + " - " +
                        match.getClubIdB().getGoles());

        holder.date.setText(match.getDate());
        holder.stadium.setText(match.getStadium());

        holder.teamLogo1.setOnClickListener(this);
        holder.teamLogo2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    public static class HistoryMatchHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.club_history_image_team1)
        ImageView teamLogo1;
        @BindView(R.id.club_history_image_team2)
        ImageView teamLogo2;
        @BindView(R.id.club_history_final_score_txt)
        TextView score;
        @BindView(R.id.club_history_day_txt)
        TextView date;
        @BindView(R.id.club_history_stadium_txt)
        TextView stadium;

        public HistoryMatchHolder(View v) {
            super(v);
            //butter knife bind
            ButterKnife.bind(this, v);
        }
    }
}
