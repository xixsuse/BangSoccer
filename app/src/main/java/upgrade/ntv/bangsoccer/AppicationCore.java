package upgrade.ntv.bangsoccer;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
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

    /*    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });
*/

        if (databaseReference == null){
            try{
                if (!FirebaseApp.getApps(this).isEmpty()) {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                }
            }catch (Exception e) {
              //  FirebaseCrash.log("firebase Crash reports  failed to initialize");
                Log.i("firebase persistance: ", e.getMessage());
            }finally
             {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                mPlayersDeftailsRef = databaseReference.child("Players");
                mTeamsRef = databaseReference.child("Team");
            }
        }

        populateDummyClubsItems();    }
}
