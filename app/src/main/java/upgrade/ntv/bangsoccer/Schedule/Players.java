package upgrade.ntv.bangsoccer.Schedule;

/**
 * Created by Jose on 3/15/2015.
 */
public class Players {

    String mPlayerName, mPosition, mPlayerNum, mPlayerAlias, mDivition;
    int  mTeamID, mPlayer_Avatar;


    public Players(String mPlayerName, String mPlayerNum, int mTeamID, String position, String Alias, String Division, int Avatar) {
        this.mPlayerName = mPlayerName;
        this.mPlayerNum = mPlayerNum;
        this.mTeamID = mTeamID;
        this.mPosition= position;
        this.mPlayerAlias = Alias;
        this.mDivition = Division;
        this.mPlayer_Avatar=Avatar;
    }

    public String getmPlayerAlias() {
        return this.mPlayerAlias;
    }

    public String getmDivition() {
        return this.mDivition;
    }

    public int getmTeamID() {
        return this.mTeamID;
    }

    public String getmPlayerName() {
        return this.mPlayerName;
    }

    public String getmPlayerNum() {
        return this.mPlayerNum;
    }

    public String getmPosition() {
        return this.mPosition;
    }

    public int getmPlayer_Avatar() {
        return this.mPlayer_Avatar;
    }

    public void setmPlayer_Avatar(int mPlayer_Avatar) {
        this.mPlayer_Avatar = mPlayer_Avatar;
    }

    @Override
    public String toString() {
        return "Players{" +
                "mPlayerName='" + mPlayerName + '\'' +
                ", mPlayerNum=" + mPlayerNum +
                '}';
    }

}
