package upgrade.ntv.bangsoccer;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.ClubsAdapter;
import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.Schedule.Club;


public class ActivityClubSelect extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_clubs);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.clubs_cardList);
        recyclerView.setHasFixedSize(true);

       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lLayout = new GridLayoutManager(ActivityClubSelect.this, 4);
        recyclerView.setLayoutManager(lLayout);

        clubsAdapter =new ClubsAdapter(this);
        recyclerView.setAdapter(clubsAdapter);
        /*
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, null));*/

        recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(this, recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = DrawerSelector.onItemSelected(thisActivity, 100);
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
      //  getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      /*  int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(this, ActivitySettings.class);
            startActivity(intent);
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
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
}
