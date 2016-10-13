package upgrade.ntv.bangsoccer.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose Rivera on 10/5/2016.
 */
public class MyReservationsFragment extends Fragment {


    private LinearLayoutManager mmanager;
    private RecyclerReservationsAdapter madapter;

    public MyReservationsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.content_reservation_cart, container, false);

        mmanager=new LinearLayoutManager(getActivity());
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler_reservations_card);

        rv.setHasFixedSize(false);
        rv.setLayoutManager(mmanager);

        madapter = new RecyclerReservationsAdapter(getActivity());

        rv.setAdapter(madapter);
       System.out.println("this is not working");
        System.out.println("this is not working");
        System.out.println("this is not working");
        System.out.println("this is not working");
        return rootView;
    }

}
