package upgrade.ntv.bangsoccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;


public class ActivityAbout extends AppCompatActivity implements CollapsingToolbarLayout.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        DivisionChooserFragment.onDivisionFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabContactUs = (FloatingActionButton) findViewById(R.id.fab_contact_us);
        fabContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, R.string.dev_email);
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.email_body);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);

        TextView text = (TextView) findViewById(R.id.about_text);
        text.setText( "We’re a group of dominicans who are very different from one another, which is great for creative problem-solving and out-of-the-box"+
              "  thinking but it turns choosing a pizza topping into a board meeting. But despite our differences we’re joined together by a common belief"+
                " if you want something to be, go out there and make it yourself” \n"+ " This common belief is what drives the projects we choose to make. We’re heavily invested in our projects because we grew tired of waiting for " +
               "others to come along and make them. We have three standards for undertaking anything: we either learn a lot from this idea, we love " +
               "it (like what), or we’re going to enjoy the hell out of making it. This is why we’re not a company that you can hire for any project, like" +
               " the next “Hot or Not” or show us your\n" +
               " “It’s like Uber but for horse-drawn carriages (which admittedly sounds hilarious).\n" +
               " If you really have faith in your idea email us at info@upgrade.do and make us believe in it.\n/" +
               "  We make the things we love, and we love beautiful things, functional things. We love things that kick ass and look good doing it.");
        text.setTextSize(14);
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
    public void onClick(View v) {
        // onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_about) {

            Intent intent = DrawerSelector.onItemSelected(this, id);

            if (intent != null) {
                startActivity(intent);
               // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDivisionSelected(String node) {

    }
}
