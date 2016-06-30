package upgrade.ntv.bangsoccer.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by xeros on 11/5/2015.
 */
public class Permissions {

    /**
     * Check if the app has access to fine location permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkFineLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean checkInternetPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.INTERNET);
    }

}
