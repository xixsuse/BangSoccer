package upgrade.ntv.bangsoccer.TournamentObjects;

/**
 * Created by Paulino on 9/25/2016.
 */

public class History {

    private String
            HISTORY_ID;

    private String
            date,
            VS;

    public History() {
    }

    public History(String date, String matchId) {
        this.date = date;
        this.VS = matchId;
    }

    public String getHISTORY_ID() {
        return HISTORY_ID;
    }

    public void setHISTORY_ID(String HISTORY_ID) {
        this.HISTORY_ID = HISTORY_ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVS() {
        return VS;
    }

    public void setVS(String VS) {
        this.VS = VS;
    }
}
