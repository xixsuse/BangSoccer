package upgrade.ntv.bangsoccer.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


/**
 * A simply utility receiver used to ensure the device stays awake for the
 * duration of the work being done
 */
public class UtilityReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = UtilityReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, UtilityService.ACTION_GEOFENCE_TRIGGERED);

        // Pass right over to UtilityService class, the wakeful receiver is
        // just needed in case the geofence is triggered while the device
        // is asleep otherwise the service may not have time to trigger the
        // notification.
        intent.setClass(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_GEOFENCE_TRIGGERED);
        startWakefulService(context, intent);

    }

}