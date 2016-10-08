package upgrade.ntv.bangsoccer.booking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose Rivera on 10/6/2016.
 */
public class MyreservationsViewHolder extends RecyclerView.ViewHolder {


    private final TextView HourView;
    private final TextView CanchaView;

    public MyreservationsViewHolder(View itemView) {
        super(itemView);


        HourView  = (TextView) itemView.findViewById(R.id.hour_txtview);
        CanchaView  = (TextView) itemView.findViewById(R.id.cancha_txtview);
    }


    public void SetData(ReservationModel reservationModel,int position){

        HourView.setText(reservationModel.ReservationDate);
        CanchaView.setText(reservationModel.Cancha);

    }



}
