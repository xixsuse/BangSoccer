package upgrade.ntv.bangsoccer;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import upgrade.ntv.bangsoccer.dao.DBClub;
import upgrade.ntv.bangsoccer.dao.DBClubDao;
import upgrade.ntv.bangsoccer.dao.DBMatch;
import upgrade.ntv.bangsoccer.dao.DBMatchDao;
import upgrade.ntv.bangsoccer.dao.DBMatchesPlayerDetailsDao;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;
import upgrade.ntv.bangsoccer.dao.DBNewsFeedDao;
import upgrade.ntv.bangsoccer.dao.DBPlayer;
import upgrade.ntv.bangsoccer.dao.DBPlayerDao;
import upgrade.ntv.bangsoccer.dao.DBTeam;
import upgrade.ntv.bangsoccer.dao.DBTeamDao;
import upgrade.ntv.bangsoccer.dao.DBTourney;
import upgrade.ntv.bangsoccer.dao.DBTourneyDao;
import upgrade.ntv.bangsoccer.dao.DaoMaster;
import upgrade.ntv.bangsoccer.dao.DaoSession;


/**
 * Created by jfrom on 5/28/2016.
 */

public class AppicationCore extends Application {

    //GreenDao variables
    private static DaoMaster daoMaster;
    private static SQLiteDatabase db;

    private static DBClubDao dbClubDao;
    private static DBTourneyDao dbTourneyDao;
    private static DBTeamDao dbTeamDao;
    private static DBMatchDao dbMatchDao;
    private static DBPlayerDao dbPlayerDao;
    private static DBMatchesPlayerDetailsDao dbMatchesPlayerDetailsDao;
    private static DBNewsFeedDao dbNewsFeedDao;

    public static String FRAGMENT_CHOOSE_DIVISION;

    @Override
    public void onCreate() {
        super.onCreate();

      /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });*/

        // Database Initialization
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "soccerDB", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();  // to avoid memory leaks

        dbClubDao = daoSession.getDBClubDao();
        dbMatchDao = daoSession.getDBMatchDao();
        dbMatchesPlayerDetailsDao = daoSession.getDBMatchesPlayerDetailsDao();
        dbPlayerDao = daoSession.getDBPlayerDao();
        dbTeamDao = daoSession.getDBTeamDao();
        dbTourneyDao = daoSession.getDBTourneyDao();
        dbNewsFeedDao = daoSession.getDBNewsFeedDao();
    }


    /**
     * Reset all values stored in the db
     */
    public static void resetDB() {
        daoMaster.dropAllTables(db, true);
        daoMaster.createAllTables(db, false);
        ;
    }

    /**
     * Reset values from a NewsFeed table
     */
    public static void resetNewsFeedTable() {
        dbNewsFeedDao.deleteAll();
    }


// **************** Get Dao methods == newInstance ****************

    public static DBClubDao getDbClubDao() {
        return dbClubDao;
    }

    public static DBTourneyDao getDbTourneyDao() {
        return dbTourneyDao;
    }

    public static DBTeamDao getDbTeamDao() {
        return dbTeamDao;
    }

    public static DBMatchDao getDbMatchDao() {
        return dbMatchDao;
    }

    public static DBPlayerDao getDbPlayerDao() {
        return dbPlayerDao;
    }

    public static DBMatchesPlayerDetailsDao getDbMatchesPlayerDetailsDao() {
        return dbMatchesPlayerDetailsDao;
    }

    public static DBNewsFeedDao getDbNewsFeedDao() {
        return dbNewsFeedDao;
    }


// **************** General getter methods ****************

    /**
     * Get all the clubs in te db
     *
     * @return a list of clubs
     */
    public static List<DBClub> getClubs() {
        return dbClubDao.loadAll();
    }

    /**
     * Get a club based on the ID
     *
     * @param clubID
     * @return a DBClub object
     */
    public static DBClub getClub(long clubID) {
        return dbClubDao.load(clubID);
    }

    /**
     * Get club based on team ID
     *
     * @param teamID
     * @return
     */
    public static DBClub getClubByTeamID(long teamID) {

        DBTeam team = dbTeamDao.load(teamID);
        return dbClubDao.load(team.getClubID());
    }

    /**
     * Get all the teams under a specific club
     *
     * @param clubID
     * @return a list of teams
     */
    public static List<DBTeam> getTeams(long clubID) {

        QueryBuilder<DBTeam> queryBuilder = dbTeamDao.queryBuilder();
        queryBuilder.where(DBTeamDao.Properties.ClubID.eq(clubID));

        return queryBuilder.list();
    }

    /**
     * Get a team based on the ID
     *
     * @param teamID
     * @return a DBTeam object
     */
    public static DBTeam getTeam(long teamID) {
        return dbTeamDao.load(teamID);
    }

    /**
     * Get all the players under a specific team
     *
     * @param teamID
     * @return a list of players
     */
    public static List<DBPlayer> getPlayers(long teamID) {

        QueryBuilder<DBPlayer> queryBuilder = dbPlayerDao.queryBuilder();
        queryBuilder.where(DBPlayerDao.Properties.TeamID.eq(teamID));

        return queryBuilder.list();
    }

    /**
     * Get a player based on the ID
     *
     * @param playerID
     * @return a DBPlayer object
     */
    public static DBPlayer getPlayer(long playerID) {
        return dbPlayerDao.load(playerID);
    }


    /**
     * Get all the tourneys that are active
     *
     * @return a list of tourneys
     */
    public static List<DBTourney> getActiveTourneys() {

        QueryBuilder<DBTourney> queryBuilder = dbTourneyDao.queryBuilder();
        queryBuilder.where(DBTourneyDao.Properties.Active.eq(true));

        return queryBuilder.list();
    }


    /**
     * Get a tourney based on the ID
     *
     * @param tourneyID
     * @return a DBTourney object
     */
    public static DBTourney getTourney(long tourneyID) {
        return dbTourneyDao.load(tourneyID);
    }

    /**
     * Get all the matches with same WeeklyScheduleID
     *
     * @return a list of matches
     */
    public static List<DBMatch> getMatches(int WeeklyScheduleID) {

        QueryBuilder<DBMatch> queryBuilder = dbMatchDao.queryBuilder();
        queryBuilder.where(DBMatchDao.Properties.Week.eq(WeeklyScheduleID));

        return queryBuilder.list();
    }

    /**
     * Get all the matches
     *
     * @return a list of matches
     */
    public static List<DBMatch> getMatches() {
        return dbMatchDao.loadAll();
    }


    /**
     * Get all the News Feed in te db
     *
     * @return a list of NewsFeed
     */
    public static List<DBNewsFeed> getAllNewsFeed() {
        return dbNewsFeedDao.loadAll();
    }

    /**
     * Get a club based on the ID
     *
     * @param newsFeedID
     * @return a DBClub object
     */
    public static DBNewsFeed getNewsFeed(long newsFeedID) {
        return dbNewsFeedDao.load(newsFeedID);
    }


}
