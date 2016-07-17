package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class Main {
    public static void main(String[] args) throws Exception{

        Schema schema  = new Schema(1,"upgrade.ntv.bangsoccer.dao"); // # is the Schema version
        schema.enableKeepSectionsByDefault();
        createDataBase(schema);

        DaoGenerator generator = new DaoGenerator();
        generator.generateAll(schema, args[0]);


    }

    private static void createDataBase(Schema schema) {

        // ******** Entities ********
        Entity club = createClubTable(schema);
        Entity tourney = createTourneyTable(schema);
        Entity team = createTeamTable(schema);
        Entity match = createMatchTable(schema);
        Entity player = createPlayerTable(schema);
        Entity matches_player_details = createMatchesPlayerDetailsTable(schema);
        Entity newsFeed = createNewsFeedTable(schema);

        // ******** Properties ********
        Property clubIDForTeam = team.addLongProperty("ClubID").notNull().getProperty();
        Property tourneyIDForMatch = match.addLongProperty("TourneyID").notNull().getProperty();
        Property teamIDAForMatch = match.addLongProperty("TeamID_A").notNull().getProperty();
        Property teamIDBForMatch = match.addLongProperty("TeamID_B").notNull().getProperty();
        Property matchIDForMatchesPlayerDetails = matches_player_details.addLongProperty("MatchID").notNull().getProperty();
        Property playerIDForMatchesPlayerDetails = matches_player_details.addLongProperty("PlayerID").notNull().getProperty();
        Property teamIDForPlayer = player.addLongProperty("TeamID").notNull().getProperty();


        // ******** Relationships between entities ********
        team.addToOne(club, clubIDForTeam, "club");
        match.addToOne(tourney, tourneyIDForMatch, "tourney");
        match.addToOne(team, teamIDAForMatch, "teamA");
        match.addToOne(team, teamIDBForMatch, "teamB");
        matches_player_details.addToOne(match, matchIDForMatchesPlayerDetails, "match");
        matches_player_details.addToOne(player, playerIDForMatchesPlayerDetails, "player");
        player.addToOne(team, teamIDForPlayer, "team");

    }

    private static Entity createNewsFeedTable(Schema schema) {
        Entity newsFeed = schema.addEntity("DBNewsFeed");
        newsFeed.addIdProperty();
        newsFeed.addStringProperty("Description");
        newsFeed.addByteArrayProperty("Picture");

        return newsFeed;
    }

    private static Entity createMatchesPlayerDetailsTable(Schema schema) {
        Entity matchesPlayerDetails = schema.addEntity("DBMatchesPlayerDetails");
        matchesPlayerDetails.addIdProperty();
        matchesPlayerDetails.addIntProperty("Goals");
        matchesPlayerDetails.addIntProperty("YellowCards");
        matchesPlayerDetails.addIntProperty("RedCards");
        matchesPlayerDetails.addIntProperty("Assist");
        matchesPlayerDetails.addBooleanProperty("Active").notNull();
        return matchesPlayerDetails;
    }

    private static Entity createPlayerTable(Schema schema) {
        Entity player = schema.addEntity("DBPlayer");
        player.addIdProperty();
        player.addStringProperty("FirstName").notNull();
        player.addStringProperty("LastName");
        player.addStringProperty("Alias");
        player.addStringProperty("Gender");
        player.addStringProperty("Nationality");
        player.addStringProperty("Number");
        player.addStringProperty("Position");
        player.addStringProperty("Email");
        player.addDateProperty("BirthDate");
        player.addStringProperty("Height");
        player.addStringProperty("Weight");
        player.addStringProperty("Feet"); // right or left
        player.addBooleanProperty("Active").notNull();
        player.addByteArrayProperty("Avatar");
        return player;
    }

    private static Entity createMatchTable(Schema schema) {
        Entity match = schema.addEntity("DBMatch");
        match.addIdProperty();
        //match.addDateProperty("Date");
        match.addStringProperty("Date");
        match.addStringProperty("Stadium");
        match.addStringProperty("Time");
        match.addIntProperty("Week"); // WeeklyScheduleID
        return match;
    }

    private static Entity createTeamTable(Schema schema) {
        Entity team = schema.addEntity("DBTeam");
        team.addIdProperty();
        team.addStringProperty("Name").notNull();
        team.addBooleanProperty("Active").notNull();
        return team;
    }

    private static Entity createTourneyTable(Schema schema) {
        Entity tourney = schema.addEntity("DBTourney");
        tourney.addIdProperty();
        tourney.addStringProperty("Name").notNull();
        tourney.addBooleanProperty("Active").notNull();
        tourney.addStringProperty("Location");
        tourney.addStringProperty("StartDate");
        return tourney;
    }

    private static Entity createClubTable(Schema schema) {
        Entity club = schema.addEntity("DBClub");
        club.addIdProperty();
        club.addStringProperty("Name").notNull();
        club.addBooleanProperty("Active").notNull();
        club.addStringProperty("Stadium");
        club.addByteArrayProperty("Logo");
        return club;
    }



}
