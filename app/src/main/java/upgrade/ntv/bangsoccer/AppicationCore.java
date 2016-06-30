package upgrade.ntv.bangsoccer;

import android.app.Application;

import static upgrade.ntv.bangsoccer.AppConstants.AppConstant.populateDummyClubsItems;

/**
 * Created by jfrom on 5/28/2016.
 */

public class AppicationCore extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        populateDummyClubsItems();    }
}
