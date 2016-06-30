package upgrade.ntv.bangsoccer.AppConstants;



/**
 * Created by Paulino on 10/9/2015.
 */
public class Constants {

    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    public static final int REQUEST_CODE_ENABLE_BLE = 1001;
    public static final String PREFS_NAME = "upgrade.ntv.superleague.Prefs";
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    static final int REQUEST_CODE_PLACE_PICKER = 1003;
    public static final int CLUBS_ACTIVITY_BY_TEAM =100;
    public static final int TOURNAMENT_ACTIVITY =0;
    public static final int NEWS_FEED_DETAILS_ACTIVITY =1;
    public static final int LOADING_SUCCESS =1;
    public static final int LOADING_FAILURE =0;

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";
    // Used to size the images in the mobile app so they can animate cleanly from list to detail
    public static final int IMAGE_ANIM_MULTIPLIER = 2;
    // Resize images sent to Wear to 400x400px
    public static final int NOTIFICATION_IMAGE_SIZE = 400;
    // Max # of attractions to show at once
    public static final int MAX_ATTRACTIONS = 4;

    // Maps values
    public static final String MAPS_INTENT_URI = "geo:0,0?q=";
    public static final String MAPS_NAVIGATION_INTENT_URI = "google.navigation:mode=w&q=";


    private Constants() {
    }
}