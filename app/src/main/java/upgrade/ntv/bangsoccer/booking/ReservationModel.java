package upgrade.ntv.bangsoccer.booking;

import java.util.Calendar;

/**
 * Created by Jose Rivera on 9/27/2016.
 */
public class ReservationModel {
    public String ReservedBy;
    public String Cancha;
    public String ReservationDate;
    public boolean approved;



    public ReservationModel(){
        this.ReservedBy="Dummy User";
        this.Cancha="Cancha 1";
        this.ReservationDate = "";
        this.approved=false;
    }
    public void setDate(int month, String day, int year, int hour) {


        Calendar tempDate = Calendar.getInstance();
        tempDate.set(Calendar.YEAR, year);
        tempDate.set(Calendar.MONTH, month-1);
        tempDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        tempDate.set(Calendar.HOUR_OF_DAY, hour);
        tempDate.set(Calendar.MINUTE, 0);
        tempDate.set(Calendar.SECOND, 0);
        tempDate.set(Calendar.MILLISECOND, 0);
        ReservationDate = tempDate.getTime().toString();
    }
}
