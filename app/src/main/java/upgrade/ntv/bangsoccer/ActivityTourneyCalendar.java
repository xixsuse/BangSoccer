package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class ActivityTourneyCalendar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentNewsFeed.OnListFragmentInteractionListener, FragmentLeaders.OnListFragmentInteractionListener, FragmentTourneyStats.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
    private DrawerLayout drawer;
    private static Activity thisActivity;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private int mLastSelectedItem = 10;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Menu mMenu;

    public static Activity getReference() {
        return thisActivity;
    }

    public int getLastSelectedItem() {
        return mLastSelectedItem;
    }

    public void setLastSpinnerSelectedItem(int mLastSpinnerSelectedItem) {
        this.mLastSelectedItem = mLastSpinnerSelectedItem;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLastSelectedItem = 10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney_matches);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        ActivityTourneyCalendar.thisActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Section 2",
                        "Section 3",
                }));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);
        assert tabLayout != null;
        if (!tabLayout.isShown()) {
            tabLayout.setVisibility(View.VISIBLE);
        }
        mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getSupportFragmentManager());
        //set the default id
        setCurrentIconID(R.id.matches_calendar);
        mTourneyCalendarPagerAdapter.setPagerCount(AppConstant.mMatchArrayList.length);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mTourneyCalendarPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(Constants.TOURNAMENT_ACTIVITY);
        //dynamically adds the tourneys to follow
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {/*  actionBar.setSelectedNavigationItem(position);*/}
        });

        ImageView matchButton = (ImageView) findViewById(R.id.matches_calendar);
        matchButton.setOnClickListener(tourneyBarListner(matchButton));
        ImageView statsButton = (ImageView) findViewById(R.id.matches_stats);
        statsButton.setOnClickListener(tourneyBarListner(statsButton));
        ImageView leadersButton = (ImageView) findViewById(R.id.matches_leaders);
        leadersButton.setOnClickListener(tourneyBarListner(leadersButton));
        ImageView newsButton = (ImageView) findViewById(R.id.matches_news);
        newsButton.setOnClickListener(tourneyBarListner(newsButton));

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

                    Log.i("ActivityClubs", "onPanelStateChanged " + newState.name());

                }
                if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {


                }
                Log.i("ActivityClubs", "onPanelStateChanged " + newState);

            }
        });


    }

    private View.OnClickListener tourneyBarListner(final ImageView button) {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBottomBarrSelected(view.getId());
            }
        });
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public void onBottomBarrSelected(int id) {

        if (getLastSelectedItem() != id) {
            switch (id) {
                case R.id.matches_calendar:
                    setCurrentIconID(R.id.matches_calendar);
                    if (!tabLayout.isShown()) {
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                    // sets a new fragment and updates the view
                    resetAdapterWithNewPagerCount(AppConstant.mMatchArrayList.length, id);
                    setLastSpinnerSelectedItem(R.id.matches_calendar);
                    break;
                case R.id.matches_stats:
                    setCurrentIconID(R.id.matches_stats);
                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }
                    resetAdapterWithNewPagerCount(1, id);
                    setLastSpinnerSelectedItem(R.id.matches_stats);
                    break;

                case R.id.matches_leaders:
                    setCurrentIconID(R.id.matches_leaders);
                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }
                    resetAdapterWithNewPagerCount(1, id);
                    setLastSpinnerSelectedItem(R.id.matches_leaders);

                    break;

                case R.id.matches_news:
                    setCurrentIconID(R.id.matches_news);
                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }
                    resetAdapterWithNewPagerCount(1, id);
                    setLastSpinnerSelectedItem(R.id.matches_news);
                    break;

            }            //mTourneyCalendarPagerAdapter.notifyDataSetChanged();
        }

        mTourneyCalendarPagerAdapter.notifyDataSetChanged();
        mTourneyCalendarPagerAdapter.getItem(0);
    }

    public boolean resetAdapterWithNewPagerCount(int pageCount, int id) {

        boolean success = false;
        try {
            mTourneyCalendarPagerAdapter.clearAll();
           // mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getSupportFragmentManager());
            //set the default id
           mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getSupportFragmentManager());
            //set the default id
            setCurrentIconID(id);
            mTourneyCalendarPagerAdapter.setPagerCount(pageCount);

            // Set up the ViewPager with the sections adapter.
            //mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mTourneyCalendarPagerAdapter);
            //setCurrentIconID(id);


            success = true;
        } catch (Exception e) {
            Log.i("tourneyBar ", e.getMessage());
           // FirebaseCrash.report(e);
        }


        return success;
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
            //startActivity(getParentActivityIntent());
            // overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
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


    /**
     * A placeholder fragment containing a simple view.
     * <p/>
     * <p/>
     * /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private static int currentIconID = 0;
    public void setCurrentIconID(int currentIconid) {
        ActivityTourneyCalendar.currentIconID = currentIconid;
    }

    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {


        private int pagerCount = 1;
        private Map<String, Fragment> mPageReferenceMap = new HashMap<>();

        public TourneyCalendarPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void clearAll() //Clear all pages
        {
            if (mPageReferenceMap != null) {
                if (mPageReferenceMap.size() > 0) {
                    Fragment oldFragment = null;
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 0; i < mPageReferenceMap.size(); i++)

                        oldFragment = getFragmentForPosition(i);
                    if (oldFragment != null) {
                        fm.beginTransaction().remove(oldFragment).commit();
                    }
                }
                mPageReferenceMap.clear();
            }
        }

        public int getCurrentIconID() {
            return currentIconID;
        }



        public int getPagerCount() {
            return pagerCount;
        }

        public void setPagerCount(int pagerCount) {
            this.pagerCount = pagerCount;
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragmentType(position);

        }

        /**
         * returns the fragment depending on the toolbar icon selection
         **/
        public Fragment fragmentType(int position) {
            Fragment selectedFragmentType = null;
            String tag = makeFragmentName(mViewPager.getId(), (int) getItemId(position));
            int typeID = getCurrentIconID();
            switch (typeID) {
                case R.id.matches_calendar:
                    selectedFragmentType = FragmentMatches.newInstance(position + 1);
                    mPageReferenceMap.put(tag, selectedFragmentType);
                    break;
                case R.id.matches_stats:
                    selectedFragmentType = FragmentTourneyStats.newInstance();
                    mPageReferenceMap.put(tag, selectedFragmentType);
                    break;
                case R.id.matches_leaders:
                    selectedFragmentType = FragmentLeaders.newInstance();
                    mPageReferenceMap.put(tag, selectedFragmentType);
                    break;
                case R.id.matches_news:
                    selectedFragmentType = FragmentNewsFeed.newInstance();
                    mPageReferenceMap.put(tag, selectedFragmentType);
                    break;
            }
            return selectedFragmentType;
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
            return getPagerCount();

        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
                case 5:
                    return getString(R.string.title_section6).toUpperCase(l);
                case 6:
                    return getString(R.string.title_section7).toUpperCase(l);
                case 7:
                    return getString(R.string.title_section8).toUpperCase(l);
                case 8:
                    return getString(R.string.title_section9).toUpperCase(l);
                case 9:
                    return getString(R.string.title_section10).toUpperCase(l);


            }
            return null;
        }
    }


}
