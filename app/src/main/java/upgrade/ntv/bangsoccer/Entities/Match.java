package upgrade.ntv.bangsoccer.Entities;


/**
 * Created by Jose on 3/15/2015.
 */
public class Match {
    private Club clubida;
    private Club clubidb;
    private String matchid;
    private String tournamentid;
    private String mFireBaseKey;
    private String stadium;
    private String date, time;

    public Match(String matchId) {
        this.matchid = matchId;

       // onCreateSchedule();
    }

    public Match() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Club getClubIdA() {
        return clubida;
    }

    public void setClubIdA(Club mClubIdA) {
        this.clubida = mClubIdA;
    }

    public Club getClubIdB() {
        return clubidb;
    }

    public void setClubIdB(Club mClubIdB) {
        this.clubidb = mClubIdB;
    }

    public String getMatchId() {
        return matchid;
    }

    public void setMatchId(String mMatchId) {
        this.matchid = mMatchId;
    }

    public String getTournamentId() {
        return tournamentid;
    }

    public void setTournamentId(String mTournamentId) {
        this.tournamentid = mTournamentId;
    }

    public String getmFireBaseKey() {
        return mFireBaseKey;
    }

    public String getStadium() {
        return stadium;
    }
}
