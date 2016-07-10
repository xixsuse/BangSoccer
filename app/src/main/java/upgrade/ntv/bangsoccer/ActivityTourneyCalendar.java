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
import android.widget.Spinner;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
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
    private TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
    private DrawerLayout drawer;
    private static Activity thisActivity;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private char mLastSelectedItem = 10;
    private MenuItem menuItem_1, menuItem_2;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Menu mMenu;

    public static Activity getReference() {
        return thisActivity;
    }

    public char getLastSelectedItem() {
        return mLastSelectedItem;
    }

    public void setLastSpinnerSelectedItem(char mLastSpinnerSelectedItem) {
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
        mTourneyCalendarPagerAdapter.setCurrentIconID('P');

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


    public char checkSRC(MenuItem item) {
        char imageSRC;
        //compares the current icon and returns the drawable id
        if (item.getTitle().equals("Partidos")) {
            imageSRC = 'P';
        } else if (item.getTitle().equals("Tabla de Posiciones")) {
            imageSRC = 'T';
        } else {
            imageSRC = 'L';
        }
        return imageSRC;
    }

 /*   public int checkID(int item){
        int imageID;
        //compares the current icon and returns the drawable id
        if(item==R.drawable.ic_match_vs){
            imageID= 1; //tourney
        }else if (item==R.drawable.ic_club_stats){
            imageID = 2; //stats
        }else {
            imageID = 3; //leader
        }
        return imageID;
    }*/

    public void onSpinnerSelecterWorker(MenuItem item) {

        char menu1src = checkSRC(menuItem_1);
        char menu2src = checkSRC(menuItem_2);
        char menuIcon = checkSRC(item);

        if (getLastSelectedItem() != menuIcon) {
            mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getSupportFragmentManager());
            mTourneyCalendarPagerAdapter.setCurrentIconID(menuIcon);
            switch (menuIcon) {
                case 'P':
                    mTourneyCalendarPagerAdapter.setCurrentIconID('P');
                    if (!tabLayout.isShown()) {
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                    // sets a new fragment and updates the view
                    mTourneyCalendarPagerAdapter.setPagerCount(AppConstant.mMatchArrayList.length);

                   mTourneyCalendarPagerAdapter.getItem(0);

                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();

                    toolbar.setTitle("Partidos");
                    try {
                        if (mMenu != null) {
                            if (menuIcon == menu1src) {
                                if ('T' == menu2src) {
                                    menuItem_1.setIcon(R.drawable.ic_leaders);
                                    menuItem_1.setTitle("Lideres");

                                } else {
                                    menuItem_1.setIcon(R.drawable.ic_club_stats);
                                    menuItem_1.setTitle("Tabla de Posiciones");
                                }
                            } else if ('T' == menu1src) {
                                menuItem_2.setIcon(R.drawable.ic_leaders);
                                menuItem_2.setTitle("Lideres");

                            } else {
                                menuItem_2.setIcon(R.drawable.ic_club_stats);
                                menuItem_2.setTitle("Tabla de Posiciones");
                            }
                        }

                    } catch (Exception e) {
                        Log.i("view exception", e.getMessage());
                    }
                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();
                    setLastSpinnerSelectedItem(menuIcon);

                    break;
                case 'T':
                    mTourneyCalendarPagerAdapter.setCurrentIconID('T');


                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }
                    mTourneyCalendarPagerAdapter.setPagerCount(1);

                    mTourneyCalendarPagerAdapter.getItem(0);
                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();

                    toolbar.setTitle("Tabla de Posiciones");

                    try {
                        if (mMenu != null) {
                            if (menuIcon == menu1src) {
                                if ('P' == menu2src) {
                                    menuItem_1.setIcon(R.drawable.ic_leaders);
                                    menuItem_1.setTitle("Lideres");
                                } else {
                                    menuItem_1.setIcon(R.drawable.ic_match_vs);
                                    menuItem_1.setTitle("Partidos");
                                }
                            } else if ('P' == menu1src) {
                                menuItem_2.setIcon(R.drawable.ic_leaders);
                                menuItem_2.setTitle("Lideres");
                            } else {
                                menuItem_2.setIcon(R.drawable.ic_match_vs);
                                menuItem_2.setTitle("Partidos");

                            }
                        }

                    } catch (Exception e) {
                        Log.i("view exception", e.getMessage());
                    }
                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();
                    setLastSpinnerSelectedItem(menuIcon);
                    break;

                case 'L':
                    mTourneyCalendarPagerAdapter.setCurrentIconID('L');
                    if (tabLayout.isShown()) {
                        tabLayout.setVisibility(View.GONE);
                    }



                    mTourneyCalendarPagerAdapter.setPagerCount(1);
                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();
                    mTourneyCalendarPagerAdapter.getItem(0);
                    toolbar.setTitle("Lideres");

                    try {
                        if (mMenu != null) {
                            if (menuIcon == menu1src) {
                                if ('P' == menu2src) {
                                    menuItem_1.setIcon(R.drawable.ic_club_stats);
                                    menuItem_1.setTitle("Tabla de Posiciones");
                                } else {
                                    menuItem_1.setIcon(R.drawable.ic_match_vs);
                                    menuItem_1.setTitle("Partidos");
                                }
                            } else if ('P' == menu1src) {
                                menuItem_2.setIcon(R.drawable.ic_club_stats);
                                menuItem_2.setTitle("Tabla de Posiciones");
                            } else {

                                menuItem_2.setIcon(R.drawable.ic_match_vs);
                                menuItem_2.setTitle("Partidos");
                            }
                        }

                    } catch (Exception e) {
                        Log.i("view exception", e.getMessage());
                    }
                    mTourneyCalendarPagerAdapter.notifyDataSetChanged();

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
            //startActivity(getParentActivityIntent());
            // overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        /*mMenu = menu;
        menuItem_1 = mMenu.findItem(R.id.action_tourney_menu_1);
        menuItem_1.setIcon(R.drawable.ic_club_stats);
        menuItem_1.setTitle("Tabla de Posiciones");
        menuItem_2 = mMenu.findItem(R.id.action_tourney_menu_2);
        menuItem_2.setIcon(R.drawable.ic_leaders);
        menuItem_2.setTitle("Lideres");*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
/*
        if (mTourneyCalendarPagerAdapter != null) {
            //erase all current fragment from the view pager
            mTourneyCalendarPagerAdapter.clearAll();
        }
*/


       // onSpinnerSelecterWorker(item);

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

    @Override
    public void onListFragmentInteraction() {
        onClickedFragmentLeaders();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //       Toast.makeText(ActivityTourneyCalendar.this, "onStart tourAct", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  mTourneyCalendarPagerAdapter.clearAll();
        //    Toast.makeText(ActivityTourneyCalendar.this, "onDestroy tourAct", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //   Toast.makeText(ActivityTourneyCalendar.this, "onRestart tourAct", Toast.LENGTH_SHORT).show();

    }

    /**
     * A placeholder fragment containing a simple view.
     * <p/>
     * <p/>
     * /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {

        private int currentIconID = 0;
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

                       oldFragment  = getFragmentForPosition(i);
                        if( oldFragment != null) {
                            fm.beginTransaction().remove(oldFragment).commit();
                        }
                }
                mPageReferenceMap.clear();
            }
        }

        public int getCurrentIconID() {
            return currentIconID;
        }

        public void setCurrentIconID(int currentIconID) {
            this.currentIconID = currentIconID;
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
                case 'P':
                    selectedFragmentType = FragmentMatches.newInstance(position + 1);
                   mPageReferenceMap.put(tag, selectedFragmentType);
                    break;

                case 'L':
                    selectedFragmentType = FragmentLeaders.newInstance();
                   mPageReferenceMap.put(tag, selectedFragmentType);
                    break;

                case 'T':
                    selectedFragmentType = FragmentTourneyStats.newInstance();
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
