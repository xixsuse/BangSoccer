package upgrade.ntv.bangsoccer.Schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose on 3/15/2015.
 */
public class Players {
    // Used for a query
    public static final String TEAM_ID = "teamid";

    private String name, division, alias,  position, goals, assistance, nationality, height, weight, dominant_foot;
    private int number, teamid, avatar, red_card, yellow_card;
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

    public Players(String name, String division, String alias, String position, String goals, String nationality, String height,
                   String weight, String dominant_foot, int number, int teamid, int avatar, int red_card, int yellow_card) {
        this.name = name;
        this.division = division;
        this.alias = alias;
        this.position = position;
        this.goals = goals;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.dominant_foot = dominant_foot;
        this.number = number;
        this.teamid = teamid;
        this.avatar = avatar;
        this.red_card = red_card;
        this.yellow_card = yellow_card;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public int getRed_card() {
        return red_card;
    }

    public void setRed_card(int red_card) {
        this.red_card = red_card;
    }

    public int getYellow_card() {
        return yellow_card;
    }

    public void setYellow_card(int yellow_card) {
        this.yellow_card = yellow_card;
    }

    public String getAssistance() {
        return assistance;
    }

    public void setAssistance(String assistance) {
        this.assistance = assistance;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDominant_foot() {
        return dominant_foot;
    }

    public void setDominant_foot(String dominant_foot) {
        this.dominant_foot = dominant_foot;
    }

    public String getCards(){
        StringBuilder cards = new StringBuilder();
        return  cards
                .append(String.valueOf(getYellow_card()))
                .append(" Amarillas")
                .append(" y ")
                .append(String.valueOf(getRed_card()))
                .append(" Rojas").toString();
    }

    public String getWeightNHeight(){
        StringBuilder weight = new StringBuilder();
        return  weight
                .append(getWeight())
                .append(" lbs ")
                .append("| ")
                .append(getHeight())
                .append(" Pies").toString();
    }

    public String getAliasNNumber(){
        StringBuilder alias= new StringBuilder();
        return  alias
                .append(getAlias())
                .append(", ")
                .append(getNumber()).toString();
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