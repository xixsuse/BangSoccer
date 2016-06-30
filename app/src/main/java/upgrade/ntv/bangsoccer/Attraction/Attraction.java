package upgrade.ntv.bangsoccer.Attraction;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import upgrade.ntv.bangsoccer.R;

/**
 * A simple shared tourist attraction class to easily pass data around.
 */
public class Attraction {

    public static final int BUILDING = 601;
    public static final int ATTRACTION_TYPE_RUIN = 602;
    public static final int ATTRACTION_TYPE_MONUMENT = 603;
    public static final int ATTRACTION_TYPE_MUSEUM = 604;
    public static final int ATTRACTION_TYPE_CATHEDRAL = 605;
    public static final int ATTRACTION_TYPE_CHURCH = 606;
    public static final int ATTRACTION_TYPE_PARK = 607;

    private int id;
    private String name;
    private LatLng geo;
    private String shortDescription;
    private String longDescription;
    private Uri imageUrl;
    private Uri secondaryImageUrl;
    private int type;
    private String schedule;
    private String address;
    private String notes;
    private int areaId;
   

    private Drawable image;
    private Bitmap secondaryImage;
    private String distance;

    public Attraction() {
    }

    public Attraction(int id, String name, LatLng geo, String shortDescription,
                      String longDescription, Drawable imageUrl, int type, String schedule, String address, String notes) {
        this.id = id;
        this.name = name;
        this.geo = geo;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.image = imageUrl;
        this.type = type;
        this.schedule = schedule;
        this.address = address;
        this.notes = notes;
 
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

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public Drawable getImage() {
        return image;
    }

    public Uri getSecondaryImageUrl() {
        return secondaryImageUrl;
    }

    public int getType() {
        return type;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    public int getAreaId() {
        return areaId;
    }


    public String getStringType() {

        switch (type) {
            case BUILDING:
                return "Terreno";
            default:
                return "Unknown Attraction Type";
        }
    }

    public int getIcon() {
        switch (type) {
            case BUILDING:
                return R.drawable.ic_terrain_black_24dp;
            default:
                return R.drawable.ic_place_black_24dp;
        }
    }
}