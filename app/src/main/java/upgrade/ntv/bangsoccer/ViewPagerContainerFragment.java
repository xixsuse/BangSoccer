package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Schedule.Match;

/**
 * Created by root on 7/10/16.
 */

public class ViewPagerContainerFragment extends Fragment {
    private TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
    private ViewPager mViewPager;
    private Context mContext;
    public ViewPagerContainerFragment() {

    }

    public static ViewPagerContainerFragment newInstance() {

        ViewPagerContainerFragment fragment = new ViewPagerContainerFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_content_recivleview, container, false);

        mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager, attaching the adapter and ...
        mViewPager = (ViewPager) root.findViewById(R.id.tourney_recycleview);
        mViewPager.setAdapter(mTourneyCalendarPagerAdapter);

        return root;
    }

    public ViewPager getViewPager()
    {
        return this.mViewPager;
    }

    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {

        private List<Match> matchList = new ArrayList<>();

        public List<Match> getMatchList() {
            return matchList;
        }

        public void setMatchList(List<Match> matchList) {
            this.matchList = matchList;
        }

        public TourneyCalendarPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public int getPagerCount() {
            return AppConstant.mMatchArrayList.length;
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return  FragmentMatches.newInstance(position + 1);

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