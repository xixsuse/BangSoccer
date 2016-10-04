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

import upgrade.ntv.bangsoccer.Adapters.DivisionsAdapter;
import upgrade.ntv.bangsoccer.Dialogs.DivisionChooserFragment;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;


public class ActivityAbout extends AppCompatActivity implements CollapsingToolbarLayout.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        DivisionsAdapter.onDivisionFragmentInteractionListener {


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
        text.setText( "We’re a multidisciplinary group made up of engineers, developers, designers and marketers, joined to create fantastic web and mobile services. \n \n"+
                "As our name says, we’re always looking to improve not only how we design and develop, but also our lives and the lives of others. \n \n" +
               "To put it simply, to be the best at everything we do is our biggest inspiration. \n");
        text.setTextSize(16);
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

    @Override
    public void onDivisionUnselected(String node) {

    }
}
