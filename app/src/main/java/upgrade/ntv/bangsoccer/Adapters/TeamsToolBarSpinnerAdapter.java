package upgrade.ntv.bangsoccer.Adapters;/*
package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.Drawer.DrawerSelector;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.RecyclerItemClickLister;
import upgrade.ntv.bangsoccer.Schedule.Team;

*/
/**
 * Created by jfrom on 4/4/2016.
 *//*




public class TeamsToolBarSpinnerAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
    private List<Team> mItems = new ArrayList<>();
    private LayoutInflater mInfalter;
    private Team team = null;
    private ThemedSpinnerAdapter.Helper mDropDownHelper;
    private Context mContext;
    private ClubsAdapter clubsAdapter;

    public TeamsToolBarSpinnerAdapter(Context context, Team team){
        mDropDownHelper =  new ThemedSpinnerAdapter.Helper(context);
        this.team = team;
        mInfalter = mDropDownHelper.getDropDownViewInflater();
        this.mContext=context;
    }

    public TeamsToolBarSpinnerAdapter(Context context){
        mDropDownHelper =  new ThemedSpinnerAdapter.Helper(context);
        mInfalter = mDropDownHelper.getDropDownViewInflater();
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(Team yourObject) {
        mItems.add(yourObject);
    }

    public void addItems(List<Team> yourObjectList) {
        mItems.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Team getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = mInfalter.inflate(R.layout.row_clubs_list, parent, false);
            view.setTag("DROPDOWN");
        }

        ImageView iView = (ImageView) view.findViewById(R.id.club_image_row);
        iView.setImageResource(getItem(position).getmTeamImage());
        //textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = mInfalter.inflate(R.layout.
                    toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }

        GridLayoutManager lLayout = new GridLayoutManager(mContext, 3);

        RecyclerView spinnerRecyclerView = (RecyclerView) view.findViewById(R.id.main_spinner_recyclerview);
        spinnerRecyclerView.setLayoutManager(lLayout);
        spinnerRecyclerView.setHasFixedSize(true);


          spinnerRecyclerView.addOnItemTouchListener(new RecyclerItemClickLister(mContext, spinnerRecyclerView, new RecyclerItemClickLister.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clubsAdapter.getItemId(position);

                Toast.makeText(mContext, String.valueOf( clubsAdapter.getItemId(position)), Toast.LENGTH_SHORT);

                */
/*Intent intent = DrawerSelector.onItemSelected(, 100);
                intent.putExtra("CLUBID", position);*//*


             */
/*   if (intent != null) {
                    mContext.startActivity(intent);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }*//*

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));


      */
/*  TextView textView = (TextView) view.findViewById(R.id.spinner_club_name);
        if(team != null) {textView.setText(team.getmName());}
        else{textView.setText("Copa Garrincha");}

        TextView textView1 = (TextView) view.findViewById(R.id.spinner_club_team);
        textView1.setText(getTitle(position));*//*

        return view;
    }

*/
/*    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
    }*//*


    @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }

    @Nullable
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return  mDropDownHelper.getDropDownViewTheme();
    }
}
*/
