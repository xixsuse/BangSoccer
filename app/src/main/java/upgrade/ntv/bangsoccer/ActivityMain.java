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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Adapters.NewsFeedAdapter;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Entities.Divisions;
import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.Utils.Permissions;
import upgrade.ntv.bangsoccer.Utils.Preferences;
import upgrade.ntv.bangsoccer.dao.DBFavorites;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;
import upgrade.ntv.bangsoccer.dao.DBSwitch;

import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback, NewsFeedAdapter.ClickListener,
        DivisionsAdapter.onDivisionFragmentInteractionListener {


    public static final int PERMISSION_REQUEST_INTERNET = 1;
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 2;
    public static DatabaseReference databaseReference;
    public static DatabaseReference mPlayersDeftailsRef;
    public static DatabaseReference mTeamsRef;
    public static DatabaseReference mDivisionsRef;
    public static DatabaseReference mMatchRef;

    public static DatabaseReference mDiv1StatsTableRef;
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
    private final int POST_QTY = 2; //  quantity of post per club
    private DrawerLayout drawer;
    private Activity thisActivity;
    private NewsFeedAdapter newsFeedAdapter;
    private List<NewsFeedItem> newsFeedItems = new ArrayList<>();
    private List<String> facebookAccounts;
    private boolean refreshStatus = false;  // true: when refresh newsfeeds is in progress
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwitchCompat mSwitch;
    private List<String> favoriteList;
    private List<DBNewsFeed> news;
    private Query query;
//gets the list of divisions
    public static List<Divisions> getDivisionsList() {
        return mDivisions;
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, ActivityMain.class);
        return in;
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


        //Updating NewsFeed
        updatefavoritesList();

//        if(mSwitch.isChecked() && favoriteList!=null && favoriteList.size()>0){
//            //   facebookAccounts.addAll(favoriteList);
//            newsFeedItems.clear();
//            newsFeedItems.addAll(getListFavorites());
//            updateNewsFeedUI(); //TODO chequiar esta llamada
//
//        }

        updateNewsFeedUI();

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

//        if(newsFeedItems==null || newsFeedItems.size()<1){
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.welcome, options);
//            newsFeedItems.add( new NewsFeedItem(0, bm, "Bienvenido a UpSport!","","",false));
//            bm=null;
//
//            newsFeedAdapter.notifyDataSetChanged();
//        }


        // Auto newsfeed refresh
        if( isNetworkAvailable() && AccessToken.getCurrentAccessToken() != null && AccessToken.getCurrentAccessToken().getToken().length()>2 )
            new RefreshNewsFeed(0).execute();


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              //  switchStatusChanged(isChecked);
                new switchChangeAsync(isChecked).execute();
               // new RefreshNewsFeed(0).execute();
//                AppicationCore.resetSwitchTable();
//
//                DBSwitch temp = new DBSwitch();
//                temp.setStatus(isChecked);
//                AppicationCore.getDbSwitchDao().insert(temp);

                //Updating UI
            //    updateNewsFeedUI();
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
                mDiv1StatsTableRef = databaseReference.child("Div1_Stats");
            }
        }
    }

    //dummy data for the global news feed
    public void populateDummyNewsFeedItems(){


        if(! mSwitch.isChecked()) {
            updateDB(); // Updating db to make sure newsfeed do not exceed max value
        }

        updateNewsFeedUI();


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

    private void clickNewsFeed(long position){

        if(!refreshStatus){

            Intent intent = new Intent(this, ActivityNewsDetails.class);
            //  intent.putExtra("MynewsFeedID", newsFeedItems.size() -1-position);
            intent.putExtra("MynewsFeedID", (int)position);

            FragmentNewsFeeddetails.updatingNewsfeedList();

            if (intent != null) {
                startActivity(intent);
            }

        }

        else{
            Toast.makeText(thisActivity, "Por favor espere que las noticias terminen de cargar",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Refresh NewsFeed UI based on new Posts added
     *
     */
    private void updateNewsFeedUI() {

     newsFeedItems.clear();
     if(mSwitch.isChecked()){
         newsFeedItems.addAll(getListFavorites());
     }
        else{
         newsFeedItems.addAll(getListAll());
     }

        if(newsFeedItems!=null || newsFeedItems.size()>0)
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

    private void share(int position){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "#UpSport https://facebook.com/"+newsFeedItems.get(position).postID;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void switchStatusChanged(boolean isChecked){


        if( isChecked) {
            // Filter is enabled

                updatefavoritesList();

                if(mSwitch.isChecked() && favoriteList!=null && favoriteList.size()>0){
                    newsFeedItems.clear();
                    newsFeedItems.addAll(getListFavorites());
                    updateNewsFeedUI();

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
        List<DBFavorites> favs = AppicationCore.getFavorites();
        List<DBNewsFeed> all = AppicationCore.getAllNewsFeed();

        for(int i=0; i<favs.size(); i++){
            for(int j=all.size()-1; j>-1; j--){

                DBNewsFeed temp = all.get(j);

                if(temp.getPostID().contains(favs.get(i).getFb_id())){
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

        if(news==null || news.size()<1){
            news = new ArrayList<>();
            news.addAll(AppicationCore.getAllNewsFeed());
        }

        for(int i=news.size()-1; i>-1; i--){
            Bitmap bm = bitmapFromByte(news.get(i).getPicture());
            list.add(new NewsFeedItem(news.get(i).getId(),bm, news.get(i).getMessage(),news.get(i).getUserName(), news.get(i).getPostID(), news.get(i).getLike()));
            bm=null;
        }

        return list;
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

    private class RefreshNewsFeed extends AsyncTask<Void, Integer, Integer> {


        private int count;

        public RefreshNewsFeed(int count) {
            this.count = count;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshStatus = true;

            if (facebookAccounts == null || facebookAccounts.size() < 1) {
                switchStatusChanged(mSwitch.isChecked());
            }

        }

        @Override
        protected Integer doInBackground(Void... voids) {

            FacebookClass fb = new FacebookClass();
            int newPost = fb.getPost(facebookAccounts.get(count), POST_QTY);
            return newPost;

        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result > 0) {
                news.clear();
                news.addAll(AppicationCore.getAllNewsFeed());
                updateNewsFeedUI();
            }


            mSwipeRefreshLayout.setRefreshing(false);

            count++;
            if (count < facebookAccounts.size()) {
                new RefreshNewsFeed(count).execute();
                refreshStatus = false;
            }


        }

    }

    private class FbLikesAsync extends AsyncTask<Void, Integer, Boolean> {

        int position;

        public FbLikesAsync(int position) {
            this.position = position;
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

            if (result) {
                //List<DBNewsFeed> list = AppicationCore.getAllNewsFeed();
                List<DBNewsFeed> list = news;
                DBNewsFeed item = list.get(list.size() - 1 - position);

                item.setLike(!item.getLike());
                AppicationCore.getDbNewsFeedDao().update(item);

                news.clear();
                news.addAll(AppicationCore.getAllNewsFeed());


                //Modify UI
                newsFeedItems.get(position).setLike(!newsFeedItems.get(position).like);
                newsFeedAdapter.notifyDataSetChanged();
            }

        }
    }

    private class switchChangeAsync extends AsyncTask<Void, Boolean, Boolean> {

        boolean status;
        boolean result=false;

        public switchChangeAsync(boolean status){
            this.status=status;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            if(status) {
                // Filter is enabled

                updatefavoritesList();

                if(status && favoriteList!=null && favoriteList.size()>0){
                    newsFeedItems.clear();
                    newsFeedItems.addAll(getListFavorites());
                    result=true;


                }

                else{
                   result=false;
                }
            }


            else{
                // Filter is disabled
                facebookAccounts.addAll(new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.fb_accounts))));
                newsFeedItems.clear();
                newsFeedItems.addAll(getListAll());
            }


            //Updating DB
            AppicationCore.resetSwitchTable();

            DBSwitch temp = new DBSwitch();
            temp.setStatus(status);
            AppicationCore.getDbSwitchDao().insert(temp);

            return result;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            updateNewsFeedUI();


            if(! result) {

                if(mSwitch.isChecked()){
                Toast.makeText(thisActivity, "Usted No ha seleccionado ningun Favorito",
                        Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


}