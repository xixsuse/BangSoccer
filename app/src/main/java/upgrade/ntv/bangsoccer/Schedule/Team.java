package upgrade.ntv.bangsoccer.Schedule;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;

/**
 * Created by Jose on 3/15/2015.
 */
public class Team implements Cloneable {

    private  int teamid, team_image, pj, dg, points, rank, clubid;
    private String name, stadium, profile, division;

    private boolean isFavorite;
    private List<Players> player_list = new ArrayList<Players>();
    private List<String> match_list = new ArrayList<String>();

    private Team team_clone;


    public Team() {
        //jackson object
    }

    // public constructor
    public Team( int teamid) {
        this.teamid = teamid;
        onSaveTeam();
        onCreateTeamMatch();
    }

    //   Setters

public Team getTeam_clone() throws CloneNotSupportedException {
   team_clone = (Team) this.clone();
    return team_clone;
}

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private void setTeam_image(int team_image) {
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


    public int getTeam_image() {
        return team_image;
    }

    public int getTeamid() {
        return teamid;
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


    private void onSaveTeam(){
        for (int i = 0; i < AppConstant.mTeamArrayList.length; i++)
        {
            if( Integer.valueOf(AppConstant.mTeamArrayList[i][0]) == this.teamid){
                setName(AppConstant.mTeamArrayList[i][1]);
                setStadium(AppConstant.mTeamArrayList[i][2]);
                setTeam_image(AppConstant.mImageList[i]);
            }
        }
    }

    private void onCreateTeamMatch(){
        for (int i = 0; i < AppConstant.mMatchArrayList.length-9 ; i++) {
            for (int j = 0; j < AppConstant.mMatchArrayList[i].length  ; j++) {
                String matchList = null;
                if(AppConstant.mMatchArrayList[i][j][1] == teamid) {
                    matchList = name + " VS " + AppConstant.mTeamArrayList[AppConstant.mMatchArrayList[i][j][2]][1] +" "+ AppConstant.mMatchTimeDateArray[i][j][0];
                }else if( AppConstant.mMatchArrayList[i][j][2] == teamid){
                    matchList = name + " VS " + AppConstant.mTeamArrayList[AppConstant.mMatchArrayList[i][j][1]][1]+" "+ AppConstant.mMatchTimeDateArray[i][j][0];
                }

                    addMatch(matchList);
                }
            }
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
        return "Team{" +
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
