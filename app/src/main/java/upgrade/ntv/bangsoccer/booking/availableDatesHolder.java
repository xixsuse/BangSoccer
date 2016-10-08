package upgrade.ntv.bangsoccer.booking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose Rivera on 9/4/2016.
 */
public class availableDatesHolder extends RecyclerView.ViewHolder {
    View mView;
    public TextView monthTextView;
    public TextView dayTextView;
    public TextView dayofweekTextView;



    public availableDatesHolder(View itemView) {
        super(itemView);
        mView=itemView;
    }

    public void setMonth(String month){

        monthTextView = (TextView) itemView.findViewById(R.id.month_text);
        monthTextView.setText(month);
    }
    public void setDay(String day){

        dayTextView= (TextView) itemView.findViewById(R.id.day_text);
        dayTextView.setText(day);
    }
    public void setDayofweek(String dayofweek){

        dayofweekTextView = (TextView) itemView.findViewById(R.id.dayofweek_text);
        dayofweekTextView.setText(dayofweek);
    }


}
