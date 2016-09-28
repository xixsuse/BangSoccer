package upgrade.ntv.bangsoccer.Entities;


import upgrade.ntv.bangsoccer.R;

/**
 * Created by Jose on 3/15/2015.
 */
public class Players {
    // Used for a query
    public static final String TEAM_ID = "teamid";

    private String name, teamid, division, alias,  position, goals, assistance, nationality, height, weight, dominant_foot, team;
    private int number,  avatar = 0 , red_card, yellow_card;

    String mFireBaseKey;

    public Players() {
        //required empty constructor for jackson lib.
    }



    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    public int getAvatar() {
        if (avatar != 0){
            return avatar;
        }
        return R.drawable.ic_player_name_icon;
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
