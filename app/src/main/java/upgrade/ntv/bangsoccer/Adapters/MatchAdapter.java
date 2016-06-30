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
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ScheduleHolder>{

    WeeklySchedule mWeeklySchedule;
    Context mContext;
    LayoutInflater inflater ;
    int mWeeklyScheduleID;

    public MatchAdapter(int weeklySchedule, Context context) {

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
        .inflate(R.layout.row_match, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new ScheduleHolder(v);
        }

@Override
public void onBindViewHolder(ScheduleHolder holder, int position) {
        // - get element from your dataset at this vPlayerPosition
        // - replace the contents of the view with that element
final int pos = position;
    holder.day.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getTime());

    //     holder.team2.setText( mTeam.getmWeeklyMatch().get(vPlayerPosition).getTeamName(2));
    holder.teamLogo1.setImageResource(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamImage(1));
    holder.teamLogo1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //calls the Team1 Screen based on the teamID
            //TODO: replace with local DBSource
            Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
            intent.putExtra("CLUBID", mWeeklySchedule.getmWeeklyMatch().get(pos).getmTeam1().getmTeamID());

            if (intent != null) {

               mContext.startActivity(intent);
               // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }

    });
    holder.teamLogo2.setImageResource(mWeeklySchedule.getmWeeklyMatch().get(position).getTeamImage(2));
    holder.teamLogo2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //calls the Team2 Screen based on the teamID
            //TODO: replace with local DBSource
            Intent intent = DrawerSelector.onItemSelected((Activity) mContext, Constants.CLUBS_ACTIVITY_BY_TEAM);
            intent.putExtra("CLUBID", mWeeklySchedule.getmWeeklyMatch().get(pos).getmTeam2().getmTeamID());

            if (intent != null) {

                mContext.startActivity(intent);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }

    });
    holder.day.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getDate());

    holder.stadium.setText(mWeeklySchedule.getmWeeklyMatch().get(position).getStadium());

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


        time = (TextView) v.findViewById(R.id.time);
       // team2 = (TextView) v.findViewById(R.id.team_2_id);
        teamLogo1= (ImageView) v.findViewById(R.id.image_team1);
        teamLogo2= (ImageView) v.findViewById(R.id.image_team2);
        day = (TextView) v.findViewById(R.id.day);
        stadium = (TextView) v.findViewById(R.id.stadium);

    }
}
}
