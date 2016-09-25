package upgrade.ntv.bangsoccer;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.luseen.datelibrary.DateConverter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.TournamentObjects.Day;
import upgrade.ntv.bangsoccer.TournamentObjects.Divisions;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;
import upgrade.ntv.bangsoccer.Utils.Preferences;

import static upgrade.ntv.bangsoccer.ActivityMain.mDivisions;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv1Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv2Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv3Ref;
import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;


public class ActivityTourneyCalendar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentNewsFeed.OnListFragmentInteractionListener, FragmentLeaders.OnListFragmentInteractionListener,
        FragmentTourneyStats.OnListFragmentInteractionListener, DivisionChooserFragment.onDivisionFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private static DrawerLayout drawer;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private int mLastSelectedItem;

    private FragmentViewPagerContainer mFragmentContainer;

    //bottom bar views
    @BindView(R.id.matches_calendar)
    ImageView matchButton ;

    @BindView(R.id.matches_stats)
    ImageView  statsButton ;

    @BindView(R.id.matches_leaders)
    ImageView leadersButton ;


    public int getLastSelectedItem() {
        return mLastSelectedItem;
    }

    public void setLastSpinnerSelectedItem(int mLastSpinnerSelectedItem) {
        this.mLastSelectedItem = mLastSpinnerSelectedItem;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney);
        mFragmentContainer = FragmentViewPagerContainer.newInstance();

        ButterKnife.bind(this);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        BindActivity();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, mFragmentContainer, "matches");
        ft.commit();
        setLastSpinnerSelectedItem(R.id.matches_calendar);

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
                }
                if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                }

            }
        });
    }


    private void BindActivity() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(Constants.TOURNAMENT_ACTIVITY);
        //dynamically adds the tourneys to follow

    }



    private View.OnClickListener onBottomBarClickListner() {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (getLastSelectedItem() != view.getId()) {

                    onOtherButtonBarSelected(id);
                }
            }

        });
    }

    public void onOtherButtonBarSelected(int id) {

        if (getLastSelectedItem() != id) {
            switch (id) {
                case R.id.matches_stats:

                    if (getSupportFragmentManager().findFragmentByTag("stats") == null) {
                        FragmentTourneyStats.newInstance();
                    } else {
                      //  (FragmentTourneyStats) getSupportFragmentManager().findFragmentByTag("stats");
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, FragmentTourneyStats.newInstance() , "stats")
                            .addToBackStack(null)
                            .commit();

                    setLastSpinnerSelectedItem(R.id.matches_stats);
                    break;

                case R.id.matches_leaders:

                    if (getSupportFragmentManager().findFragmentByTag("leaders") == null) {
                        FragmentLeaders.newInstance();
                    } else {
                      //  (FragmentLeaders) getSupportFragmentManager().findFragmentByTag("leaders");
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, FragmentLeaders.newInstance(), "leaders")
                            .addToBackStack(null)
                            .commit();

                    setLastSpinnerSelectedItem(R.id.matches_leaders);

                    break;


                case R.id.matches_calendar:

                    setLastSpinnerSelectedItem(R.id.matches_calendar);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, FragmentLeaders.newInstance(), "leaders")
                            .addToBackStack(null)
                            .commit();

                    setLastSpinnerSelectedItem(R.id.matches_leaders);

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
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
    private void onDvisionChoosertDialog(){
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
        if (id != Constants.TOURNAMENT_ACTIVITY) {
            Intent intent = DrawerSelector.onItemSelected(this, id);
            if (intent != null) {
                startActivity(intent);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onClickedFragmentLeaders() {
        try {
            if (findViewById(R.id.dragView) != null) {
                findViewById(R.id.dragView).setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Log.i("view exception", e.getMessage());
        }
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onListFragmentInteraction() {
        onClickedFragmentLeaders();
    }

    @Override
    public void onDivisionSelected(String node) {
        //updates the viewpager adapater
        mFragmentContainer.addSelectedDivision(node);
    }

    @Override
    public void onDivisionUnselected(String divisionKey) {
        //updates the viewpager adapater
   mFragmentContainer.removeUnselectedDivision(divisionKey);
    }



    public static class FragmentViewPagerContainer extends Fragment {

        private FragmentViewPagerContainer.TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
        private ViewPager mViewPager;
        private List<Day> mMatchesOfTheDiv = new ArrayList<>();
        private int dateInCurrentWeek = -1;

        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(R.id.tabs)
        TabLayout tabLayout;

        private Unbinder unbinder;


        public Toolbar getToolbar(){
            return toolbar;
        }
        public TabLayout getTabLayout(){
            return tabLayout;
        }

        private GenericTypeIndicator<Map<String, Match>> t = new GenericTypeIndicator<Map<String, Match>>() {};
        private Date date = new Date();
        //empty cosntructor
        public FragmentViewPagerContainer() { //holds the tourney calendar
        }

        public static FragmentViewPagerContainer newInstance() {
           FragmentViewPagerContainer frag = new FragmentViewPagerContainer();
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_content_recivleview, container, false);
            unbinder = ButterKnife.bind(this, root);

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setupWithViewPager(mViewPager);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            if (toolbar != null) {
                toolbar.setTitle("Torneo");
            }

            mTourneyCalendarPagerAdapter = new  FragmentViewPagerContainer.TourneyCalendarPagerAdapter(getChildFragmentManager());

            // Set up the ViewPager, attaching the adapter and ...
            mViewPager = (ViewPager) root.findViewById(R.id.tourney_recycleview);
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


        @Override public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
        public void addSelectedDivision(String node) {

            mTourneyCalendarPagerAdapter.referenceFinder(node);
            mTourneyCalendarPagerAdapter.notifyDataSetChanged();

        }

        public void removeUnselectedDivision(String divisionKey) {

            mTourneyCalendarPagerAdapter.nukeElement(divisionKey);
            mTourneyCalendarPagerAdapter.notifyDataSetChanged();

        }

        public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {
            private Query query;

            synchronized  List<Day> matchScanner(String divisionKey){
                List<Day> array = new ArrayList<>();
                if (mMatchesOfTheDiv.size() >0) {

                    for (Day day : mMatchesOfTheDiv) {
                        //iterate thru the map to pull all the
                        Map<String, Match> matchMap = day.getGames();
                        for (Map.Entry<String, Match> entry :
                                matchMap.entrySet()) {
                            Match value1 = entry.getValue();

                            if (divisionKey.equals(value1.getTournamentId())) {
                                //removes the tourney from the list
                                if(!array.contains(day)){

                                    array.add(day);
                                }
                            }
                        }

                    }
                }
                return array;
            }
            boolean removeDay( List<Day> array){
                boolean result = false;

                for (Day day :  array
                        ) {

                    mMatchesOfTheDiv.remove(day);
                    // removeTabPage(index);
                    notifyDataSetChanged();
                }

                return  result;
            }

            //remove a division.
            boolean nukeElement(String divisionKey){
                return removeDay(matchScanner(divisionKey));
            }

            //query the division calendar based on the firebase node name
            private void addEvelentListener(Query q) {
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
            // returns the pertinent query to firebase by node name
            private Query referenceFinder(String id) {

                Query queryMatchesOfTheDay = null;
                switch (id) {
                    case "Div1_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv1Ref;
                        addEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    case "Div12_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv2Ref;
                        addEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    case "Div3_Calendar":
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv3Ref;
                        addEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                    default:
                        queryMatchesOfTheDay = mMatchesOfTheDayDiv1Ref;
                        addEvelentListener(queryMatchesOfTheDay.orderByChild("date"));
                        break;
                }
                return queryMatchesOfTheDay;
            }



            public TourneyCalendarPagerAdapter(FragmentManager fm) {
                super(fm);
                if (mMatchesOfTheDiv.size() == 0) {
                    for (Divisions div : mDivisions) {
                        //checks if its saved in the shared preferences
                        if (Preferences.getPreferredDivisions(getActivity(), div.getNode()))
                            //get the calendar for a given node
                            query = referenceFinder(div.getNode());
                    }


                }
            }

            public int getPagerCount() {
                return mMatchesOfTheDiv.size();
            }

            private String makeFragmentName(int viewId, int index) {
                return "android:switcher:" + viewId + ":" + index;
            }


            @Override
            public Fragment getItem(int position) {
                boolean selected = false;

                FragmentMatches frag = new FragmentMatches();
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

                return frag;
            }

            public boolean analizeDate(int position){
                //match day converted to Date to compare against the upcoming dates and setup the tablayout
                date =  DateConverter.stringToDate(mMatchesOfTheDiv.get(position).getDate() , "dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                //default day is Sunday, set to Monday
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                //holds the monday pof the week
                Date monday =  calendar.getTime();
                //ensures GC on calendar
                calendar = null;
                //gets na new instance for Sunday
                calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                Date sunday = calendar.getTime();

                //do action:
                if ((date.equals(monday) || date.after(monday)) && (date.equals(sunday) || date.before(sunday))){

                    System.out.println(date.toString() + "sunday");
                    return true;
                }

                return false;
            }

            public
            @Nullable
            Fragment getFragmentForPosition(int position) {
                String tag = makeFragmentName(mViewPager.getId(), (int) getItemId(position));
                Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
                return fragment;
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
            public CharSequence getPageTitle(int position) {
                //Locale l = Locale.getDefault();
                // String x =  date ;
                if (mMatchesOfTheDiv.size() > 0) {
                    Map<String, Match> matchMap = mMatchesOfTheDiv.get(position).getGames();
                    for (Map.Entry<String, Match> entry :
                            matchMap.entrySet()) {
                        Match value1 = entry.getValue();
                        mMatchesOfTheDiv.get(position).setDate(value1.getDate());
                    }
                    if(analizeDate(position)){
                        dateInCurrentWeek = position;
                    }
                    // sets the viewpager to the games of the week.
                    // needs to be here to references the tab layout
                    if(dateInCurrentWeek !=-1){
                        mViewPager.setCurrentItem(dateInCurrentWeek, true);
                    }
                    return mMatchesOfTheDiv.get(position).getDate().toUpperCase();
                }
                return null;
            }
        }
    }



}
