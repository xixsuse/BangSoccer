package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import upgrade.ntv.bangsoccer.TournamentObjects.Day;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;

import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv1Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv2Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv3Ref;

/**
 * Created by root on 7/10/16.
 */

public class ViewPagerContainerFragment extends Fragment {
    private TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
    private ViewPager mViewPager;
    private Context mContext;
    private List<Day> mMatchesOfTheDiv = new ArrayList<>();
    private List<Match> mMatchesOfTheDay = new ArrayList<>();
    private Query query;
    private GenericTypeIndicator<Map<String, Match>> t = new GenericTypeIndicator<Map<String, Match>>() {
    };
    private String date = new SimpleDateFormat("EEEEE dd").format(new Date());
    //empty cosntructor
    public ViewPagerContainerFragment() { //holds the tourney calendar
    }

    public static ViewPagerContainerFragment newInstance() {
        return new ViewPagerContainerFragment();
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
    }//2102

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {

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

        private Query referenceFinder(String id) {

            Query queryMatchesOfTheDay = null;
            switch (id) {
                case "Div1_Calendar":
                    queryMatchesOfTheDay = mMatchesOfTheDayDiv1Ref;
                    addEvelentListener(queryMatchesOfTheDay);
                    break;
                case "Div12_Calendar":
                    queryMatchesOfTheDay = mMatchesOfTheDayDiv2Ref;
                    addEvelentListener(queryMatchesOfTheDay);
                    break;
                case "Div3_Calendar":
                    queryMatchesOfTheDay = mMatchesOfTheDayDiv3Ref;
                    addEvelentListener(queryMatchesOfTheDay);
                    break;
            }
            return queryMatchesOfTheDay;
        }

        public TourneyCalendarPagerAdapter(FragmentManager fm) {
            super(fm);
            if (mMatchesOfTheDiv.size() == 0) {
                query = referenceFinder("Div1_Calendar");
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            FragmentMatches frag = new FragmentMatches();
            if (mMatchesOfTheDiv.size() > 0) {
                frag = FragmentMatches.newInstance(mMatchesOfTheDiv.get(position));
            }

            return frag;
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
          String x =  date ;
            if (mMatchesOfTheDiv.size() > 0) {

                Map<String, Match> matchMap = mMatchesOfTheDiv.get(position).getGames();
                for (Map.Entry<String, Match> entry :
                        matchMap.entrySet()) {
                    Match value1 = entry.getValue();
                    mMatchesOfTheDiv.get(position).setDate(value1.getDate());
                }

                return mMatchesOfTheDiv.get(position).getDate().toUpperCase();
            }
            return null;
        }
    }
}
