package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import upgrade.ntv.bangsoccer.Adapters.ClubsAdapter;
import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;

import static upgrade.ntv.bangsoccer.AppicationCore.FRAGMENT_CHOOSE_DIVISION;


public class ActivityClubSelect extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DivisionsAdapter.onDivisionFragmentInteractionListener {

    // name of the file to preserve areas
    private Activity thisActivity;
    private ClubsAdapter clubsAdapter;
    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.thisActivity = this;
        setContentView(R.layout.activity_club_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.clubs_cardList);
        recyclerView.setHasFixedSize(true);

       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lLayout = new GridLayoutManager(ActivityClubSelect.this, 2);
        recyclerView.setLayoutManager(lLayout);

        clubsAdapter =new ClubsAdapter(this);
        recyclerView.setAdapter(clubsAdapter);
        /*
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, null));*/

        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(this, recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(thisActivity, ActivityClubs.class);
                intent.putExtra("CLUBID",  clubsAdapter.getClubID(position));

                if (intent != null) {
                    startActivity(intent);
                   // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));




    }

    public void onClickedTeam(View v) {


        Intent intent = DrawerSelector.onItemSelected(this, 100);
      //  intent.putExtra("ID", );

        if (intent != null) {
            startActivity(intent);
          //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
    private void onDvisionChoosertDialog() {
        Log.i("main", "Calling Create Client Dialog Fragment");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
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

        if (id != R.id.nav_clubs) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {
                startActivity(intent);
             //   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDivisionSelected(String node) {

    }

    @Override
    public void onDivisionUnselected(String divisionKey) {

    }
}
