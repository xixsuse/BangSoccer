package upgrade.ntv.bangsoccer.Notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Attraction.Attraction;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.service.UtilityService;

import static upgrade.ntv.bangsoccer.Notifications.NotificationTools.zzb;


/**
 * This class handles notification posts based on a Notification ID
 */
public class NotificationRequest {
    /**
     * Notification Identifiers
     */
    public static final int NOTIFICATION_ID_ENTER_GEOLOCATION = 101;
    public static final int NOTIFICATION_ID_EXIT_GEOLOCATION = 102;
    public static final int NOTIFICATION_ID_ENTER_REGION = 103;
    public static final int NOTIFICATION_ID_EXIT_REGION = 104;

    /**
     * Member Variables
     */
    // TODO: 11/17/2015 Update variables based on needs 
    private final Context mContext;
    private final int mId;
    private final Area mArea;


    // Constructor
    NotificationRequest(Context context, int id, Area area) {
        this.mContext = context;
        this.mId = id;
        this.mArea = area;

    }

    /**
     * Chooses the method to be executed based on the defined ID.
     *
     * @return wetter notification was posted successfully.
     */
    public boolean post() {

        switch (mId) {
            case NOTIFICATION_ID_ENTER_GEOLOCATION:
                return postEnteredGeofenceNotification();

            case NOTIFICATION_ID_EXIT_GEOLOCATION:
                break;

            case NOTIFICATION_ID_ENTER_REGION:
                return postEnteredRegionNotification();

            case NOTIFICATION_ID_EXIT_REGION:
                break;

            default:
                return false;

        }
        return false;
    }

    public boolean postEnteredRegionNotification() {

        //Attraction attraction = getAttractionFromRegion(mRegion);
        Attraction attraction = getAttractionFromRegion();
        // The intent to trigger when the notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, ActivityMain.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the main notification
        if (attraction != null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setContentTitle(attraction.getName())
                    .setContentText(attraction.getStringType())
                    .setSmallIcon(attraction.getIcon())
                    .setContentIntent(pendingIntent)
                    .setColor(mContext.getResources().getColor(R.color.colorPrimary /*getTheme()*/))
                    .setCategory(Notification.CATEGORY_RECOMMENDATION)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            // Trigger the notification
            NotificationManagerCompat.from(mContext).notify(
                    NOTIFICATION_ID_ENTER_REGION, builder.build());
        }
        return true;
    }

    private Attraction getAttractionFromRegion(){
        Attraction at=null;
        for (Attraction attraction : ActivityMain.mAttractionsArrayList) {
            at=attraction;
        }
        return at;
    }

    /**
     * Notification to be posted when the device enters a GeoZone
     *
     * @return wetter notification was posted successfully.
     */
    private boolean postEnteredGeofenceNotification() {

        // Gets list of attractions for current area from the database
        UtilityService.getAttractions(mContext, mArea.getId());

        // The intent to trigger when the notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, ActivityMain.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // The bitmap to display when the notification is expanded
        Bitmap bitmap = BitmapFactory.decodeResource(
                mContext.getResources(),
                R.drawable.bg_upgrade);

        // Construct the main notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .setBigContentTitle(mArea.getName())
                                .setSummaryText(mContext.getString(R.string.nearby_attraction))
                )
                .setContentTitle(mArea.getName())
                .setContentText(mArea.getStringType())
                .setSmallIcon(mArea.getIcon())
                .setContentIntent(pendingIntent)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary /*getTheme()*/))
                .setCategory(Notification.CATEGORY_RECOMMENDATION)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Trigger the notification
        NotificationManagerCompat.from(mContext).notify(
                NOTIFICATION_ID_ENTER_GEOLOCATION, builder.build());

        return true;
    }

    /**
     * NotificationRequest Builder Inner Class. Creates a notification request
     * based on given parameters
     */
    public static final class Builder {
        private Context context = null;
        private int id;
        private Area area = null;


        // Default Constructor
        public Builder() {
        }

        /**
         * Defines launcher context for the notification.
         *
         * @param context Is used to define the context from
         *                where the notification was triggered
         * @return builder
         */
        public NotificationRequest.Builder setContext(Context context) {
            zzb(context, "context can\'t be null");
            this.context = context;
            return this;
        }

        /**
         * Defines the type of notification to be triggered. id needs to
         * be set to one of the NOTIFICATION_ID values on the NotificationRequest class
         *
         * @param id Notification type/style
         * @return builder
         */
        public NotificationRequest.Builder setId(int id) {
            if (id >= 100 && id <= 104) {
                this.id = id;
            } else {
                throw new IllegalArgumentException("Invalid id, Refer to id documentation for more details");
            }
            return this;
        }

        /**
         * Area needs to be set if @param id was set to:
         * NOTIFICATION_ID_ENTER_GEOLOCATION or
         * NOTIFICATION_ID_EXIT_GEOLOCATION
         *
         * @param area is used to get values for the notification
         * @return builder
         */
        public NotificationRequest.Builder setArea(Area area) {
            if (id == NOTIFICATION_ID_ENTER_GEOLOCATION ||
                    id == NOTIFICATION_ID_EXIT_GEOLOCATION) {
                zzb(area, "area can\'t be null");
                this.area = area;
            } else {
                throw new IllegalArgumentException("id set does not support Area objects. Please set a different notification id.");
            }
            return this;
        }

        /**
         * Region needs to be set if @param id was set to:
         * NOTIFICATION_ID_ENTER_REGION or
         * NOTIFICATION_ID_EXIT_REGION
         *
         * @param region is used to get values for the notification
         * @return builder
         */
      /*  public NotificationRequest.Builder setRegion(Region region) {
            if (id == NOTIFICATION_ID_ENTER_REGION ||
                    id == NOTIFICATION_ID_EXIT_REGION) {
                zzx.zzb(region, "region can\'t be null");
                this.region = region;
            } else {
                throw new IllegalArgumentException("id set does not support Region objects. Please set a different notification id.");
            }
            return this;
        }*/

        /**
         * Builds the notification request
         *
         * @return NotificationRequest
         */
        public NotificationRequest build() {
            return new NotificationRequest(
                    this.context,
                    this.id,
                    this.area);
        }
    }
}
