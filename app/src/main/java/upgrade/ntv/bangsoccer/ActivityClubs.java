package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Schedule.Players;
import upgrade.ntv.bangsoccer.Schedule.Team;

public class ActivityClubs extends AppCompatActivity implements CollapsingToolbarLayout.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener,
        FragmentPlayers.OnListFragmentInteractionListener, FragmentHistory.OnListFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout drawer;
    private CollapsingToolbarLayout collapsingToolbar;
    private static SlidingUpPanelLayout slidingUpPanelLayout;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private int teamid;
    private Toolbar toolbar;
    private ViewPager mViewPager;

    private Activity thisActivity;

    private TextView vPlayerName ;
    private TextView vPlayerFullName ;
    private CircleImageView vPlayerAvatar ;
    private TextView vPlayerNationality ;
    private TextView vLeaderPosition ;
    private TextView vPlayerWeightNHeight ;
    private TextView vPlayerGoals ;
    private TextView vPlayerCards ;
    private TextView vPlayerDominantFoot;
    private TextView vPlayerAliasNNumber;
    private String PlayerId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);

        this.thisActivity = this;

        getTeamId();
        BindActivity();
        BindSlidingPanel();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_clubs);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //actionBar.setSelectedNavigationItem(position);
            }
        });

    }

    private void getTeamId() {
        teamid = (int) getIntent().getExtras().get("CLUBID");
    }

    private void BindActivity() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   mTitle = (TextView) findViewById(R.id.main_textview_title);
        //  mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        //    mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //toolbar team image
        ImageView img = (ImageView) findViewById(R.id.club_header_img);
        Team selectedTeam = new Team(teamid);

        if (img != null) {
            img.setImageResource(selectedTeam.getTeam_image());
        }
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
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                slidingUpPanelLayout.setAnchorPoint(0.0f);
                slidingUpPanelLayout.setPanelHeight(0);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    //      slidingUpPanelLayout.setAnchorPoint(0.0f);
                    //    slidingUpPanelLayout.setPanelHeight(0);

                    Log.i("ActivityClubs", "onPanelStateChanged " + newState.name());

                }
                if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    //    slidingUpPanelLayout.setAnchorPoint(0.0f);

                }
                Log.i("ActivityClubs", "onPanelStateChanged " + newState);

            }
        });
        //sliding view elements
        vPlayerName = (TextView) findViewById(R.id.player_detail__name);
        vPlayerFullName = (TextView) findViewById(R.id.detailss_player_fullname_text);
        vPlayerAvatar = (CircleImageView) findViewById(R.id.player_detail_avatar);
        vPlayerNationality = (TextView) findViewById(R.id.details_player__nationality_text);
        vLeaderPosition = (TextView) findViewById(R.id.detailss_player_position_text);
        vPlayerWeightNHeight = (TextView) findViewById(R.id.detailss_player_weight_height_text);
        vPlayerGoals = (TextView) findViewById(R.id.detailss_player_goals_text);
        vPlayerCards = (TextView) findViewById(R.id.detailss_player_discipline_text);
        vPlayerDominantFoot = (TextView) findViewById(R.id.detailss_player_dominant_foot);
        vPlayerAliasNNumber = (TextView) findViewById(R.id.player_detail_Alias_n_Number);
    }

    private void onClickedFragmentPlayer(final String playerid) {
        PlayerId=playerid;
        Query query = AppicationCore.mPlayersDeftailsRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot playerSnapshot) {
               String snapshotKey = playerSnapshot.child(PlayerId).getKey();

                if (playerid.equals(snapshotKey)){
                    Players playerDetails = playerSnapshot.child(PlayerId).getValue(Players.class);
                    vPlayerName.setText( playerDetails.getName());
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
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
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

        if (id == R.id.action_favorites) {
            intent = new Intent(this, ActivityFavoriteNFollow.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
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
        //fragmentPayer interaction listener sends the player id
        Log.i("ActivityClubs", "onPanelStateChanged " + " FRAGMENTCLICk! ");
        onClickedFragmentPlayer(playerid);
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

                FragmentPlayers fragmentPlayers = FragmentPlayers.newInstance(teamid);
                mPageReferenceMap.put("2", tag);
                return fragmentPlayers;


            } else {
                FragmentHistory fragmentHistory = FragmentHistory.newInstance(position + 1);
                mPageReferenceMap.put("3", tag);
                return fragmentHistory;
            }

        }


        public
        @Nullable
        Fragment getFragmentForPosition(int position) {
            String tag = makeFragmentName(mViewPager.getId(), (int) getItemId(position));
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            return fragment;
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

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
