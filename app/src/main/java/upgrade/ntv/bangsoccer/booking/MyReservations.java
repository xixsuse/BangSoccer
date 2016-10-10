package upgrade.ntv.bangsoccer.booking;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jose Rivera on 10/2/2016.
 */
public class MyReservations {
    public static ArrayList<ReservationModel> reservations;
    private static ReservationModel currentReservation;

    public static void Add(availableDatesViewModel model, int hour) {

        if(reservations==null){
            reservations = new ArrayList<ReservationModel>();
        }

        SetCurrentReservation(model,hour);
        reservations.add(currentReservation);


    }

    public static ReservationModel GetReservation(int position){

        return reservations.get(position);
    }



    private static void SetCurrentReservation(availableDatesViewModel model,int hour) {
       currentReservation = new ReservationModel();
       currentReservation.setDate(model.monthnumber,model.day, Calendar.getInstance().YEAR,hour);
    }



    public static void Remove(availableDatesViewModel model, int hour) {

        SetCurrentReservation(model,hour);
        ReservationModel toremove = new ReservationModel();
        for (ReservationModel res:reservations)
        {

            if(res.ReservationDate.equals(currentReservation.ReservationDate)){

               toremove=res;
               // reservations.remove(res);
                System.out.println("Removing date from my reservations....");
            }

        }


        reservations.remove(toremove);
        for (ReservationModel res:reservations)
        {

           System.out.println(res.ReservedBy);
            System.out.println(res.Cancha);
            System.out.println(res.ReservationDate);

        }
    }

    public static boolean IsReserved(Calendar date) {

        if(reservations!=null)
        for (ReservationModel model:reservations)
        {

            if(model.ReservationDate.equals(date.getTime().toString())){
                return true;
            }

        }

        return false;
    }




    public static boolean IsEmpty() {

        if(reservations==null){
             return true;
        }

        return false;
    }

    public static int Count() {


        return reservations.size();
    }
}
