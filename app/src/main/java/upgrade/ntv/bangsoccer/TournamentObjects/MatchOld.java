package upgrade.ntv.bangsoccer.TournamentObjects;

/**
 * Created by Jose on 3/15/2015.
 */
public class MatchOld {

    private final int mMatchID;
    private String mStadium, mTime, mDate;
    private boolean mTv;
    private Club mClub1, mClub2;

    public MatchOld(int mID, Club mClub1, Club mClub2) {
        this.mMatchID = mID ;
        this.mClub1 = mClub1;
        this.mClub2 = mClub2;
        this.mTv = false;
        onCreateMatch();
    }

     /*
     *   setters & getters
     */

    public Club getmClub1() {
        return mClub1;
    }

    public Club getmClub2() {
        return mClub2;
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

    public String getTeamImage(int index){
        String image ="";
        switch (index){
            case 1:
            image = mClub1.getTeam_image();
                break;

            case 2:
                image = mClub2.getTeam_image();
                break;
        }

        return image;
    }

    public String getTeamName(int index){
        String name = null;
        switch (index){
            case 1:
                name = mClub1.getName();
                break;

            case 2:
                name = mClub2.getName();
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
       this.mStadium = mClub1.getStadium();
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
        return "MatchOld{" +
                "mClub1='" + mClub1 + '\'' +
                ", mClub2='" + mClub2 + '\'' +
                ", mStadium='" + mStadium + '\'' +

                ", mTime=" + mTime +
                ", mTv=" + mTv +
                '}';
    }
}
