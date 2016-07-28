package upgrade.ntv.bangsoccer.Schedule;



import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;

/**
 * Created by Jose on 3/15/2015.
 */
public class WeeklySchedule {

    private List<Match> mWeeklyMatch;
    private int mWeeklyScheduleID;

    public WeeklySchedule(int weeklyScheduleID) {
        this.mWeeklyScheduleID = weeklyScheduleID;
        onCreateMatchList();
        onCreateSchedule();
    }

    public int getMweeklyScheduleID() {
        return mWeeklyScheduleID;
    }

    public List<Match> getmWeeklyMatch() {
        return mWeeklyMatch;
    }

    public void setmWeeklyMatch(List<Match> mWeeklyMatch) {
        this.mWeeklyMatch = mWeeklyMatch;
    }

    // obejct functions

    public Match getMatch( int index) {
        return mWeeklyMatch.get(index);
    }

    public void setMatch(Match match) {
        this.mWeeklyMatch.add(match);
    }



    public void onCreateMatchList(){
        List<Match> xMatch = new ArrayList<Match>();
        setmWeeklyMatch(xMatch);
    }

    private void onCreateSchedule(){
        for (int i = 0; i < AppConstant.mMatchArrayList.length ; i++) {
            if( AppConstant.mMatchArrayList[i][0][0] == mWeeklyScheduleID -1){
                 for (int j = 0; j < AppConstant.mMatchArrayList[i].length  ; j++) {
                     Club club1 = new Club();
                     Club club2 = new Club();
                     Match myMatch = new Match(mWeeklyScheduleID, club1, club2);
                     myMatch.setDate("6:00pm");
                     setMatch(myMatch);
                    }
            }
        }
    }

    private void onCreateDate(){

    }

    @Override
    public String toString() {
        return "WeeklySchedule{" +
                "mWeeklyMatch=" + mWeeklyMatch +
                '}';
    }
}
