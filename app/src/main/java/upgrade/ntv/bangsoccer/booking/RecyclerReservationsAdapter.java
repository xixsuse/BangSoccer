package upgrade.ntv.bangsoccer.booking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose Rivera on 10/6/2016.
 */
public class RecyclerReservationsAdapter extends RecyclerView.Adapter<MyreservationsViewHolder> {
    private final LayoutInflater mInflater;
    private final Context mycontext;

    public RecyclerReservationsAdapter(Context context) {

        this.mInflater= LayoutInflater.from(context);
        this.mycontext=context;


    }

    @Override
    public MyreservationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= mInflater.inflate(R.layout.row_reservation_car,parent,false);

       MyreservationsViewHolder holder = new MyreservationsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyreservationsViewHolder holder, int position) {

        holder.SetData(MyReservations.GetReservation(position),position);
    }

    @Override
    public int getItemCount() {
        return MyReservations.Count();
    }
}
