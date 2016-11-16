package upgrade.ntv.bangsoccer;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Entities.Club;
import upgrade.ntv.bangsoccer.Entities.Players;

import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;

public class ActivityClubs extends AppCompatActivity implements CollapsingToolbarLayout.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener,DivisionsAdapter.onDivisionFragmentInteractionListener ,
        FragmentPlayers.OnListFragmentInteractionListener, FragmentHistory.OnFragmentHistoryInteractionListener{

    // For log purposes
    private static final String TAG = ActivityField.class.getSimpleName();
    private static final String DB_REF_CLUBS = "Clubs";
    private static final String DB_REF_FIELD_LA_MEDIA_CANCHA = "565faa52-7d02-4907-bef7-ef5e18f90fea";

    // Firebase references
    public static DatabaseReference mDatabaseRef;
    public static StorageReference mStorageRef;
    //sliding view elements
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.player_detail__name)
    TextView vPlayerName;
    @BindView(R.id.detailss_player_fullname_text)
    TextView vPlayerFullName;
    @BindView(R.id.player_detail_avatar)
    CircleImageView vPlayerAvatar;
    @BindView(R.id.details_player__nationality_text)
    TextView vPlayerNationality;
    @BindView(R.id.detailss_player_position_text)
    TextView vLeaderPosition;
    @BindView(R.id.detailss_player_weight_height_text)
    TextView vPlayerWeightNHeight;
    @BindView(R.id.detailss_player_goals_text)
    TextView vPlayerGoals;
    @BindView(R.id.detailss_player_discipline_text)
    TextView vPlayerCards;
    @BindView(R.id.detailss_player_dominant_foot)
    TextView vPlayerDominantFoot;
    @BindView(R.id.player_detail_Alias_n_Number)
    TextView vPlayerAliasNNumber;
    private Query query;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout drawer;
    private String teamid = "";
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private Club mClub;
    private String PlayerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference()
                .child(DB_REF_CLUBS)
                .child(DB_REF_FIELD_LA_MEDIA_CANCHA);

        teamid = (String) getIntent().getExtras().get("CLUBID");

        BindActivity();
        BindSlidingPanel();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //actionBar.setSelectedNavigationItem(position);
            }
        });

    }

    private String getTeamId() {
        if (teamid != null && teamid.length() < 2) {
            teamid = (String) getIntent().getExtras().get("CLUBID");
        }
        return teamid;
    }

    private void BindActivity() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   mTitle = (TextView) findViewById(R.id.main_textview_title);
        //  mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        //    mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //toolbar team image
        final ImageView img = (ImageView) findViewById(R.id.club_header_img);
        //final TextView txt = (TextView) findViewById(R.id.clubs_header_team_name);

        String currentKey = getTeamId();
        this.query = ActivityMain.mTeamsRef.child(currentKey);

        this.query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mClub = dataSnapshot.getValue(Club.class);
                mClub.setFirebasekey(dataSnapshot.getKey());

                if (img != null && mClub != null) {
                    Picasso.with(getApplication()).
                            load(mClub.getTeam_image()).
                            placeholder(R.drawable.ic_upgraden).
                            into(img);

                   // txt.setText(mClub.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

    }

    private void BindSlidingPanel() {
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                slidingUpPanelLayout.setAnchorPoint(0.0f);
                slidingUpPanelLayout.setPanelHeight(0);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });

    }

    private void onClickedFragmentPlayer(final String playerid) {
        PlayerId = playerid;
        Query query = ActivityMain.mPlayersDeftailsRef.child(playerid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot playerSnapshot) {
                Players playerDetails = playerSnapshot.getValue(Players.class);
                vPlayerName.setText(playerDetails.getName());
                vPlayerFullName.setText(playerDetails.getName());
                vPlayerAvatar.setImageResource(playerDetails.getAvatar());
                vPlayerNationality.setText(playerDetails.getNationality());
                vLeaderPosition.setText(playerDetails.getPosition());

                vPlayerWeightNHeight.setText(playerDetails.getWeightNHeight());
                vPlayerGoals.setText(playerDetails.getGoals());
                vPlayerCards.setText(playerDetails.getCards());
                vPlayerDominantFoot.setText(playerDetails.getDominant_foot());
                vPlayerAliasNNumber.setText(playerDetails.getAliasNNumber());
                vPlayerAvatar.setImageResource(R.drawable.ic_player_name_icon2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.dragView).setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = DrawerSelector.onItemSelected(this, id);

        if (intent != null) {
            startActivity(intent);
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

    }


    @Override
    public void onListFragmentInteraction() {


    }

    @Override
    public void onListFragmentInteraction(String playerid) {
        onClickedFragmentPlayer(playerid);
    }

    @Override
    public void onDivisionSelected(String node) {

    }

    @Override
    public void onDivisionUnselected(String node) {

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Map<String, String> mPageReferenceMap = new HashMap<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public String getFragment(String key) {

            return mPageReferenceMap.get(key);
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            String tag = makeFragmentName(mViewPager.getId(), (int) getItemId(position));
            if (position == 0) {

                FragmentTeamInfo fragmentMatches = FragmentTeamInfo.newInstance(position + 1);
                mPageReferenceMap.put("1", tag);
                return fragmentMatches;

            } else if (position == 1) {

                FragmentPlayers fragmentPlayers = FragmentPlayers.newInstance(getTeamId());
                mPageReferenceMap.put("2", tag);
                return fragmentPlayers;


            } else {
                FragmentHistory fragmentHistory = FragmentHistory.newInstance(getTeamId());
                mPageReferenceMap.put("3", tag);
                return fragmentHistory;
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            // Show 10 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "INFO";
                case 1:
                    return "JUGADORES";
                case 2:
                    return "Historial".toUpperCase(l);
            }
            return null;
        }
    }
}
