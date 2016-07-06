package upgrade.ntv.bangsoccer.TourList;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Attraction.Attraction;

/**
 * Static data content provider.
 */
public class TouristAttractions {

    public static final HashMap<String, Area> AREA_LOCATIONS = new HashMap<>();

    public static final HashMap<String, List<Attraction>> ATTRACTIONS = new HashMap<>();

    public static String getClosestCity(LatLng curLatLng) {
        if (curLatLng == null) {
            // If location is unknown return test city so some data is shown
            return ActivityMain.mAreasArrayList.get(0).getName();
        }

        for(Area area: ActivityMain.mAreasArrayList){
            AREA_LOCATIONS.put(area.getName(), area);
            ATTRACTIONS.put(area.getName(),ActivityMain.mAttractionsArrayList);
        }


        double minDistance = 0;
        String closestCity = null;
        for (Map.Entry<String, Area> entry : AREA_LOCATIONS.entrySet()) {
            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue().getGeo());
            if (minDistance == 0 || distance < minDistance) {
                minDistance = distance;
                closestCity = entry.getKey();
            }
        }
        return closestCity;
    }
}