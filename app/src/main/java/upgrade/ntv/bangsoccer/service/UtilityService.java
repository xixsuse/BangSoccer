package upgrade.ntv.bangsoccer.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.TimeUnit;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Notifications.NotificationRequest;
import upgrade.ntv.bangsoccer.Utils.Utils;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.google.android.gms.location.LocationServices.GeofencingApi;

/**
 * A utility IntentService, used for a variety of asynchronous background
 * operations that do not necessarily need to be tied to a UI.
 */
public class UtilityService extends IntentService {

    //FOR LOG PURPOSES
    private static final String TAG = UtilityService.class.getSimpleName();

    private static final String ACTION_GET_AREAS = "get_areas";
    private static final String ACTION_GET_ATTRACTIONS = "get_attractions";
    private static final String ACTION_GET_BEACONS = "get_beacons";

    public static final String ACTION_GEOFENCE_TRIGGERED = "geofence_triggered";
    private static final String ACTION_LOCATION_UPDATED = "location_updated";
    private static final String ACTION_REQUEST_LOCATION = "request_location";
    private static final String ACTION_ADD_GEOFENCES = "add_geofences";
    private static final String ACTION_FAKE_UPDATE = "fake_update";
    private static final String ACTION_GET_ALL_BEACONS = "get_all_beacons";
    private static final String ACTION_RANGE_BEACONS_FOR_CONTENT = "range_beacons_for_context";

    public UtilityService() {
        super(TAG);
    }

    public static IntentFilter getLocationUpdatedIntentFilter() {
        return new IntentFilter(UtilityService.ACTION_LOCATION_UPDATED);
    }

    public static void getAttractions(Context context, int areaId) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_GET_ATTRACTIONS);
        intent.putExtra(ACTION_GET_ATTRACTIONS, areaId);
        context.startService(intent);
    }

    public static void addGeofences(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_ADD_GEOFENCES);
      //  context.startService(intent);
    }

    public static void requestLocation(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_REQUEST_LOCATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;

        switch (action) {
            case ACTION_GET_ATTRACTIONS:
                getAttractionsRequest(intent);
                break;
            case ACTION_ADD_GEOFENCES:
                addGeofences();
                break;
            case ACTION_GEOFENCE_TRIGGERED:
                geofenceTriggered(intent);
                break;
            case ACTION_REQUEST_LOCATION:
                requestDeviceLocation();
                break;
            case ACTION_LOCATION_UPDATED:
                locationUpdated(intent);
                break;
        }
    }

    /********************************************************************************************
     * JSON Requests
     ********************************************************************************************/
    private void getAttractionsRequest(Intent intent) {
    /*    int areaId = intent.getIntExtra(ACTION_GET_ATTRACTIONS, 0);

        if (areaId != 0){
            JSONHandler jsonHandler = new JSONHandler(this);
            jsonHandler.getAttractionsFromAreaRequest(areaId);
        }*/
    }

    /**
     * Add geofences using Play Services
     */
    private void addGeofences() {
        Log.i(TAG, ACTION_ADD_GEOFENCES);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, new Intent(this, UtilityReceiver.class), 0);

            GeofencingRequest geofenceRequest =
                    new GeofencingRequest.Builder()
                            .addGeofences(ActivityMain.getGeofenceList())
                            .build();

            GeofencingApi.addGeofences(
                    googleApiClient,
                    geofenceRequest,
                    pendingIntent);
            googleApiClient.disconnect();

        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when a geofence is triggered
     */
    private void geofenceTriggered(Intent intent) {
        Log.v(TAG, ACTION_GEOFENCE_TRIGGERED);

        // Check if geofences are enabled
        boolean geofenceEnabled = Utils.getGeofenceEnabled(this);

        // Extract the geofences from the intent
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = event.getTriggeringGeofences();

        if (geofenceEnabled && geofences != null && geofences.size() > 0) {
            if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
                // Trigger the notification based on the first geofence

                int areaId = Integer.valueOf(geofences.get(0).getRequestId());
                for (Area area : ActivityMain.mAreasArrayList) {
                    if(areaId == area.getId()){

                        // Trigger the notification based on the first geofence
                        new NotificationRequest.Builder()
                                .setContext(this)
                                .setId(NotificationRequest.NOTIFICATION_ID_ENTER_GEOLOCATION)
                                .setArea(area)
                                .build()
                                .post();
                    }
                }

            } else if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT) {
                // Clear notifications
                // TODO: 11/18/2015 decide what to do when user exits a geofence
            }
        }
        UtilityReceiver.completeWakefulIntent(intent);
    }

    /**
     * Called when a location update is requested
     */
    private void requestDeviceLocation() {
        Log.v(TAG, ACTION_REQUEST_LOCATION);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            Intent locationUpdatedIntent = new Intent(this, UtilityService.class);
            locationUpdatedIntent.setAction(ACTION_LOCATION_UPDATED);

            // Send last known location out first if available
            Location location = FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                Intent lastLocationIntent = new Intent(locationUpdatedIntent);
                lastLocationIntent.putExtra(
                        FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
                startService(lastLocationIntent);
            }

            // Request new location
            LocationRequest mLocationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest,
                    PendingIntent.getService(this, 0, locationUpdatedIntent, 0));

            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when the location has been updated
     */
    private void locationUpdated(Intent intent) {
        Log.v(TAG, ACTION_LOCATION_UPDATED);

        // Extra new location
        Location location =
                intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null) {
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Store in a local preference as well
            Utils.storeLocation(this, latLngLocation);

            // Send a local broadcast so if an Activity is open it can respond
            // to the updated location
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }


}
