package upgrade.ntv.bangsoccer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;

public class ActivityField extends AppCompatActivity implements OnMapReadyCallback,
        CollapsingToolbarLayout.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    // For log purposes
    private static final String TAG = ActivityField.class.getSimpleName();
    private static final String DB_REF_FIELDS = "Fields";
    private static final String DB_REF_FIELD_LA_MEDIA_CANCHA = "dce43f83-5eb8-4ea5-ac59-78aa60302273";

    // Firebase references
    public static DatabaseReference mDatabaseRef;
    public static StorageReference mStorageRef;

    // Map Stuff
    private MapView mMapView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(DB_REF_FIELDS)
                .child(DB_REF_FIELD_LA_MEDIA_CANCHA);
        mStorageRef = FirebaseStorage.getInstance().getReference()
                .child(DB_REF_FIELDS)
                .child(DB_REF_FIELD_LA_MEDIA_CANCHA);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gets the MapView from the XML layout and creates it
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMapView.getMapAsync(this);

        final ImageView testimage = (ImageView) findViewById(R.id.field_image);
        mStorageRef.child("MediaCancha.jpg")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.ic_goals_icon)
                        .into(testimage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

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
    public void onClick(View v) {
        // onBackPressed();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_about) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {
                startActivity(intent);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap == null) {
            mMap = googleMap;
            setUpMap();
        }
    }

    private void setUpMap() {

    }
}
