package upgrade.ntv.bangsoccer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.NewsFeedAdapter;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Attraction.Attraction;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.Utils.JsonReader;
import upgrade.ntv.bangsoccer.Utils.JsonWriter;
import upgrade.ntv.bangsoccer.Utils.Permissions;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;
import upgrade.ntv.bangsoccer.service.UtilityService;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    public static final int PERMISSION_REQUEST_INTERNET = 1;
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    public static final float TRIGGER_RADIUS = 4000; // 50m
    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    // List of sites
    public static ArrayList<Area> mAreasArrayList = new ArrayList<>();
    public static ArrayList<Attraction> mAttractionsArrayList = new ArrayList<>();

    // name of the file to preserve areas
    private final String AREAS_DATA_FILE_NAME = "areas_data";
    private DrawerLayout drawer;
    private Activity thisActivity;
    private NewsFeedAdapter newsFeedAdapter;
    private List<NewsFeedItem> newsFeedItems = new ArrayList<>();

    private ProgressBar progressBar;
    private MenuItem refreshButton;
    private  RecyclerView recyclerView;
    private List<String> mFacebookAccounts;
    private boolean refreshStatus=false;  // true: when refresh newsfeeds is in progress

    private GridLayoutManager lLayout;


    @Override
    protected void onResume() {
        super.onResume();

        try {
            InputStream in = openFileInput(AREAS_DATA_FILE_NAME);
            JsonReader reader = new JsonReader();
            mAreasArrayList = reader.readJsonStream(in);
        } catch (IOException e) {
            Log.v("Loading Areas Failed: " , e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        try {
            OutputStream out = openFileOutput(AREAS_DATA_FILE_NAME, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter();
            writer.writeJsonStream(out, mAreasArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.thisActivity=this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_main);


        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mFacebookAccounts = Arrays.asList(getResources().getStringArray(R.array.fb_accounts));

        //adds a dummy area and Attraction
        setDummyAreaNdAttraction();

        if (!Permissions.checkInternetPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.INTERNET)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar(PERMISSION_REQUEST_INTERNET);
            } else {
                // Otherwise request permission from user
                if (savedInstanceState == null) {
                    requestInternetPermission();
                }
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            onInternetPermissionGranted();
        }

        populateDummyNewsFeedItems();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_newsfeed_cardList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        newsFeedAdapter = new NewsFeedAdapter(newsFeedItems);
        recyclerView.setAdapter(newsFeedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(this, recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Intent intent = DrawerSelector.onItemSelected(thisActivity, Constants.NEWS_FEED_DETAILS_ACTIVITY);
//                intent.putExtra("newsFeedID", position+1);
//
//                if (intent != null) {
//                    startActivity(intent);
//                }

                clickNewsFeed(position);


            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        FacebookSdk.sdkInitialize(getApplicationContext());

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        AppEventsLogger.activateApp(this);
   /*    FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        FirebaseCrash.log("Activity created");*/
    }

        //dummy data for the global news feed
    public void populateDummyNewsFeedItems(){

        newsFeedItems.clear();

        List<DBNewsFeed> newsfeeds = AppicationCore.getAllNewsFeed();
        if(newsfeeds!=null && newsfeeds.size()>0){

            for(int i=0; i<newsfeeds.size(); i++){
                Bitmap bm = bitmapFromByte(newsfeeds.get(i).getPicture());
                newsFeedItems.add(new NewsFeedItem(bm, newsfeeds.get(i).getMessage()));
                bm=null;
            }
        }
        else{
            newsFeedItems.add(new NewsFeedItem(null, "Inicia sesion en Facebook! \n"));
        }

    }

    public void setDummyAreaNdAttraction() {

        Area a1 = new Area(1, "Leonel Plácido", new LatLng(18.467425, -69.915474), 1);
        mAreasArrayList.add(a1);

        LatLng myTestLatLong = new LatLng(19.791893, -70.681265);
        Attraction a2 = new Attraction(1, "Leonel Plácido", myTestLatLong, "Atlántico Futbol Club Es un equipo de fútbol Profesional con sede en Puerto Plata, República Dominicana. Fue Fundado en el año 2015 y en la actualidad el equipo participa en la Liga Dominicana de Fútbol.", "El señor Ruben Garcia decidió fundar el club en el 2014 dándole una perspectiva de fútbol a los puertoplateños ya que es un pueblo donde el deporte no es muy popular. Parte de los dirigentes del equipo son Arturo Heinsen gerente del equipo, Segundo Polanco, Fernando Ortega Zeller y su hermano Gustavo Eduardo Zeller lo cual han invertido para que este equipo crezca y sea de los mejores en la LDF Banco Popular.",
                ContextCompat.getDrawable(this, R.drawable.puerto_atlantico),
                1, "8:00am - 10:00pm", "Estadio Leonel Plácido, Av. Luis Ginebra, Puerto Plata 57000", "Garrincha F.C nosotros nunca ponemos excusas.");
        mAttractionsArrayList.add(a2);
    }


    /*********************************************************************************************
     * Permission Requests
     *********************************************************************************************/

    // Request the internet permission from the user
    private void requestInternetPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
    }

    // Request the fine location permission from the user
    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
    }

    /*********************************************************************************************
     * Permissions Request result callback
     *********************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onInternetPermissionGranted();
                }
                break;
            case PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onFineLocationPermissionGranted();
                }
                break;
        }
    }

    /*********************************************************************************************
     * onPermissions granted methods
     *********************************************************************************************/
    // Run when fine location permission has been granted
    private void onInternetPermissionGranted() {


        // Check fine location permission has been granted
        if (!Permissions.checkFineLocationPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar(PERMISSION_REQUEST_FINE_LOCATION);
            } else {
                requestFineLocationPermission();
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            onFineLocationPermissionGranted();
        }

    }

    //Run when fine location permission has been granted
    private void onFineLocationPermissionGranted() {
        UtilityService.requestLocation(this);
    }

    /**
     * Show a permission explanation snackbar
     */
    private void showPermissionSnackbar(final int permission) {
        Snackbar.make(
                findViewById(R.id.main_content), R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_explanation_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (permission) {
                            case PERMISSION_REQUEST_INTERNET:
                                requestInternetPermission();
                                break;
                            case PERMISSION_REQUEST_FINE_LOCATION:
                                requestFineLocationPermission();
                        }
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feeds, menu);
        refreshButton = menu.findItem(R.id.action_reload);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_favorites:
                intent = new Intent(this, ActivityFavoriteNFollow.class);
                startActivity(intent);
                return true;
            case R.id.action_reload:
                progressBar.setVisibility(View.VISIBLE);
                new RefreshNewsFeed().execute();
                newsFeedAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static List<Geofence> getGeofenceList() {

        List<Geofence> geofenceList = new ArrayList<>();

        for (Area area : mAreasArrayList) {

            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(area.getGeo().latitude, area.getGeo().longitude, TRIGGER_RADIUS)
                    .setRequestId(String.format("%d", area.getId()))
                    .setTransitionTypes(TRIGGER_TRANSITION)
                    .setExpirationDuration(EXPIRATION_DURATION)
                    .build());
            Log.i("Geofence List", String.format("Added area %d - ", area.getId()) + area.getName());
        }
        return geofenceList;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_main) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {

                startActivity(intent);
              //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public Bitmap bitmapFromByte(byte[] b){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return  BitmapFactory.decodeByteArray(b , 0, b .length, options);
    }

    private class RefreshNewsFeed extends AsyncTask<Void, Integer, Integer> {



        public RefreshNewsFeed() {
            refreshButton.setVisible(false);
            refreshStatus=true;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(thisActivity, "Actualizando...",
                    Toast.LENGTH_SHORT).show();
            progressBar.setMax(100);
            AppicationCore.resetNewsFeedTable();
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            FacebookClass fb = new FacebookClass();
            for(int i=0; i<mFacebookAccounts.size(); i++){
                publishProgress(i*(100/mFacebookAccounts.size()));
                fb.getPost(mFacebookAccounts.get(i),2);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            populateDummyNewsFeedItems();
            newsFeedAdapter.notifyDataSetChanged();
            Toast.makeText(thisActivity, "Las noticias se han actualizado !",
                    Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            refreshButton.setVisible(true);
            refreshStatus=false;



        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }



    private void clickNewsFeed(int position){

        if(!refreshStatus){

            Intent intent = DrawerSelector.onItemSelected(thisActivity, Constants.NEWS_FEED_DETAILS_ACTIVITY);
            intent.putExtra("newsFeedID", position);

            if (intent != null) {
                startActivity(intent);
            }

        }

    }
}

