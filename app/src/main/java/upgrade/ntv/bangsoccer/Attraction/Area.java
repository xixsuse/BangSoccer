package upgrade.ntv.bangsoccer.Attraction;

import com.google.android.gms.maps.model.LatLng;

import upgrade.ntv.bangsoccer.R;


/**
 * Created by frO on 11/11/2015.
 */
public class Area {

    public static final int AREA_TYPE_TOURISTIC = 501;
    public static final int AREA_TYPE_RECREATIONAL = 502;

    private int id;
    private String name;
    private LatLng geo;
    private int type;

    public Area() {
    }

    public Area(int id, String name, LatLng geo, int type) {
        this.id = id;
        this.name = name;
        this.geo = geo;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getGeo() {
        return geo;
    }

    public int getType() {
        return type;
    }

    public String getStringType() {
        String areaType;
        switch (type) {
            case AREA_TYPE_RECREATIONAL:
                areaType = "Recreational Area";
                break;
            default:
                areaType = "Unknown Area Type";
        }
        return areaType;
    }

    public int getIcon() {
        int icon;
        switch (type) {
            case AREA_TYPE_RECREATIONAL:
                icon = R.drawable.ic_terrain_black_24dp;
                break;
            default:
                icon = R.drawable.ic_place_black_24dp;
        }
        return icon;
    }
}
