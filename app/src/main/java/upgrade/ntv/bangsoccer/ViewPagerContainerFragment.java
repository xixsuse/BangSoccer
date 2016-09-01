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

import java.util.ArrayList;
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
    private  static List<Day> mMatchesOfTheDiv = new ArrayList<>();
    private  List<Match> mMatchesOfTheDay= new ArrayList<>();
    private  Query query ;
    private  GenericTypeIndicator<Map<String,Match>> t = new GenericTypeIndicator<Map<String,Match>>() {};

    //empty cosntructor
    public ViewPagerContainerFragment() { //holds the tourney calendar
    }

    public static ViewPagerContainerFragment newInstance() {

     /*   for (Divisions division : ActivityMain.mDivisions) {
            if(division.isSelected()){
                mMatchesOfTheDayRef.add(databaseReference.child(division.getNode()).orderByChild("date"));
            }
        }*/
        //instantiates a new instance of this type
        ViewPagerContainerFragment fragment = new ViewPagerContainerFragment();


        return fragment;
    }



    public static List<Day> getMatchesOfTheDay () {
        return mMatchesOfTheDiv;
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

    public ViewPager getViewPager()
    {
        return this.mViewPager;
    }

    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {

        public  Query referenceFinder(String id){

            Query q = null;
            switch(id){
                case "Div1_Calendar":
                    q =   mMatchesOfTheDayDiv1Ref;
                    q.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Day d = dataSnapshot.getValue(Day.class);
                            dataSnapshot.getChildren();
                            d.setId(dataSnapshot.getKey());
                            Map<String, Match> matchMap = (dataSnapshot.getValue(t));
                            d.setGames(dataSnapshot.getValue(t)) ;
                            mMatchesOfTheDiv.add(d);
                            notifyDataSetChanged();



                            for (Map.Entry<String, Match> entry :
                                    matchMap.entrySet()) {

                                String key = entry.getKey();
                                Match value = entry.getValue();
                                value.setMatchId(key);
                                mMatchesOfTheDay.add(value);
                                //getMatchList().add(value);
                            }
                            //    Match m = d.getGames().get();

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
                    break;
                case "Div12_Calendar":
                    q =   mMatchesOfTheDayDiv2Ref;
                    q.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Day d = dataSnapshot.getValue(Day.class);
                            dataSnapshot.getChildren();
                            d.setGames(dataSnapshot.getValue(t));
                            d.setId(dataSnapshot.getKey());
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
                    break;
                case "Div3_Calendar":
                    q =   mMatchesOfTheDayDiv3Ref;
                    q.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Day d = dataSnapshot.getValue(Day.class);
                            dataSnapshot.getChildren();
                            d.setGames(dataSnapshot.getValue(t));
                            d.setId(dataSnapshot.getKey());
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
                    break;
            }
            return q;
        }

        public TourneyCalendarPagerAdapter(FragmentManager fm) {
            super(fm);
            query = referenceFinder("Div1_Calendar");
        }

        public int getPagerCount() {
            //TODO : do this the propper way... the 10 is for testing
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
            if(mMatchesOfTheDiv.size() > 0 ){
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