package upgrade.ntv.bangsoccer;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static upgrade.ntv.bangsoccer.AppConstants.AppConstant.populateDummyClubsItems;

/**
 * Created by jfrom on 5/28/2016.
 */

public class AppicationCore extends Application {

    public static DatabaseReference databaseReference ;
    public  static DatabaseReference mPlayersDeftailsRef ;
    public  static DatabaseReference mTeamsRef ;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mPlayersDeftailsRef = databaseReference.child("Players");
        mTeamsRef = databaseReference.child("Team");
        populateDummyClubsItems();    }
}
