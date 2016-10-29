package upgrade.ntv.bangsoccer.Drawer;

import android.app.Activity;
import android.content.Intent;

import upgrade.ntv.bangsoccer.ActivityAbout;
import upgrade.ntv.bangsoccer.ActivityClubSelect;
import upgrade.ntv.bangsoccer.ActivityClubs;
import upgrade.ntv.bangsoccer.ActivityField;
import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.ActivityNewsDetails;
import upgrade.ntv.bangsoccer.ActivityTournament;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.booking.MainActivity;

/**
 * Created by xeros on 10/19/2015.
 */
public class DrawerSelector {

    private static int selectedItem;

    public static Intent onItemSelected(Activity callingActivity, int id) {

        selectedItem = id;

        Intent intent = null;

        switch (selectedItem) {


            case R.id.nav_reservation:
                intent = new Intent(callingActivity, MainActivity.class);
                break;
            case R.id.nav_main:
                intent = new Intent(callingActivity, ActivityMain.class);
                break;
            case R.id.nav_field:
                intent = new Intent(callingActivity, ActivityField.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                break;
            case R.id.nav_clubs:
                //TODO: add history activity
                intent = new Intent(callingActivity, ActivityClubSelect.class);

                break;
            // 100 = ActivityClubs
            case Constants.CLUBS_ACTIVITY_BY_TEAM:
                intent = new Intent(callingActivity, ActivityClubs.class);

                break;

            case R.id.nav_dynamic_tourney:
                intent = new Intent(callingActivity, ActivityTournament.class);

                break;
          /*  case R.id.nav_settings:
                intent = new Intent(callingActivity, ActivitySettings.class);
                break;*/
           case R.id.nav_about:
                intent = new Intent(callingActivity, ActivityAbout.class);
                break;

            // 0 = ActivityTournament
            case Constants.TOURNAMENT_ACTIVITY:
                intent = new Intent(callingActivity, ActivityTournament.class);
                break;
            case Constants.NEWS_FEED_DETAILS_ACTIVITY:
                intent = new Intent(callingActivity, ActivityNewsDetails.class);
                break;

//            case R.id.nav_facebook:
//                intent = new Intent(callingActivity, ActivityFacebook.class);
//                break;


           /* case R.id.users_nav_view_item:
               // intent = new Intent(callingActivity, SignedInActivity.class);
                break;*/


            default:
        }

        return intent;
    }
}
