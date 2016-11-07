package upgrade.ntv.bangsoccer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import upgrade.ntv.bangsoccer.dao.DBNewsFeed;


public class ActivityNewsDetails extends AppCompatActivity implements
        FragmentNewsFeeddetails.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int newsFeedID=-1;

    //private List<NewsFeedItem> newsFeedItems = new ArrayList<>();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_details);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            newsFeedID= extras.getInt("MynewsFeedID");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setCurrentItem(newsFeedID);


    }

    @Override
    public void onListFragmentInteraction() {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int qty;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            List<DBNewsFeed> temp = FragmentNewsFeeddetails.getNewsfeedList();
            if(temp!=null)
                qty=temp.size();
            else
                qty=AppicationCore.getAllNewsFeed().size();

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FragmentNewsFeeddetails.newInstance(position);
        }

        @Override
        public int getCount() {
            return qty;
        }

    }
}
