package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Utils.Tools;
import upgrade.ntv.bangsoccer.service.UtilityService;

public class ActivityTour extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        FragmentAttractionsList.OnFragmentInteractionListener, FragmentMap.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,DivisionChooserFragment.onDivisionFragmentInteractionListener {


    // for log porpuses
    private final String TAG = ActivityTour.class.getSimpleName();
    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;
    private DrawerLayout drawer;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    // The {@link ViewPager} that will host the section contents.
    private ViewPager mViewPager;
    private Activity thisActivity;


    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        //   startLoading(Constants.LOADING_SUCCESS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilityService.requestLocation(this);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_tour_activity);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (Tools.checkPlayServices(this)) {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
        } else finish();

        if (!Tools.isGoogleMapsInstalled(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Install Google Maps");
            builder.setCancelable(false);
            builder.setPositiveButton("Install", getGoogleMapsListener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_location);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
              /*  actionBar.setSelectedNavigationItem(position);*/
            }
        });

    }

    /**
     * Show a basic debug dialog to provide more info on the built-in debug
     * options.
     */
    private void showDebugDialog(int titleResId, int bodyResId) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setMessage(bodyResId)
                .setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    /* public boolean startLoading(int id){

         //noinspection SimplifiableIfStatement
         if (id == Constants.LOADING_SUCCESS) {

             new Handler().post(new Runnable() {
                 @Override
                 public void run() {
                     mElasticDownloadView.startIntro();
                 }
             });

             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     mElasticDownloadView.success();
                 }
             }, 2 * ProgressDownloadView.ANIMATION_DURATION_BASE);

             return true;
         } else if (id == Constants.LOADING_FAILURE) {

             new Handler().post(new Runnable() {
                 @Override
                 public void run() {
                     mElasticDownloadView.startIntro();
                 }
             });

             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     mElasticDownloadView.setProgress(45);
                 }
             }, 2 * ProgressDownloadView.ANIMATION_DURATION_BASE);

             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     mElasticDownloadView.fail();
                 }
             }, 3 * ProgressDownloadView.ANIMATION_DURATION_BASE);

             return true;
         }

         return false;
     }
 */

    /**
     * Navigates to Google play to download google maps
     */
    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                startActivity(intent);
                //Finish the activity so they can't circumvent the check
                finish();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Builds Google Api Client to {@link #mGoogleApiClient).
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * This method is called on a successful connection.
     * Retrieves last known location of the device and calls
     *
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // TODO: 11/6/2015 do on connected stuff
        Log.v("onConnected", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO: 11/6/2015 do stuff
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Snackbar.make(this.getCurrentFocus(), "Doh, could not connect to Google Api Client", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGoogleApiClient.connect();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_location) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {

                startActivity(intent);
                //              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //   overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            //   finish();
        }
    }

    @Override
    public void onDivisionSelected(String node) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return FragmentAttractionsList.newInstance();
            } else {
                return FragmentMap.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Canchas";
            } else {
                return "Mapa";
            }
        }
    }
}
