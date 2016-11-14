/*
package upgrade.ntv.bangsoccer.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;

import upgrade.ntv.bangsoccer.Adapters.RecyclerHoursAdapter;
import upgrade.ntv.bangsoccer.R;


*/
/**
 * Created by Jose Rivera on 9/3/2016.
 *//*

public class AvailableDatesFragment extends Fragment {

    private FirebaseRecyclerAdapter<availableDatesViewModel, availableDatesHolder> mAdapter;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mmanager;
    private LinearLayoutManager mmanager2;
    private FirebaseRecyclerAdapter<availableDatesViewModel, availableDatesHolder> mAdapter2;
    private ArrayList<HourObject> hoursList;
    private RecyclerHoursAdapter madapter3;
    private RecyclerView rv_hours;
    private String Week;

    ;

    public AvailableDatesFragment() {
        // Required empty public constructor
    }


    public static AvailableDatesFragment newInstance(String week){

       AvailableDatesFragment fragment = new AvailableDatesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("week_key",week);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Week="Week-"+getArguments().getString("week_key");
        final View rootView = inflater.inflate(R.layout.koala_fragment_main, container, false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        mref= ref.child("Available-Dates").child(Week);
        Query query = mref.orderByChild("available").equalTo(true);
        mmanager=new LinearLayoutManager(getActivity());
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);

        rv.setHasFixedSize(false);
        rv.setLayoutManager(mmanager);

        rv_hours =(RecyclerView) rootView.findViewById(R.id.rv_hours);
        rv_hours.setHasFixedSize(false);





        mAdapter = new FirebaseRecyclerAdapter<availableDatesViewModel, availableDatesHolder>(availableDatesViewModel.class,R.layout.row_calendar_layout, availableDatesHolder.class, query) {


                @Override
            protected void populateViewHolder(availableDatesHolder viewHolder, availableDatesViewModel model, int position) {

                final int pos = position;
                final availableDatesViewModel finalModel=model;


                    viewHolder.setMonth(model.month);
                    viewHolder.setDay(model.day);
                    viewHolder.setDayofweek(model.dayofweek);
                    int selectedPos=0;
                    viewHolder.itemView.setSelected(selectedPos == position);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View view){
                    Log.w("Available-fragment","you clicked on position:"+pos);
                    SetHoursAdapter(finalModel);
                }
                });

            }



        };




        rv.setAdapter(mAdapter);


        return rootView;

    }

    private ArrayList<HourObject> GetHours(AvailableHours availablehours) {

        ArrayList<HourObject> hours = new ArrayList<>();

        hours.add(availablehours.H1);
        hours.add(availablehours.H2);
        hours.add(availablehours.H3);
        hours.add(availablehours.H4);
        hours.add(availablehours.H5);
        hours.add(availablehours.H6);
        hours.add(availablehours.H7);
        hours.add(availablehours.H8);

        return hours;
    }

    private void SetHoursAdapter(availableDatesViewModel model) {

       hoursList = new ArrayList<HourObject>();
        ArrayList toremove = new ArrayList();

        hoursList.add(model.availablehours.H1);
        hoursList.add(model.availablehours.H2);
        hoursList.add(model.availablehours.H3);
        hoursList.add(model.availablehours.H4);
        hoursList.add(model.availablehours.H5);
        hoursList.add(model.availablehours.H6);
        hoursList.add(model.availablehours.H7);
        hoursList.add(model.availablehours.H8);

        for (HourObject hour:hoursList) {
            if(hour.available.equals("false")){
               toremove.add(hour);
            }


        }

        hoursList.removeAll(toremove);
        toremove = new ArrayList();

        Calendar tempDate=  Calendar.getInstance();

        for (HourObject hour:hoursList) {

          //  int currentHour = MyMatcher.MatchHourToInt(hour.Hour);
          //  tempDate.set(Calendar.YEAR, Calendar.getInstance().YEAR);
          //  tempDate.set(Calendar.MONTH, model.monthnumber-1);
          //  tempDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(model.day));
         //   tempDate.set(Calendar.HOUR_OF_DAY,currentHour);
          //  tempDate.set(Calendar.MINUTE, 0);
          //  tempDate.set(Calendar.SECOND, 0);
           // tempDate.set(Calendar.MILLISECOND, 0);
            String fecha = model.monthnumber+"-"+model.day+"-"+ Calendar.getInstance().get(Calendar.YEAR)+"-"+hour.Hour;
            tempDate.setTime(MyDateUtils.MatchAmPmFormat(fecha));
            String DatetoCompare=MyDateUtils.Match24HourFormat(tempDate);
            System.out.println("Tamo peddio la hora es:"+tempDate.getTime());
            if( MyReservations.IsReserved(DatetoCompare)){

                toremove.add(hour);
            }

        }

        hoursList.removeAll(toremove);



        mmanager2 = new LinearLayoutManager(getActivity());
        rv_hours.setLayoutManager(mmanager2);
        madapter3 = new RecyclerHoursAdapter(getActivity(),hoursList,model);
        rv_hours.setAdapter(madapter3);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

}
*/
