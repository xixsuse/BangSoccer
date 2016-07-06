package upgrade.ntv.bangsoccer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import upgrade.ntv.bangsoccer.R;


/**
 * Created by Paulino on 10/13/2015.
 */
public class Preferences {

    public static String getPreferredMapStyle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(
                context.getString(R.string.pref_key_map_style),
                context.getString(R.string.pref_default_map_style));
    }
}
