package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.booking.HourObject;
import upgrade.ntv.bangsoccer.booking.HoursViewHolder;
import upgrade.ntv.bangsoccer.booking.MyDateUtils;
import upgrade.ntv.bangsoccer.booking.MyDialogFragment;
import upgrade.ntv.bangsoccer.booking.MyMatcher;
import upgrade.ntv.bangsoccer.booking.MyReservations;
import upgrade.ntv.bangsoccer.booking.availableDatesViewModel;


/**
 * Created by Jose Rivera on 9/19/2016.
 */
public class RecyclerHoursAdapter extends RecyclerView.Adapter<HoursViewHolder> {

    private final Context framecontext;

    private final availableDatesViewModel model;
    private LayoutInflater mInflater;
    List<HourObject> mhours;


    public RecyclerHoursAdapter(Context context, List<HourObject> hours, availableDatesViewModel model){

        this.mInflater= LayoutInflater.from(context);
       this.mhours=hours;
       this.framecontext=context;
       this.model=model;
   }

    @Override
    public HoursViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= mInflater.inflate(R.layout.available_hours_item,parent,false);

        HoursViewHolder holder = new HoursViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HoursViewHolder holder, int position) {

        HourObject hourobj = mhours.get(position);
        holder.setData(hourobj,position);

        holder.chboxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                final HoursViewHolder currentHolder=holder;
                String hora=currentHolder.HourView.getText().toString();
                int hour= MyMatcher.MatchHourToInt(hora);

                String fecha = model.monthnumber+"-"+model.day+"-"+ Calendar.getInstance().get(Calendar.YEAR)+"-"+hora;
                Calendar myCalendar = Calendar.getInstance();
                try{

                     myCalendar.setTime(MyDateUtils.MatchAmPmFormat(fecha));

                   System.out.println("Day of month:"+myCalendar.get(Calendar.DAY_OF_MONTH));

                }

                catch (Exception ex){
                    System.out.println("eta fecha no sirvio:"+fecha);
                }


                System.out.println("La fecha mapeada es:"+fecha);
                if(currentHolder.chboxView.isChecked()){

                    ShowConfirmationMsg("Esta hora sera agregada a tus Reservaciones, puedes removerla deseleccionando o en tu carro de reservaciones ");
                    MyReservations.Add(myCalendar);
                }
                else{

                    ShowConfirmationMsg("Esta hora sera removida de tus Reservaciones");
                    MyReservations.Remove(myCalendar);
                }


            }
        });
    }

    private void ShowConfirmationMsg(String msj) {

        DialogFragment dialog=  MyDialogFragment.NewInstance(msj);
        dialog.show(((FragmentActivity)framecontext).getSupportFragmentManager(),"My_tag");

    }

    @Override
    public int getItemCount() {
        return mhours.size();
    }
}
