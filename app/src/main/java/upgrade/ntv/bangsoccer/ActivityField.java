package upgrade.ntv.bangsoccer;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
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

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Entities.Field;

public class ActivityField extends AppCompatActivity implements OnMapReadyCallback, OnPermissionCallback,
        CollapsingToolbarLayout.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // For log purposes
    private static final String TAG = ActivityField.class.getSimpleName();

    private static final String DB_REF_FIELDS = "Fields";
    private static final String DB_REF_FIELD_LA_MEDIA_CANCHA = "dce43f83-5eb8-4ea5-ac59-78aa60302273";
    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    //Views
    @BindView(R.id.field_image)
    ImageView mFieldImage;
    @BindView(R.id.mainText)
    TextView mMainText;
    @BindView(R.id.field_url)
    TextView mFieldURL;
    @BindView(R.id.field_phone)
    TextView mFieldPhone;
    @BindView(R.id.field_shcedule)
    TextView mFieldSchedule;
    @BindView(R.id.field_address)
    TextView mFieldAddress;
    // Firebase references
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    // Permissions
    private PermissionHelper permissionHelper;
    // Map Stuff
    private MapView mMapView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        ButterKnife.bind(this);

        permissionHelper = PermissionHelper.getInstance(this);

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

        onAddDatabaseRefListeners();
    }

    private void onAddDatabaseRefListeners() {
        mDatabaseRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Field field = dataSnapshot.getValue(Field.class);
                        if (field != null) {
                            onAddStorageRefListeners(field.getImageURL());
                            onUpdateFieldInfo(field);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getField:onCancelled", databaseError.toException());
                    }
                });
    }

    private void onAddStorageRefListeners(String image) {
        mStorageRef.child(image)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.bg_football)
                        .into(mFieldImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void onUpdateFieldInfo(Field field) {

        mMainText.setText(field.getStory());
        mFieldURL.setText(field.getURL());
        mFieldPhone.setText(field.getPhone());
        mFieldSchedule.setText(field.getSchedule());
        mFieldAddress.setText(field.getAddress());

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
        Intent intent;

        int id = v.getId();
        switch (id) {
            case R.id.mainButton1:
                permissionHelper.setForceAccepting(true).request(PERMISSION_CALL_PHONE);
                break;
            case R.id.mainButton2:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=18.491401,-69.978308"));
                startActivity(intent);
                break;
            case R.id.mainButton3:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "a_marranzini@hotmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reservación");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "body");
                startActivity(Intent.createChooser(emailIntent, "Que app deseas usar?"));
                break;
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8095307335"));
        startActivity(intent);
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        permissionHelper.setForceAccepting(false).request(PERMISSION_CALL_PHONE);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8095307335"));
        startActivity(intent);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {

    }

    @Override
    public void onNoPermissionNeeded() {

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

        int mapStyle = 1;

        mMap.setMapType(mapStyle);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(18.491401, -69.978308), 13));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_media_cancha);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(18.491401, -69.978308))
                .title("La Media Cancha")
                .icon(icon)
                .draggable(false));
    }

}
