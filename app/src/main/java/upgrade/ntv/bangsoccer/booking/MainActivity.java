package upgrade.ntv.bangsoccer.booking;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import upgrade.ntv.bangsoccer.R;

public class MainActivity extends AppCompatActivity {

    /** 809-549-7944
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public List<Fragment> fragments;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ref = FirebaseDatabase.getInstance().getReference();

        InitFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.ic_add_shopping_cart_black_24dp);

        mViewPager = (ViewPager) findViewById(R.id.container);

        setSupportActionBar(toolbar);
      //  getSupportActionBar().setIcon(R.drawable.ic_add_shopping_cart_black_24dp);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);



      //  for (int i = 0; i < tabLayout.getTabCount(); i++) {
       //     TabLayout.Tab tab = tabLayout.getTabAt(i);
       //     tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
     //   }

    }

    private void InitFragments() {

        fragments = new ArrayList<Fragment>();
        fragments.add(AvailableDatesFragment.newInstance(MyDateUtils.GetWeekKey(0)));
        fragments.add(AvailableDatesFragment.newInstance(MyDateUtils.GetWeekKey(1)));
        fragments.add(AvailableDatesFragment.newInstance(MyDateUtils.GetWeekKey(2)));
        fragments.add(AvailableDatesFragment.newInstance(MyDateUtils.GetWeekKey(3)));
        System.out.println(MyDateUtils.GetWeekKey(0));
        System.out.println(MyDateUtils.GetWeekKey(1));
        System.out.println(MyDateUtils.GetWeekKey(2));
        System.out.println(MyDateUtils.GetWeekKey(3));


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void PushReservations(View view) {


        for (ReservationModel reservation:MyReservations.reservations) {

            DateFormat formatter = new SimpleDateFormat("EEEMMMddKK:mm:ss");
            try{

                Date date = formatter.parse(reservation.ReservationDate);
                System.out.println(date);

            }

            catch (Exception ex){

                    System.out.println(ex.getMessage());
            }
           // String weekKey = Integer.toString(reservation.ReservationDate.get(Calendar.WEEK_OF_YEAR));

            //DatabaseReference reservationref = ref.child("Reservaciones").child("Week-"+weekKey);

            //reservationref.push().setValue(reservation);

        }
    }

    private String GetWeekFor(Calendar reservationDate) {

        System.out.println("This is the week for a reservation:"+reservationDate.WEEK_OF_YEAR);
        return  Integer.toString(reservationDate.WEEK_OF_YEAR);

    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.koala_fragment_main, container, false);
          //  TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            AvailableDatesFragment fragment;
           // MyDateUtils.GetWeekInterval(position);
           switch (position){

               case 0:

                  fragment= AvailableDatesFragment.newInstance((Integer.toString(MyDateUtils.GetWeek(0)-1)));
                   break;

               case 1:
                   fragment= AvailableDatesFragment.newInstance((Integer.toString(MyDateUtils.GetWeek(1)-1)));
                   break;

               case 2:
                   fragment= AvailableDatesFragment.newInstance((Integer.toString(MyDateUtils.GetWeek(2)-1)));
                   break;

               case 3:
                   fragment= AvailableDatesFragment.newInstance((Integer.toString(MyDateUtils.GetWeek(3)-1)));
                   break;

               default:
                   System.out.println("This is the reservation Fragment");
                   if(MyReservations.IsEmpty())
                   {
                       return new BlankFragment();
                   }
                   return new MyReservationsFragment();

           }
        return fragment;




        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size()+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            //return tabTitles[position];
            System.out.println("getting page title:"+position);

            if(position==4)
                return "Reservations";
            return  MyDateUtils.GetWeekInterval(position);
        }

      //  public View getTabView(int position) {
        //   View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout., null);
           // TextView tv = (TextView) tab.findViewById(R.id.custom_text);
          //  tv.setText(tabTitles[position]);
          //  return tab ;
      //  }
    }
}
