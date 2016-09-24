package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.luseen.datelibrary.DateConverter;
import com.luseen.datelibrary.DateHelper;
import com.luseen.datelibrary.DatePatterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.TournamentObjects.Day;
import upgrade.ntv.bangsoccer.TournamentObjects.Divisions;
import upgrade.ntv.bangsoccer.TournamentObjects.Match;
import upgrade.ntv.bangsoccer.Utils.Preferences;

import static upgrade.ntv.bangsoccer.ActivityMain.mDivisions;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv1Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv2Ref;
import static upgrade.ntv.bangsoccer.ActivityMain.mMatchesOfTheDayDiv3Ref;

/**
 * Created by root on 7/10/16.
 */

public class FragmentViewPagerContainer extends Fragment  {
    private TourneyCalendarPagerAdapter mTourneyCalendarPagerAdapter;
    private ViewPager mViewPager;
    private List<Day> mMatchesOfTheDiv = new ArrayList<>();
    private int dateInCurrentWeek = -1;
    private final static String GAME_OF_THE_WEEK = "GAMES";



    private Query query;

    private GenericTypeIndicator<Map<String, Match>> t =
            new GenericTypeIndicator<Map<String, Match>>() {};
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
       // dateInCurrentWeek = getArguments().getInt(GAME_OF_THE_WEEK);

        View root = inflater.inflate(R.layout.fragment_content_recivleview, container, false);
        mTourneyCalendarPagerAdapter = new TourneyCalendarPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager, attaching the adapter and ...
        mViewPager = (ViewPager) root.findViewById(R.id.tourney_recycleview);
        mViewPager.setAdapter(mTourneyCalendarPagerAdapter);
        //sets the viewpager to the current week

        return root;
    }//2102

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    public void addSelectedDivision(String node) {

           mTourneyCalendarPagerAdapter.referenceFinder(node);
           mTourneyCalendarPagerAdapter.notifyDataSetChanged();

    }

    public void removeUnselectedDivision(String divisionKey) {

        mTourneyCalendarPagerAdapter.removeFromTheList(divisionKey);
        mTourneyCalendarPagerAdapter.notifyDataSetChanged();

    }




    public class TourneyCalendarPagerAdapter extends FragmentPagerAdapter {

        private boolean removeFromTheList(String divisionKey){
            int index = 0;
            for (Day day :
                    mMatchesOfTheDiv) {
                    Map<String, Match> matchMap = day.getGames();
                    for (Map.Entry<String, Match> entry :
                            matchMap.entrySet()) {
                        Match value1 = entry.getValue();
                      if(divisionKey.equals(value1.getTournamentId())){
                          //removes the tourney from the list

                             mMatchesOfTheDiv.remove(day);
                            // removeTabPage(index);
                             notifyDataSetChanged();

                      }
                    }

            }

            return  true;
        }



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
