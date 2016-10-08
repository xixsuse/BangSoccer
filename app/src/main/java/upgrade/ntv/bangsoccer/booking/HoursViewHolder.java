package upgrade.ntv.bangsoccer.booking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose Rivera on 9/19/2016.
 */
public class HoursViewHolder extends RecyclerView.ViewHolder{
    public CheckBox chboxView;
    public TextView HourView;
    private int position;
    private HourObject currentHour;

    public HoursViewHolder(View itemView) {
        super(itemView);

        HourView  = (TextView) itemView.findViewById(R.id.txtview_hours);
        chboxView = (CheckBox) itemView.findViewById(R.id.chkbox_view);
    }

    public void setData(HourObject hourobj, int position) {

        HourView.setText(hourobj.Hour);
        this.position = position;
        this.currentHour=hourobj;

    }
}
