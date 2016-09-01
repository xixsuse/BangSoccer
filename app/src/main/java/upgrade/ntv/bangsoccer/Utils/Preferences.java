package upgrade.ntv.bangsoccer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import upgrade.ntv.bangsoccer.R;


/**
 * Created by Paulino on 10/13/2015.
 */
public class Preferences {

    public static String DEFAULT_DIV_VALUE_NOT_FOUND = "no_value_for_such_key";

    public static String getPreferredMapStyle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(
                context.getString(R.string.pref_key_map_style),
                context.getString(R.string.pref_default_map_style));
    }
    public static boolean getPreferredDivisions(Context context, String division){ //update
        SharedPreferences prefs = context.getSharedPreferences
                (context.getString(R.string.pref_key_divisions),
                        Context.MODE_PRIVATE);
        //returns default value if the key does not exist.e
        boolean result = prefs.getBoolean(division, false);

        return result;
    }

    public static void setPreferredDivisions(Context context, String division){ //update
        //getl divisions preferences
        SharedPreferences prefs = context.getSharedPreferences
                (context.getString(R.string.pref_key_divisions),
                Context.MODE_PRIVATE);
        //create editor
        SharedPreferences.Editor editor = prefs.edit();
        //input data
        editor.putBoolean(division, true);
        // Commit the edits
        editor.commit();
    }

    public static void removePreferredDivisions(Context context, String division){ //update
        //getl divisions preferences
        SharedPreferences prefs = context.getSharedPreferences
                (context.getString(R.string.pref_key_divisions),
                        Context.MODE_PRIVATE);
        //create editor
        SharedPreferences.Editor editor = prefs.edit();
        //input data
        editor.remove(division);
        // Commit the edits
        editor.commit();
    }




}