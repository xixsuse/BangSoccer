package upgrade.ntv.bangsoccer;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Adapters.NewsFeedAdapter;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Attraction.Area;
import upgrade.ntv.bangsoccer.Attraction.Attraction;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.Entities.Divisions;
import upgrade.ntv.bangsoccer.Utils.JsonReader;
import upgrade.ntv.bangsoccer.Utils.JsonWriter;
import upgrade.ntv.bangsoccer.Utils.Permissions;
import upgrade.ntv.bangsoccer.Utils.Preferences;
import upgrade.ntv.bangsoccer.dao.DBFavorites;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;
import upgrade.ntv.bangsoccer.dao.DBSwitch;
import upgrade.ntv.bangsoccer.service.UtilityService;

import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback, NewsFeedAdapter.ClickListener,
        DivisionsAdapter.onDivisionFragmentInteractionListener {


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

    private List<String> facebookAccounts;
    private boolean refreshStatus = false;  // true: when refresh newsfeeds is in progress
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final int POST_QTY=2; //  quantity of post per club
    private SwitchCompat mSwitch;
    private List<String> favoriteList;
    private List<DBNewsFeed> news;


    public static DatabaseReference databaseReference;
    public static DatabaseReference mPlayersDeftailsRef;
    public static DatabaseReference mTeamsRef;
    public static DatabaseReference mDivisionsRef;
    public static DatabaseReference mMatchRef;
    public static DatabaseReference  mLeadersofTheDayDiv1Ref ;
    public static DatabaseReference  mMatchesOfTheDayDiv1Ref ;
    public static DatabaseReference  mMatchesOfTheDayDiv2Ref ;
    public static DatabaseReference  mMatchesOfTheDayDiv3Ref ;
    public static StorageReference storageReference;
    public static StorageReference mPrimeraRef;
    public static StorageReference mSegundaRef;
    public static StorageReference mTerceraRef;
    public static StorageReference mCuartaRef;

    public static List<Divisions> mDivisions = new ArrayList<>();

    private Query query;
//gets the list of divisions
    public static List<Divisions> getDivisionsList() {
        return mDivisions;
    }

    @Override
    public void itemClicked(View view, int position) {

        switch (view.getId()){

            case R.id.newsfeed_likes_button:

                if(isNetworkAvailable())
                    new FbLikesAsync(position).execute();
                else{
                    Toast.makeText(thisActivity, "Revise su conexion a internet",
                            Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.newsfeed_share:
                share(position);

                break;

            default:
               // clickNewsFeed(newsFeedItems.get(position).id);
                clickNewsFeed(position);
                break;
        }
    }

    @Override
    public void onDivisionSelected(String node) {

    }

    @Override
    public void onDivisionUnselected(String node) {

    }

    //firebase division listener
    private class DivisionEvenetListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Divisions firebaseRequest = dataSnapshot.getValue(Divisions.class);
            firebaseRequest.setFirebasekey(dataSnapshot.getKey());
            getDivisionsList().add(firebaseRequest);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //ensures a value for the shared pref.
        boolean result = false;
        for (Divisions div : mDivisions) {
            if(Preferences.getPreferredDivisions(this, div.getNode())){
               result = true;
            }
        }
        if (!result){
            Preferences.setPreferredDivisions(this, "Div1_Calendar");
        }

        try {
            InputStream in = openFileInput(AREAS_DATA_FILE_NAME);
            JsonReader reader = new JsonReader();
            mAreasArrayList = reader.readJsonStream(in);
        } catch (IOException e) {
            Log.v("Loading Areas Failed: ", e.getMessage());
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
        this.thisActivity = this;

        initFirebaseRefs();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            SignedInActivity.createIntent(this);
        }
        //avoid duplication if its  been created
        if (mDivisions.size() < 2) {
            this.query = ActivityMain.mDivisionsRef;
            this.query.addChildEventListener(new DivisionEvenetListener());
        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(! isNetworkAvailable()){
                    Toast.makeText(thisActivity, "Revise su conexion a internet",
                            Toast.LENGTH_LONG).show();

                    mSwipeRefreshLayout.setRefreshing(false);

                }

                else if(AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().getToken().length()<2 ){

                    Toast.makeText(thisActivity, "Inicie sesion en Facebook!",
                            Toast.LENGTH_LONG).show();

                    mSwipeRefreshLayout.setRefreshing(false);
                }

                else if(!refreshStatus)
                    new RefreshNewsFeed(0).execute();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setSelected(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_newsfeed_cardList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        newsFeedAdapter = new NewsFeedAdapter(newsFeedItems, this );
        recyclerView.setAdapter(newsFeedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        newsFeedAdapter.setClickListener(this);

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        AppEventsLogger.activateApp(this);
   /*    FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        FirebaseCrash.log("Activity created");*/

        mSwitch = (SwitchCompat) findViewById(R.id.filter);

        /////////////////// Filter Switch ///////////////////
        List<DBSwitch> temp = AppicationCore.getSwitchStatus();
        if(temp!= null && temp.size()>0){
            mSwitch.setChecked(temp.get(0).getStatus());
        }
        else{
            mSwitch.setChecked( false);
        }

        /////////////////// Facebook Newsfeed Variables ///////////////////
        facebookAccounts = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.fb_accounts)));
        favoriteList = new ArrayList<>();

        news = AppicationCore.getAllNewsFeed();
        if(news==null)
            news= new ArrayList<>();

        switchStatusChanged(mSwitch.isChecked());

        populateDummyNewsFeedItems();


        // Auto newsfeed refresh
        if( isNetworkAvailable() && AccessToken.getCurrentAccessToken() != null && AccessToken.getCurrentAccessToken().getToken().length()>2 )
            new RefreshNewsFeed(0).execute();


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switchStatusChanged(isChecked);
               // new RefreshNewsFeed(0).execute();
                AppicationCore.resetSwitchTable();

                DBSwitch temp = new DBSwitch();
                temp.setStatus(isChecked);
                AppicationCore.getDbSwitchDao().insert(temp);

                //Updating UI
                updateNewsFeedUI(-1);
            }
        });



    }

    public void initFirebaseRefs(){
        if (databaseReference == null) {
            try {
                if (!FirebaseApp.getApps(this).isEmpty()) {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                }
            } catch (Exception e) {
                //  FirebaseCrash.log("firebase Crash reports  failed to initialize");
                Log.i("firebase persistance: ", e.getMessage());
            } finally {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://bangsoccer-1382.appspot.com/MediaCancha/");
                mPrimeraRef = storageReference.child("primera");
                mSegundaRef = storageReference.child("segunda");
                mTerceraRef = storageReference.child("tercera");
                mCuartaRef = storageReference.child("cuarta");
                databaseReference = FirebaseDatabase.getInstance().getReference();
                mPlayersDeftailsRef = databaseReference.child("Players");
                mTeamsRef = databaseReference.child("Clubs");
                mDivisionsRef = databaseReference.child("Divisions");
                mMatchesOfTheDayDiv1Ref = databaseReference.child("Div1_Calendar");
                mMatchesOfTheDayDiv2Ref = databaseReference.child("Div2_Calendar");
                mMatchesOfTheDayDiv3Ref = databaseReference.child("Div3_Calendar");
                mLeadersofTheDayDiv1Ref = databaseReference.child("Div1_Leader");
            }
        }
    }

    //dummy data for the global news feed
    public void populateDummyNewsFeedItems(){


        if(! mSwitch.isChecked()) {
            updateDB(); // Updating db to make sure newsfeed do not exceed max value
        }

        updateNewsFeedUI(-1);


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

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, ActivityMain.class);
        return in;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;


        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_favorites:
                intent = new Intent(this, ActivityFavoriteNFollow.class);
                startActivity(intent);
                break;
            case R.id.action_divisiones:
                onDvisionChoosertDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays Divisions Dialog Fragment
     */
    private void onDvisionChoosertDialog() {
        Log.i("main", "Calling Create Client Dialog Fragment");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment prev = (DialogFragment) getFragmentManager().findFragmentByTag(FRAGMENT_CHOOSE_DIVISION);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new DivisionChooserFragment();
        newFragment.show(ft, FRAGMENT_CHOOSE_DIVISION);
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

    public Bitmap bitmapFromByte(byte[] b) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return BitmapFactory.decodeByteArray(b, 0, b.length, options);
    }


    private class RefreshNewsFeed extends AsyncTask<Void, Integer, Integer> {


        private int count;

        public RefreshNewsFeed(int count) {
            this.count=count;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshStatus=true;

            if(facebookAccounts==null || facebookAccounts.size()<1){
                switchStatusChanged(mSwitch.isChecked());
            }

        }

        @Override
        protected Integer doInBackground(Void... voids) {

            FacebookClass fb = new FacebookClass();
            int newPost=fb.getPost(facebookAccounts.get(count),POST_QTY);
            return newPost;

        }



        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            refreshStatus=false;

            if(result>0) {

                updateNewsFeedUI(result);
            }


            mSwipeRefreshLayout.setRefreshing(false);

            count++;
            if(count< facebookAccounts.size()){
                new RefreshNewsFeed(count).execute();
            }


        }

    }


    private void clickNewsFeed(long position){

        if(!refreshStatus){

            Intent intent = DrawerSelector.onItemSelected(thisActivity, Constants.NEWS_FEED_DETAILS_ACTIVITY);
            //  intent.putExtra("MynewsFeedID", newsFeedItems.size() -1-position);
            intent.putExtra("MynewsFeedID", (int)position);

            FragmentNewsFeeddetails.updatingNewsfeedList();

            if (intent != null) {
                startActivity(intent);
            }

        }

    }


    /**
     * Refresh NewsFeed UI based on new Posts added
     * @param newPost
     */
    private void updateNewsFeedUI(int newPost) {

     newsFeedItems.clear();
     if(mSwitch.isChecked()){
         newsFeedItems.addAll(getListFavorites());
     }
        else{
         newsFeedItems.addAll(getListAll());
     }

        newsFeedAdapter.notifyDataSetChanged();


    }


    /**
     * Refresh NewsFeed DB based on new Posts added

     */
    private void updateDB(){

       // List<DBNewsFeed> list = AppicationCore.getAllNewsFeed();
        List<DBNewsFeed> list = news;
        List<DBNewsFeed> newList = new ArrayList<>();
        int size =  facebookAccounts.size() * POST_QTY;
        int dif = (list.size()>0) ? (list.size() - size) : 0;

        if(dif>0) {

            AppicationCore.resetNewsFeedTable();

            for (int i = dif; i < list.size(); i++) {
                newList.add(list.get(i));
            }
            AppicationCore.getDbNewsFeedDao().insertInTx(newList);
            news.clear();
            news.addAll( AppicationCore.getAllNewsFeed());
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void updatefavoritesList(){

        List<DBFavorites> favs = AppicationCore.getFavorites();
        favoriteList.clear();

        if(favs!=null && favs.size()>0){

            for(int i=0; i<favs.size(); i++){
                favoriteList.add(favs.get(i).getFb_id());
            }
        }

    }


    private boolean updateLikes(int position){

       // List<DBNewsFeed> newsFeeds = AppicationCore.getAllNewsFeed();
        List<DBNewsFeed> newsFeeds = news;
        DBNewsFeed newsFeed = newsFeeds.get(newsFeeds.size() -1 - position);

        FacebookClass fb = new FacebookClass();
        String id = newsFeed.getPostID();
        boolean result=false;

        //Confirming is the same post
        if( newsFeedItems.get(position).postID.equals(id)){

            if(newsFeed.getLike()){

                result=fb.disLikePost(id);

            }else{

                result=fb.likePost(id);
            }
        }

       return result;
    }




    private class FbLikesAsync extends AsyncTask<Void, Integer, Boolean> {

        int position;

        public FbLikesAsync(int position){
            this.position=position;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            return updateLikes(position);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result) {
                //List<DBNewsFeed> list = AppicationCore.getAllNewsFeed();
                List<DBNewsFeed> list = news;
                DBNewsFeed item = list.get(list.size() -1 - position);

                item.setLike(!item.getLike());
                AppicationCore.getDbNewsFeedDao().update(item);

                news.clear();
                news.addAll(AppicationCore.getAllNewsFeed());


                //Modify UI
                 newsFeedItems.get(position).setLike(! newsFeedItems.get(position).like );
                 newsFeedAdapter.notifyDataSetChanged();
            }

        }
    }



    private void share(int position){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "#UpSport https://facebook.com/"+newsFeedItems.get(position).postID;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void switchStatusChanged(boolean isChecked){

        //facebookAccounts.clear();

        if( isChecked) {
            // Filter is enabled

                updatefavoritesList();

                if(favoriteList!=null && favoriteList.size()>0){
                    //   facebookAccounts.addAll(favoriteList);
                    newsFeedItems.clear();
                    newsFeedItems.addAll(getListFavorites());
                    updateNewsFeedUI(0); //TODO chequiar esta llamada

                }

                else{
                    Toast.makeText(thisActivity, "Usted No ha seleccionado ningun Favorito",
                            Toast.LENGTH_SHORT).show();
                }
        }


        else{
            // Filter is disabled
            facebookAccounts.addAll(new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.fb_accounts))));
        }
    }



    private List<NewsFeedItem> getListFavorites(){

        List<NewsFeedItem> list = new ArrayList<>();

                for(int i=0; i< news.size(); i++){

            for(int j=0; j<favoriteList.size(); j++){

                DBNewsFeed temp = news.get(i);

                if(temp.getPostID().contains(favoriteList.get(j))){
                    Bitmap bm = bitmapFromByte(temp.getPicture());
                    list.add( new NewsFeedItem(temp.getId(),bm, temp.getMessage(),temp.getUserName(),temp.getPostID(), temp.getLike()));
                    bm=null;
                }
            }
        }

        return list;
    }



    private List<NewsFeedItem> getListAll(){

        List<NewsFeedItem> list = new ArrayList<>();

        for(int i=news.size()-1; i>-1; i--){
            Bitmap bm = bitmapFromByte(news.get(i).getPicture());
            list.add(new NewsFeedItem(news.get(i).getId(),bm, news.get(i).getMessage(),news.get(i).getUserName(), news.get(i).getPostID(), news.get(i).getLike()));
            bm=null;
        }

        return list;
    }
}