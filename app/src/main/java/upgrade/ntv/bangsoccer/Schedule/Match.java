package upgrade.ntv.bangsoccer.Schedule;

/**
 * Created by Jose on 3/15/2015.
 */
public class Match {

    private final int mMatchID;
    private String mStadium, mTime, mDate;
    private boolean mTv;
    private Team mTeam1, mTeam2;

    public Match(int mID, Team mTeam1, Team mTeam2) {
        this.mMatchID = mID ;
        this.mTeam1 = mTeam1;
        this.mTeam2 = mTeam2;
        this.mTv = false;
        onCreateMatch();
    }

     /*
     *   setters & getters
     */

    public Team getmTeam1() {
        return mTeam1;
    }

    public Team getmTeam2() {
        return mTeam2;
    }

    private void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getStadium(){
        return this.mStadium;
    }

    public String getTime() {
        return  this.mTime;
    }

    public String getDate(){
        return this.mDate;
    }

    /*
     *Public Object functions
    */

    public  void onChangeTv(){
        this.mTv = !this.mTv;
    }

    public boolean isTv() {
        return mTv;
    }

    public int getTeamImage(int index){
        int image = 0;
        switch (index){
            case 1:
            image = mTeam1.getmTeamImage();
                break;

            case 2:
                image = mTeam2.getmTeamImage();
                break;
        }

        return image;
    }

    public String getTeamName(int index){
        String name = null;
        switch (index){
            case 1:
                name = mTeam1.getmName();
                break;

            case 2:
                name = mTeam2.getmName();
                break;
        }

        return name;
    }

    /*
     *Private Object functions
    */

    private void onCreateMatch(){

        onCreateTime();
        onCreateStadium();
 //       onCreateDate();
    }


    /** HARD CODED: AUTOMATE **/
    private void onCreateTime(){
        setmTime("4:00pm");
    }

    private void onCreateStadium(){
       this.mStadium = mTeam1.getmStadium();
    }

 /*   private void onCreateDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        String formattedDate = df.format(c.getTime());
        this.mDate = formattedDate;
    }
*/

    //to String
    @Override
    public String toString() {
        return "Match{" +
                "mTeam1='" + mTeam1 + '\'' +
                ", mTeam2='" + mTeam2 + '\'' +
                ", mStadium='" + mStadium + '\'' +

                ", mTime=" + mTime +
                ", mTv=" + mTv +
                '}';
    }
}
