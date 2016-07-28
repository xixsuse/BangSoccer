package upgrade.ntv.bangsoccer.Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jose on 3/15/2015.
 */
public class Club implements Cloneable {

    private  int  pj, dg, points, rank;
    private String name, stadium, profile, division, team_image;

    private boolean isFavorite;
    private HashMap<String, Boolean> players_ids = new HashMap<>();
    private List<Players> player_list = new ArrayList<Players>();
    private List<String> match_list = new ArrayList<String>();

    private Club club_clone;

    String mFireBaseKey;


    public Club() {
        //jackson object
    }

    //   Setters

public Club getClub_clone() throws CloneNotSupportedException {
   club_clone = (Club) this.clone();
    return club_clone;
}

    public HashMap<String, Boolean> getPlayers_ids() {
        return players_ids;
    }

    public void setPlayers_ids(HashMap<String, Boolean> players_ids) {
        this.players_ids = players_ids;
    }

    public String getmFireBaseKey() {
        return mFireBaseKey;
    }

    public void setmFireBaseKey(String mFireBaseKey) {
        this.mFireBaseKey = mFireBaseKey;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private void setTeam_image(String team_image) {
        this.team_image = team_image;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setStadium(String stadium) {
        this.stadium = stadium;
    }

    private void setProfile(String profile) {
        this.profile = profile;
    }

    private void setPj(int pj) {
        this.pj = pj;
    }

    private void setDg(int dg) {
        this.dg = dg;
    }

    private void setPoints(int points) {
        this.points = points;
    }

    private void setRank(int rank) {
        this.rank = rank;
    }

    private void addPlayersList(List<Players> mPlayersList) {
        this.player_list = mPlayersList;
    }

    // Getters


    public String getTeam_image() {
        return team_image;
    }

    public String getName() {
        return name;
    }

    public String getStadium() {
        return stadium;
    }



    public int getPj() {
        return pj;
    }

    public int getDg() {
        return dg;
    }

    public int getPoints() {
        return points;
    }

    public int getRank() {
        return rank;
    }

    public List<Players> getPlayer_list() {
        return player_list;
    }

    public List<String> getMatch_list() {
        return match_list;
    }

    public void addMatchList(List<String> mMatchList) {
        this.match_list = mMatchList;
    }


    // object functions

    private void addPlayer(Players player){
        player_list.add(player);
    }

    private void addMatch(String match){
        match_list.add(match);
    }


    public Players getPlayer(int index){
        return player_list.get(index);
    }

    public String getMatch(int index){
       return match_list.get(index);
    }


    public String onCreateMatchListString(){
        String listToShow = "";
        for (int j = 0; j < getMatch_list().size() ; j++) {
            if (getMatch(j) != null) {
                listToShow = listToShow + getMatch(j) + "\n" ;
            }

        }

        return listToShow;
    }



    @Override
    public String toString() {
        return "Club{" +
                "name='" + name + '\'' +
                ", stadium='" + stadium + '\'' +
                ", profile='" + profile + '\'' +
                ", pj=" + pj +
                ", dg=" + dg +
                ", points=" + points +
                ", rank=" + rank +
                ", player_list=" + player_list +
                '}';
    }
}
