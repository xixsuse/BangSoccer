package upgrade.ntv.bangsoccer.booking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jose Rivera on 9/11/2016.
 */
public class MyDateUtils {


    private static Calendar calendar;
    private static String endDaString;
    private static String startDateInStr;

    public static String GetWeekInterval(int position) {

      return getStartEndOFWeek(GetWeek(position),GetCurrentYear());
    }

    private static int GetCurrentYear() {
      return  Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int GetWeek(int weeknumber) {

     return   Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)+weeknumber;
    }




   public static String getStartEndOFWeek(int enterWeek, int enterYear){
//enterWeek is week number
//enterYear is year

         InitDates(enterWeek,enterYear,"ddMMM");

        return(startDateInStr+"-"+endDaString);
    }

    private static void InitDates(int enterWeek, int enterYear, String format) {


        calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat(format); // PST`
        Date startDate = calendar.getTime();
         startDateInStr = formatter.format(startDate);


        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
         endDaString = formatter.format(enddate);
    }



    public static String GetWeekKey(int weeknumber) {

        //what day of week of current week is?
        //if (currentDay+1)<=endofWeekDay then show currentDay+1 trough endofWeekday
        //or if(currentDate+1day) - (endweekdate) >=24hrs then show currentDate+1 trough EndWeekDate else print no dates available for this week

        InitDates(GetWeek(weeknumber),GetCurrentYear(),"dd");
        return("Week-"+startDateInStr+"-"+endDaString+"-"+GetCurrentYear()+"-available");

    }
}
