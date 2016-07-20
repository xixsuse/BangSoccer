package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Adapters.ClubsToFollowAdapter;
import upgrade.ntv.bangsoccer.AppConstants.AppConstant;
import upgrade.ntv.bangsoccer.Decorators.DividerItemDecoration;
import upgrade.ntv.bangsoccer.Schedule.Team;

import static upgrade.ntv.bangsoccer.AppConstants.AppConstant.clubItems;

public class ActivityFavoriteNFollow extends AppCompatActivity {

    private Activity thisActivity;
    public List<Team> clubsToSelect  = new ArrayList<>();
    private ClubsToFollowAdapter clubsAdapter;
    private GridLayoutManager lLayout;

    public  void cloneClubsItems() throws CloneNotSupportedException {
        for (int i = 0; i < clubItems.size(); i++) {
            Team myTeam =  clubItems.get(i);
            clubsToSelect.add(myTeam.getTeam_clone());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //references to self
        thisActivity=this;
        try {
            cloneClubsItems();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_favorite_nfollow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fav_n_follow_recuclerview);
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lLayout = new GridLayoutManager(ActivityFavoriteNFollow.this, 1);
        recyclerView.setLayoutManager(lLayout);

        clubsAdapter = new ClubsToFollowAdapter(clubsToSelect, this);
        recyclerView.setAdapter(clubsAdapter);
       // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, null));

  /*      recyclerView.addOnItemTouchListener(new RecyclerItemClickLister(this, recyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clubsAdapter.getClubID(position);

                Intent intent = DrawerSelector.onItemSelected(tourneyActivity, 100);
                intent.putExtra("CLUBID", position);

                ClubsToFollowAdapter.TeamHolder myholder   = (ClubsToFollowAdapter.TeamHolder) clubsAdapter.getItemHolder(position).getTag();


                if( myholder != null){

                    myholder.setCheckBoxState(!myholder.getCheckBox().isChecked());

                }

                if (intent != null) {
                 //   startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));*/


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_follow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

      /*  if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
*/
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
          for(int i = 0; AppConstant.clubItems.size() > i; i++);
            {
                clubItems=clubsToSelect;
                finish();
              //  AppConstant.clubItems.size()
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
