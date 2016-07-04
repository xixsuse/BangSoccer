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

import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Schedule.WeeklySchedule;

/**
 * Created by jfrom on 3/19/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ScheduleHolder> {

    WeeklySchedule mWeeklySchedule;
    Context mContext;
    LayoutInflater inflater;
    int mWeeklyScheduleID;


    public HistoryAdapter(int weeklySchedule, Context context) {

        this.mWeeklyScheduleID = weeklySchedule;
        this.mWeeklySchedule = new WeeklySchedule(weeklySchedule);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemCount() {
        return (this.mWeeklySchedule.getmWeeklyMatch().size());
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_match_v2, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new ScheduleHolder(v);
    }

    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        // - get element from your dataset at this vPlayerPosition
        // - replace the contents of the view with that element
        String result = String.valueOf(5 + (int) (Math.random() * ((5) + 1)));
        final  int pos = position;/*
        holder.teamName1.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamName(1));
        holder.teamName2.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamName(2));*/
        holder.teamLogo1.setImageResource(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamImage(1));
        holder.teamLogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls the Team1 Screen based on the teamID
                //TODO: replace with local DBSource
                Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
                intent.putExtra("CLUBID", mWeeklySchedule.getmWeeklyMatch().get(pos).getmTeam1().getTeamid());

                if (intent != null) {

                    mContext.startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

        });        holder.teamLogo2.setImageResource(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamImage(2));
        holder.teamLogo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls the Team2 Screen based on the teamID
                //TODO: replace with local DBSource
                Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
                intent.putExtra("CLUBID", mWeeklySchedule.getmWeeklyMatch().get(pos).getmTeam2().getTeamid());

                if (intent != null) {

                    mContext.startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

        });
        holder.scoreTeam1.setText( result + " - " + result );
       // holder.scoreTeam2.setText(String.valueOf(5 + (int) (Math.random() * ((5) + 1))));
       // holder.day.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getDate());

        holder.stadium.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getStadium());


    }

// Provides a reference to the views for each data item


    public static class ScheduleHolder extends RecyclerView.ViewHolder {
        /*TextView teamName1;
        TextView teamName2;*/
        ImageView teamLogo1;
        ImageView teamLogo2;
        TextView scoreTeam1;
       // TextView scoreTeam2;
        TextView time;
        TextView day;
        TextView stadium;

        public ScheduleHolder(View v) {
            super(v);


           /* teamName1 = (TextView) v.findViewById(R.id.history_team1_name);
            teamName2 = (TextView) v.findViewById(R.id.history_team2_name);*/
            teamLogo1 = (ImageView) v.findViewById(R.id.club_history_image_team1 );
            teamLogo2 = (ImageView) v.findViewById(R.id.club_history_image_team2);
            scoreTeam1 = (TextView) v.findViewById(R.id.club_history_final_score_txt);
            day = (TextView) v.findViewById(R.id.club_history_day_txt);
            stadium = (TextView) v.findViewById(R.id.club_history_stadium_txt);

        }
    }
}