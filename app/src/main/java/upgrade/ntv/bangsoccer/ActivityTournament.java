package upgrade.ntv.bangsoccer;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luseen.datelibrary.DateConverter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Entities.Day;
import upgrade.ntv.bangsoccer.Entities.Divisions;
import upgrade.ntv.bangsoccer.Entities.LeadersIndex;
import upgrade.ntv.bangsoccer.Entities.Match;
import upgrade.ntv.bangsoccer.Entities.Players;
import upgrade.ntv.bangsoccer.Utils.Preferences;

import static upgrade.ntv.bangsoccer.ActivityMain.mDivisions;
import static upgrade.ntv.bangsoccer.ActivityMain.mLeadersofTheDayDiv1Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv1Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv2Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv3Ref;
import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;


public class ActivityTournament extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentNewsFeed.OnListFragmentInteractionListener, FragmentLeaders.OnListFragmentInteractionListener,
        FragmentTourneyStats.OnListFragmentInteractionListener, DivisionsAdapter.onDivisionFragmentInteractionListener {

    private static final String FRAGMENT_TYPE = "type";
    private static DrawerLayout drawer;
    //bottom bar views
    @BindView(R.id.matches_calendar)
    ImageView matchButton;
    @BindView(R.id.matches_stats)
    ImageView statsButton;
    @BindView(R.id.matches_leaders)
    ImageView leadersButton;

    //sliding view elements
    @BindView(R.id.sliding_layout_tourney)
    SlidingUpPanelLayout slidingUpPanelLayoutT;
    @BindView(R.id.player_detail__name_tourney)
    TextView vPlayerNameT;
    @BindView(R.id.detailss_player_fullname_text_tourney)
    TextView vPlayerFullNameT;
    @BindView(R.id.player_detail_avatar_tourney)
    CircleImageView vPlayerAvatarT;
    @BindView(R.id.details_player__nationality_text_tourney)
    TextView vPlayerNationalityT;
    @BindView(R.id.detailss_player_position_text_tourney)
    TextView vLeaderPositionT;
    @BindView(R.id.detailss_player_weight_height_text_tourney)
    TextView vPlayerWeightNHeightT;
    @BindView(R.id.detailss_player_goals_text_tourney)
    TextView vPlayerGoalsT;
    @BindView(R.id.detailss_player_discipline_text_tourney)
    TextView vPlayerCardsT;
    @BindView(R.id.detailss_player_dominant_foot_tourney)
    TextView vPlayerDominantFootT;
    @BindView(R.id.player_detail_Alias_n_Number_tourney)
    TextView vPlayerAliasNNumberT;
    String PlayerId;

    private int mLastSelectedItem;
    private FragmentViewPagerContainer mFragmentContainer;

    //private static Map<Integer, String> mBottomBarButtonID = new HashMap<>();

    @Optional
    @OnClick({R.id.matches_calendar, R.id.matches_stats, R.id.matches_leaders})
    public void onBottomBarClickListner(View view) {
        final int id = view.getId();

        if (getLastSelectedItem() != view.getId()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            mFragmentContainer = FragmentViewPagerContainer.newInstance(id);
            ft.replace(R.id.fragment_holder, mFragmentContainer);
            ft.commit();
            setLastSpinnerSelectedItem(id);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the user's current gamez state
        outState.putInt(FRAGMENT_TYPE, getLastSelectedItem());
        super.onSaveInstanceState(outState);
    }

    public int getLastSelectedItem() {
        return mLastSelectedItem;
    }

    public void setLastSpinnerSelectedItem(int mLastSpinnerSelectedItem) {
        this.mLastSelectedItem = mLastSpinnerSelectedItem;
    }

    private void BindActivity() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dynamic_tourney);
    }




    private void BindSlidingPanel() {
        slidingUpPanelLayoutT.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                slidingUpPanelLayoutT.setAnchorPoint(0.0f);
                slidingUpPanelLayoutT.setPanelHeight(0);
                slidingUpPanelLayoutT.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

    }

    private void onClickedFragmentLeaders(final String playerid) {
        PlayerId = playerid;
        Query query = ActivityMain.mPlayersDeftailsRef.child(playerid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot playerSnapshot) {
                Players playerDetails = playerSnapshot.getValue(Players.class);
                vPlayerNameT.setText(playerDetails.getName());
                vPlayerFullNameT.setText(playerDetails.getName());
                vPlayerAvatarT.setImageResource(playerDetails.getAvatar());
                vPlayerNationalityT.setText(playerDetails.getNationality());
                vLeaderPositionT.setText(playerDetails.getPosition());

                vPlayerWeightNHeightT.setText(playerDetails.getWeightNHeight());
                vPlayerGoalsT.setText(playerDetails.getGoals());
                vPlayerCardsT.setText(playerDetails.getCards());
                vPlayerDominantFootT.setText(playerDetails.getDominant_foot());
                vPlayerAliasNNumberT.setText(playerDetails.getAliasNNumber());
                vPlayerAvatarT.setImageResource(R.drawable.ic_player_name_icon2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.dragView).setVisibility(View.VISIBLE);
        slidingUpPanelLayoutT.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

    }


    public String makePagerFragmentTag(int id) {
        String i = "string";
        switch (id) {
            case R.id.matches_calendar:
                i = "match";
                break;
            case R.id.matches_stats:
                i = "stats";
                break;
            case R.id.matches_leaders:
                i = "leader";
                break;
        }
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney);
        ButterKnife.bind(this);
        //fragment type id
        int id = R.id.matches_calendar;

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            id = savedInstanceState.getInt(FRAGMENT_TYPE, R.id.matches_calendar);
            setLastSpinnerSelectedItem(id);
        }
        mFragmentContainer = FragmentViewPagerContainer.newInstance(id);



        BindActivity();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, mFragmentContainer, makePagerFragmentTag(id));
        ft.commit();
        setLastSpinnerSelectedItem(id);


        BindSlidingPanel();
    }

    /*********************
     * Activity Overrides
     **********************/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slidingUpPanelLayoutT != null &&
                (slidingUpPanelLayoutT.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slidingUpPanelLayoutT.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayoutT.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    /***
     * Displays Divisions Dialog Fragment
     ***/
    private void onDvisionChoosertDialog() {
        Log.i("main", "Calling Create Client Dialog Fragment");

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
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
        if (id != R.id.nav_dynamic_tourney) {
            Intent intent = DrawerSelector.onItemSelected(this, id);
            if (intent != null) {
                startActivity(intent);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*********************
     * Interface implementation
     **********************/

    @Override
    public void onListFragmentInteraction(String PlayerKey) {
        onClickedFragmentLeaders(PlayerKey);
    }

    @Override
    public void onDivisionSelected(String node) {
        //add matches to the calendar viewpager when a division is selected
        mFragmentContainer.addSelectedDivision(node);
    }

    @Override
    public void onDivisionUnselected(String divisionKey) {
        //removes the objects from the viewpager when a division is deselected
        mFragmentContainer.removeUnselectedDivision(divisionKey);
    }

    @Override
    public void onListFragmentInteraction() {

    }


    /****************************************************************************************************************************************************************/
    /*********************************************************
     * FRAGMENT VIEW PAGER CONTAINER
     ***********************************************************************/

    public static class FragmentViewPagerContainer extends Fragment {

        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(R.id.tabs)
        TabLayout tabLayout;
        @BindView(R.id.tourney_recycleview)
        ViewPager mViewPager;

        private FragmentViewPagerContainer.TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
        private List<Day> mMatchesOfTheDiv = new ArrayList<>();
        private List<Players> mPlayersOfTheDiv = new ArrayList<>();
        private HashMap<String, List<LeadersIndex>> leaderMaps = new HashMap<>();
        //TODO: catch map <string, List<index>> handle the goals/cards section
        private int dateInCurrentWeek = -1;
        private Unbinder unbinder;
        private int fragmentId;
        private GenericTypeIndicator<Map<String, Match>> t =
                new GenericTypeIndicator<Map<String, Match>>() {
                };
        private Date date = new Date();

        //empty cosntructor
        public FragmentViewPagerContainer() { //holds the tourney calendar
        }

        public static FragmentViewPagerContainer newInstance(int id) {
            FragmentViewPagerContainer frag = new FragmentViewPagerContainer();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);

            return frag;
        }

        /*********************
         * Fragment  Overrides
         **********************/

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_content_recivleview, container, false);
            unbinder = ButterKnife.bind(this, root);

            Bundle b = getArguments();
            if (null != b) { //Null Checking
                fragmentId = b.getInt("id", R.id.matches_calendar);
            }

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(mViewPager);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            if (toolbar != null) {
                switch (fragmentId) {

                    case R.id.matches_stats:
                        toolbar.setTitle("Tabla de Posiciones");
                        break;
                    case R.id.matches_leaders:
                        toolbar.setTitle("Lideres");
                        break;

                    case R.id.matches_calendar:
                        toolbar.setTitle("Calendario");
                        break;

                    default:
                        toolbar.setTitle("Calendario");
                        break;
                }

            }

            mTourneyCalendarPagerAdapter = new FragmentViewPagerContainer.TourneyCalendarPagerAdapter(getChildFragmentManager(), fragmentId);

            mViewPager.setAdapter(mTourneyCalendarPagerAdapter);
            //sets the viewpager to the current week

            if (ViewCompat.isLaidOut(tabLayout)) {
                tabLayout.setupWithViewPager(mViewPager);
            } else {
                tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        tabLayout.setupWithViewPager(mViewPager);
                        tabLayout.removeOnLayoutChangeListener(this);
                    }
                });
            }


            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {/*  actionBar.setSelectedNavigationItem(position);*/}
            });
            // mViewPager.setCurrentItem(viewPagerContainerFragment.get().getDateInCurrentWeek());

            return root;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        /*********************
         * Fragment Functions
         **********************/
        public void addSelectedDivision(String node) {

            if (fragmentId == R.id.matches_calendar) {

                mTourneyCalendarPagerAdapter.calendarDivReferenceFinder(node);
                mTourneyCalendarPagerAdapter.notifyDataSetChanged();
            }
            if (fragmentId == R.id.matches_leaders) {

                mTourneyCalendarPagerAdapter.leaderDivReferenceFinder(node);
                mTourneyCalendarPagerAdapter.notifyDataSetChanged();
            }

        }

        //
        public void removeUnselectedDivision(String divisionKey) {

            mTourneyCalendarPagerAdapter.nukeElement();
            mTourneyCalendarPagerAdapter.notifyDataSetChanged();
            // mViewPager.setAdapter(mTourneyCalendarPagerAdapter);

        }

        public void removeAll() {


            for (int i = 0; i < mTourneyCalendarPagerAdapter.getCount(); i++) {
                //mTourneyCalendarPagerAdapter.getFragmentForPosition(i);
                // getFragmentManager().beginTransaction().remove(mTourneyCalendarPagerAdapter.getFragmentForPosition(i)).commit();
                //  mTourneyCalendarPagerAdapter.notifyDataSetChanged();
            }

        }


        /****************************************************************************************************************************************************************/
        /****************************************************************************************************************************************************************/
        /*********************************************************
         * TOURNEY CALENDAR PAGER ADAPTER
         ***********************************************************************/

        public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {
            private Query queryCalendar;
            private Query queryLeader;
            private Query queryStats;

            TourneyCalendarPagerAdapter(FragmentManager fm, int id) {
                super(fm);
                switch (id) {
                    case R.id.matches_calendar:
                        if (mMatchesOfTheDiv.size() == 0) {
                            for (Divisions div : mDivisions) {
                                //checks if its saved in the shared preferences
                                if (Preferences.getPreferredDivisions(getActivity(), div.getNode())) {
                                    //get the calendar for a given node
                                    queryCalendar = calendarDivReferenceFinder(div.getNode());

                                }
                                if (queryCalendar == null) {

                                }
                            }
                        }
                        break;
                    case R.id.matches_leaders:
                        if (leaderMaps.size() == 0) {

                            for (Divisions div : mDivisions) {
                                //checks if its saved in the shared preferences
                                if (Preferences.getPreferredDivisions(getActivity(), div.getNode())) {
                                    //get the calendar for a given node
                                    queryLeader = leaderDivReferenceFinder(div.getNode().substring(0, 5) + "Leader");
                                }
                                if (queryLeader == null) {

                                }
                            }
                        }
                        break;
                    case R.id.matches_stats:
                        break;

                }
            }

            /*********************
             * Fragment Functions
             **********************/

            synchronized List<Day> matchScanner(String divisionKey) {
                List<Day> array = new ArrayList<>();
                if (mMatchesOfTheDiv.size() > 0) {

                    for (Day day : mMatchesOfTheDiv) {
                        //iterate thru the map to pull all the
                        Map<String, Match> matchMap = day.getGames();
                        for (Map.Entry<String, Match> entry :
                                matchMap.entrySet()) {
                            Match value1 = entry.getValue();

                            if (divisionKey.equals(value1.getTournamentId())) {
                                //removes the tourney from the list
                                if (!array.contains(day)) {

                                    array.add(day);
                                }
                            }
                        }

                    }
                }
                return array;
            }

            boolean removeDay(List<Day> array) {
                boolean result = false;

                for (Day day : array) {

                    mMatchesOfTheDiv.remove(day);
                    notifyDataSetChanged();
                    result = true;
                }

                return result;
            }

            boolean removeDay() {
                boolean result = false;

                try {
                    mMatchesOfTheDiv.clear();
                    notifyDataSetChanged();
                    result = true;
                } catch (Exception e) {
                    Log.v("Remove Divisions", e.getMessage());
                }


                return result;
            }

            //remove a division.
            boolean nukeElement(String divisionKey) {
                return removeDay(matchScanner(divisionKey));
            }

            boolean nukeElement() {
                return removeDay();
            }

            @Nullable
            public Fragment getFragmentForPosition(int position) {
                String tag = makeFragmentName(mViewPager.getId(), (int) getItemId(position));
                Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
                // getFragmentManager().beginTransaction().remove(fragment).commit();
                return fragment;
            }

            int getPagerCount() {
                int size = 0;
                switch (fragmentId) {

                    case R.id.matches_stats:
                        size = 1;
                        break;

                    case R.id.matches_leaders:
                        size = leaderMaps.size();
                        break;

                    case R.id.matches_calendar:
                        size = mMatchesOfTheDiv.size();
                        break;
                }
                return size;
            }

            boolean analizeDate(int position) {
                //match day converted to Date to compare against the upcoming dates and setup the tablayout
                date = DateConverter.stringToDate(mMatchesOfTheDiv.get(position).getDate(), "dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                //default day is Sunday, set to Monday
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                //holds the monday pof the week
                Date monday = calendar.getTime();
                //ensures GC on calendar
                calendar = null;
                //gets na new instance for Sunday
                calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                Date sunday = calendar.getTime();

                //do action:
                if ((date.equals(monday) || date.after(monday)) && (date.equals(sunday) || date.before(sunday))) {

                    System.out.println(date.toString() + "sunday");
                    return true;
                }

                return false;
            }

            private String makeFragmentName(int viewId, int index) {
                return "android:switcher:" + viewId + ":" + index;
            }

            String setFragmentMatchesTabs(int position) {
                if (mMatchesOfTheDiv.size() > 0) {
                    Map<String, Match> matchMap = mMatchesOfTheDiv.get(position).getGames();
                    for (Map.Entry<String, Match> entry :
                            matchMap.entrySet()) {
                        Match value1 = entry.getValue();
                        mMatchesOfTheDiv.get(position).setDate(value1.getDate());
                    }
                    if (analizeDate(position)) {
                        dateInCurrentWeek = position;
                    }
                    // sets the viewpager to the games of the week.
                    // needs to be here to references the tab layout
                    if (dateInCurrentWeek != -1) {
                        mViewPager.setCurrentItem(dateInCurrentWeek);
                    }
                }
                return mMatchesOfTheDiv.get(position).getDate().toUpperCase();
            }

            void setFragmentStatsTabs(int position) {
                if (mMatchesOfTheDiv.size() > 0) {
                }
            }

            String setFragmentLeaderTabs(int position) {
                String header = "";
                if (leaderMaps.size() > 0) {
                    switch (position) {
                        case 0:
                            header = "goles".toUpperCase();
                            break;
                        case 1:
                            header = "Tarjetas".toUpperCase();
                            break;
                    }

                }
                return header;
            }

            /********************
             * FIRE BASE LISTENERS
             *********************/


            // returns the pertinent query to firebase by node name
            private Query calendarDivReferenceFinder(String id) {


                Query queryMatchesOfTheDay = null;
                switch (id) {
                    case "Div1_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv1Ref;
                        addCalendarEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    case "Div2_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv2Ref;
                        addCalendarEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    case "Div3_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv3Ref;
                        addCalendarEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    default:
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv1Ref;
                        addCalendarEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                }
                return queryMatchesOfTheDay;
            }

            private Query leaderDivReferenceFinder(String id) {

                Query queryLeaderPlayers = null;
                switch (id) {
                    case "Div1_Leader":
                        queryLeaderPlayers = mLeadersofTheDayDiv1Ref;
                        addLeadersEvelentListener(queryLeaderPlayers.orderByChild("pos"));
                        break;
                    case "Div2_Leader":/*
                        queryLeaderPlayers = mMatchesOfTheDayDiv2Ref;
                        addLeadersEvelentListener(queryLeaderPlayers.orderByChild("pos"));*/
                        break;
                    case "Div3_Leader":/*
                        queryLeaderPlayers = mMatchesOfTheDayDiv3Ref;
                        addLeadersEvelentListener(queryLeaderPlayers.orderByChild("pos"));*/
                        break;
                    default:/*
                        queryLeaderPlayers = mMatchesOfTheDayDiv1Ref;
                        addLeadersEvelentListener(queryLeaderPlayers.orderByChild("pos"));*/
                        break;
                }
                return queryLeaderPlayers;
            }

            //firebase event listener
            private void addLeadersPlayerEvenetListener(Query q) {
                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Players player = dataSnapshot.getValue(Players.class);
                        String playerid = dataSnapshot.getKey();

                       /* for (LeadersIndex index : mLeaderIndex) {

                            if (playerid.equals(index.getPlayerid())) {
                                player.setmFireBaseKey(dataSnapshot.getKey());
                                mPlayersOfTheDiv.add(player);
                                notifyDataSetChanged();
                            }
                        }*/
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
                });
            }

            //query the division calendar based on the firebase node name
            private void addLeadersEvelentListener(Query q) {

                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String key = dataSnapshot.getKey();
                        List<LeadersIndex> mLeaderIndex = new ArrayList<>();

                        for (DataSnapshot mdSnapshot : dataSnapshot.getChildren()) {
                            LeadersIndex p = mdSnapshot.getValue(LeadersIndex.class);
                            mLeaderIndex.add(p);

                        }

                        //   Map<String, Map<String, LeadersIndex>> p2 = dataSnapshot.getValue(j);
                        leaderMaps.put(key, mLeaderIndex);
                        notifyDataSetChanged();
                        // mLeaderIndex.add(p);
                    }

                    // DataSnapshot { key = cards, value = {P2={playerid=101, pos=2}, P1={playerid=100, pos=1}} }
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
                });
            }

            //query the division calendar based on the firebase node name
            private void addCalendarEvelentListener(Query q) {
                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Day d = dataSnapshot.getValue(Day.class);
                        dataSnapshot.getChildren();
                        d.setId(dataSnapshot.getKey());
                        d.setGames(dataSnapshot.getValue(t));
                        mMatchesOfTheDiv.add(d);
                        notifyDataSetChanged();

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
                });
            }


            /*********************
             * Fragment Overrrides
             **********************/
            @Override
            public Fragment getItem(int position) {

                boolean selected = false;
                Fragment frag = null;

                switch (fragmentId) {

                    case R.id.matches_stats:
                        frag = FragmentTourneyStats.newInstance();
                        break;

                    case R.id.matches_leaders:

                        List<LeadersIndex> list = new ArrayList<>();
                        int type = -1;
                        if (leaderMaps.size() > 0) {
                            switch (position) {
                                case 1:
                                    list = leaderMaps.get("goals");
                                    type = 1;
                                    break;
                                case 0:
                                    list = leaderMaps.get("cards");
                                    type = 0;
                                    break;
                            }
                            frag = FragmentLeaders.newInstance(list, type);
                        }

                        break;

                    case R.id.matches_calendar:
                        //  frag = FragmentMatches.newInstance();

                        if (mMatchesOfTheDiv.size() > 0) {

                            Map<String, Match> matchMap = mMatchesOfTheDiv.get(position).getGames();
                            for (Map.Entry<String, Match> entry :
                                    matchMap.entrySet()) {
                                Match value1 = entry.getValue();
                                mMatchesOfTheDiv.get(position).setDate(value1.getDate());
                            }
                            //returns true if the match date is in the current week
                            frag = FragmentMatches.newInstance(mMatchesOfTheDiv.get(position), selected);
                        }
                        break;
                }

                return frag;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getCount() {
                return getPagerCount();
            }

            @Override
            public int getItemPosition(Object object) {
                // POSITION_NONE makes it possible to reload the PagerAdapter
                return POSITION_NONE;

            }

            @Override
            public Parcelable saveState() {
                return null;
            }

            @Override
            public void restoreState(Parcelable state, ClassLoader loader) {

            }

            @Override
            public CharSequence getPageTitle(int position) {
                //Locale l = Locale.getDefault();
                // String x =  date ;

                switch (fragmentId) {

                    case R.id.matches_stats:
                        setFragmentStatsTabs(position);
                        return "Primera".toUpperCase();

                    case R.id.matches_leaders:
                        return setFragmentLeaderTabs(position);

                    case R.id.matches_calendar:
                        setFragmentMatchesTabs(position);
                        return mMatchesOfTheDiv.get(position).getDate().toUpperCase();
                }
                return null;
            }


        }
    }


}
