package upgrade.ntv.bangsoccer.booking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.R;

/**
 * Created by Carbs.Wang on 2016/6/24.
 */
public class ActivityBooking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,  View.OnClickListener, NumberPickerView.OnValueChangeListener{

    @BindView(R.id.picker_hour)
    NumberPickerView mPickerViewH;
    @BindView(R.id.picker_minute)
    NumberPickerView mPickerViewM;
    @BindView(R.id.booking_selected_date)
    AppCompatTextView selectedDateTextView;
    @BindView(R.id.booking_submit_selection)
    ImageView mSubmit;
    @BindView(R.id.toolbar)
    Toolbar toolbar ;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            selectedDateTextView.setText((String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);



        mPickerViewH.setOnValueChangedListener(this);
        mPickerViewM.setOnValueChangedListener(this);
        mSubmit.setOnClickListener(this);
        selectedDateTextView = (AppCompatTextView)this.findViewById(R.id.booking_selected_date);
        initTime();
    }

    //habdles the initialization of the pickers
    private void initTime(){
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);

        h = h % 12;

        setData(mPickerViewH, 0, 11, h);
        setData(mPickerViewM, 0, 59, m);

       // setData(mPickerViewH, 1, 31, 1 /*LAST VALUE IS WHERE IS SET  TO STARTS*/);
       // setData(mPickerViewM, 3, 10, 3);
    }

    private void setData(NumberPickerView picker, int minValue, int maxValue, int value){
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.booking_submit_selection:
                String h = mPickerViewH.getContentByCurrValue();
                String m = mPickerViewM.getContentByCurrValue();
                Toast.makeText(getApplicationContext(),h + getString(R.string.hour_hint) + " "
                        + m + getString(R.string.minute_hint), Toast.LENGTH_LONG).show();
            break;
        }
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        //handles the first picker position updates
        if (Looper.myLooper() == Looper.getMainLooper()) {
            selectedDateTextView.setText(String.valueOf(newVal));
        }else{
            //handles the second picker position updates
            Message message = Message.obtain();
            message.obj =  String.valueOf(newVal);
            mHandler.sendMessage(message);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_reservation) {

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