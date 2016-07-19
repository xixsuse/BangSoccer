package upgrade.ntv.bangsoccer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;


/**
 * Created by lopez1407 on 6/25/2016.
 */

public class ActivityFacebook extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private TextView mTextDetails;
    private ProfilePictureView mProfilePicture;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback;
    private DrawerLayout drawer;
    private AccessTokenTracker accessTokenTracker;



    public ActivityFacebook(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);


        //******** Drawer config ********//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_location);

       // createDynamicTournamentMenu(navigationView);

        //*********************************//

        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        mTextDetails = (TextView)findViewById(R.id.text_details);
        mProfilePicture =  (ProfilePictureView)findViewById(R.id.image_profilePicture);

        //permissions
        loginButton.setReadPermissions("user_posts");


        // ****** Refreshing profile picture in case user logs out ****** //
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    settingProfile();
                }
            }
        };


        mCallback = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();

                settingProfile();
                getPost("LaMediaCancha");


            }

            @Override
            public void onCancel() {
                mTextDetails.setText("CANCELL");
            }

            @Override
            public void onError(FacebookException error) {
                mTextDetails.setText("ERROR: " + error.toString());
            }
        };

        //loginButton.setFragment();
        loginButton.registerCallback(mCallbackManager, mCallback);

        settingProfile();




    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



    public void getPost(String user){

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                user+"/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {


                            JSONObject json = response.getJSONObject();
                            JSONArray jarray = json.getJSONArray("data");

                            AppicationCore.resetNewsFeedTable();
                            for(int i = 0; i < 6; i++){
                                JSONObject obj = jarray.getJSONObject(i);
                                String message= obj.optString("message");
                                String imageURL= obj.optString("full_picture");
                                String date= obj.optString("created_time");
                                String story= obj.optString("story");

                                //Cleaning Date/Time
                                date = date.split("T")[0]+" ("+date.split("T")[1].substring(0,5)+")";

                                if(imageURL!=null){

                                    new DownloadImageTask(message, story, date)
                                            .execute(imageURL);
                                }


                            }


                        } catch (Exception e) {
                            mTextDetails.setText("ERROR: "+e.toString());
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "full_picture,message,created_time,story");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id != R.id.nav_location) {

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



    /**
     * Download a image and populate a  ImageView specified
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String msg;
        String story;
        String date;


        public DownloadImageTask(String msg, String story, String date) {
            this.msg = msg;
            this.story = story;
            this.date=date;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            DBNewsFeed newsFeed = new DBNewsFeed();
            newsFeed.setMessage(msg);
            newsFeed.setStory(story);
            newsFeed.setDate(date);
            newsFeed.setPicture(bitmapToByte(result));
            AppicationCore.getDbNewsFeedDao().insertInTx(newsFeed);
        }
    }


//    public void createDynamicTournamentMenu(NavigationView navigationView) {
//        final Menu menu = navigationView.getMenu();
//        final NavigationView nav = navigationView;
//        //adds all the available tourneys to the navigation Torneos Group
//        for (int tourney = 0; tourney  < AppConstant.availableTourneys.size() ; tourney ++ ){
//            final int torneyID = tourney ;
//            menu.findItem(R.id.nav_dynamic_tourney)
//                    .getSubMenu()
//                    .add(Menu.NONE, torneyID  , Menu.NONE, AppConstant.availableTourneys.get(torneyID))
//                    .setIcon(R.drawable.ic_goals_icon)
//                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            item.setChecked(true);
//                            int id = item.getItemId();
//
//                            if (id != torneyID) {
//                                nav.setCheckedItem(torneyID);
//
//                                Intent intent = DrawerSelector.onItemSelected( ActivityFacebook.this, id);
//
//                                if (intent != null) {
//
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//                                }
//                            }
//                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                            drawer.closeDrawer(GravityCompat.START);
//
//                            return false;
//                        }
//                    });
//        }
//    }


    private byte[] bitmapToByte(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    /**
     * Write a welcome message and profile picture if user is logged in
     */
    private void settingProfile(){

        Profile profile = Profile.getCurrentProfile();


        if(profile!=null) {
            mTextDetails.setTextSize(18.0f);
            mTextDetails.setTypeface(null, Typeface.BOLD);
            mTextDetails.setText("Bienvenido " + profile.getName());


            //getting User Profile Picture
            mProfilePicture.setProfileId(profile.getId());
        }

        else{

            mTextDetails.setTextSize(16.0f);
            mTextDetails.setTypeface(null, Typeface.NORMAL);
            mTextDetails.setText("Inicia sesión en Facebook");


            //getting User Profile Picture
            mProfilePicture.setProfileId(null);

        }

    }





}



