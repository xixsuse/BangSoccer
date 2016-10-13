package upgrade.ntv.bangsoccer.booking;

/**
 * Created by Jose Rivera on 9/3/2016.
 */
public class availableDatesViewModel {
    public int monthnumber;
    public String month;
    public String day;
    public String dayofweek;
    public AvailableHours availablehours;
    public boolean available;

    availableDatesViewModel(){

    }

    availableDatesViewModel(String month,String day,String dayofweek,AvailableHours availablehours,boolean available,int monthnumber){

        this.month=month;
        this.day=day;
        this.dayofweek=dayofweek;
        this.availablehours=availablehours;
        this.available=available;
        this.monthnumber=monthnumber;
    }


    public String getMonth(){

        return month;
    }

    public String getDay(){
        return day;
    }

    public String getDayofweek(){
        return dayofweek;
    }

    public AvailableHours getavailablehours(){
        return availablehours;
    }

    public boolean getAvailable(){return available;}

    public int getMonthnumber(){return monthnumber;}

}
