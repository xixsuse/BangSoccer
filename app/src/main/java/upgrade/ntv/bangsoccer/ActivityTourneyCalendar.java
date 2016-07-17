package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;


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

    private DrawerLayout drawer;
    public static Activity tourneyActivity;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private int mLastSelectedItem;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Menu mMenu;

    //fragment holders
    private final ThreadLocal<ViewPagerContainerFragment> viewPagerContainerFragment = new ThreadLocal<>();
    private final ThreadLocal<FragmentNewsFeed> fragmentNewsFeed = new ThreadLocal<>();
    private final ThreadLocal<FragmentTourneyStats> fragmentTourneyStats = new ThreadLocal<>();
    private final ThreadLocal<FragmentLeaders> fragmentLeaders = new ThreadLocal<>();

    public static Activity getReference() {
        return tourneyActivity;
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
        mViewPager = viewPagerContainerFragment.get().getViewPager();
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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney_matches);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        ActivityTourneyCalendar.tourneyActivity = this;
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

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);
        assert tabLayout != null;
        if (!tabLayout.isShown()) {
            tabLayout.setVisibility(View.VISIBLE);
        }

        viewPagerContainerFragment.set(ViewPagerContainerFragment.newInstance());

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


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, viewPagerContainerFragment.get(), "matches");
        ft.commit();
        setLastSpinnerSelectedItem(R.id.matches_calendar);
        initButtons();

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

    private void initButtons() {
        ImageView matchButton = (ImageView) findViewById(R.id.matches_calendar);
        matchButton.setOnClickListener(tourneyBarListner());

        ImageView statsButton = (ImageView) findViewById(R.id.matches_stats);
        statsButton.setOnClickListener(tourneyBarOthersListner());

        ImageView leadersButton = (ImageView) findViewById(R.id.matches_leaders);
        leadersButton.setOnClickListener(tourneyBarOthersListner());

        ImageView newsButton = (ImageView) findViewById(R.id.matches_news);
        newsButton.setOnClickListener(tourneyBarOthersListner());
    }

    private View.OnClickListener tourneyBarListner() {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLastSelectedItem() != view.getId()) {
                    if (!tabLayout.isShown()) {
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                    if (getSupportFragmentManager().findFragmentByTag("matches") == null) {
                        viewPagerContainerFragment.set(ViewPagerContainerFragment.newInstance());
                    } else {
                        viewPagerContainerFragment.set((ViewPagerContainerFragment) getSupportFragmentManager().findFragmentByTag("matches"));
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, viewPagerContainerFragment.get(), "matches")
                            .addToBackStack(null)
                            .commit();

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

                    setLastSpinnerSelectedItem(R.id.matches_calendar);
                }
            }

        });
    }

    private View.OnClickListener tourneyBarOthersListner() {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onOtherButtonBarSelected(view.getId());
            }
        });
    }


    public void onOtherButtonBarSelected(int id) {

        if (getLastSelectedItem() != id) {
            switch (id) {
                case R.id.matches_stats:
                  if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }

                    if (getSupportFragmentManager().findFragmentByTag("stats") == null) {
                        fragmentTourneyStats.set(FragmentTourneyStats.newInstance());
                    } else {
                        fragmentTourneyStats.set((FragmentTourneyStats) getSupportFragmentManager().findFragmentByTag("stats"));
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, fragmentTourneyStats.get(), "stats")
                            .addToBackStack(null)
                            .commit();

                    setLastSpinnerSelectedItem(R.id.matches_stats);
                    break;

                case R.id.matches_leaders:

                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }


                    if (getSupportFragmentManager().findFragmentByTag("leaders") == null) {
                        fragmentLeaders.set(FragmentLeaders.newInstance());
                    } else {
                        fragmentLeaders.set((FragmentLeaders) getSupportFragmentManager().findFragmentByTag("leaders"));
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, fragmentLeaders.get(), "leaders")
                            .addToBackStack(null)
                            .commit();

                    setLastSpinnerSelectedItem(R.id.matches_leaders);

                    break;

                case R.id.matches_news:
                  if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }

                    if (getSupportFragmentManager().findFragmentByTag("news") == null) {
                        fragmentNewsFeed.set(FragmentNewsFeed.newInstance());
                    } else {
                        fragmentNewsFeed.set((FragmentNewsFeed) getSupportFragmentManager().findFragmentByTag("news"));
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, fragmentNewsFeed.get(), "news")
                            .addToBackStack(null)
                            .commit();
                    //TODO: replce null to back stack with name and methos for proper pop
                    setLastSpinnerSelectedItem(R.id.matches_news);
                    break;

            }


        }
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

}
