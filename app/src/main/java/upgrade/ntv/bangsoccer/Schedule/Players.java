package upgrade.ntv.bangsoccer.Schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose on 3/15/2015.
 */
public class Players {


    private String name, division, alias,  position, goals, red_card, yellow_card, asisst, nacionalidad, altura, peso, pie_dominante;
    private int number, teamid, avatar, playerid;
     @JsonIgnore
    String mFireBaseKey;

    public Players() {
        //required empty constructor for jackson lib.
    }

    public Players(String name, int avatar, String division, String alias, String position, int number, int teamid) {
        avatar = R.drawable.ic_player_icon;
        this.name = name;
        this.avatar = avatar;
        this.division = division;
        this.alias = alias;
        this.position = position;
        this.number = number;
        this.teamid = teamid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getmFireBaseKey() {
        return mFireBaseKey;
    }

    public void setmFireBaseKey(String mFireBaseKey) {
        this.mFireBaseKey = mFireBaseKey;
    }



    @Override
    public String toString() {
        return "Players{" +
                "mPlayerName='" + name + '\'' +
                ", mPlayerNum=" + number +
                '}';
    }

}
