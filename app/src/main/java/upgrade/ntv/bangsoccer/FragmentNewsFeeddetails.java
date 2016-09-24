package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import upgrade.ntv.bangsoccer.dao.DBNewsFeed;


public class FragmentNewsFeeddetails extends Fragment  {
    // TODO: Customize parameter argument names
    private static final String NEWS_ID = "news-id";
    private Toolbar toolbar;
    private OnListFragmentInteractionListener mListener;

    private DBNewsFeed mNewsFeed;
    @BindView(R.id.newsfeeddetails_news_timestamp)
    TextView tvDate;
    @BindView(R.id.newsfeeddetails_news_title)
    TextView tvTitle;
    @BindView(R.id.newsfeeddetails_news_description)
    TextView tvDescription;
    @BindView(R.id.app_barr_news_details_imageheader)
    ImageView ivPicture;
    @BindView(R.id.newsfeeddetails_news_publisher)
    TextView tvUsername;

    private static List<DBNewsFeed> newsFeedList;

    public FragmentNewsFeeddetails() {
    }

    public static FragmentNewsFeeddetails newInstance(int newId) {
        FragmentNewsFeeddetails fragment = new FragmentNewsFeeddetails();
        Bundle args = new Bundle();
        args.putInt(NEWS_ID, newId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long mNewsID = getArguments().getInt(NEWS_ID);
            mNewsFeed = newsFeedList.get((int)(mNewsID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_news_details, container, false);
        //bind layout
        ButterKnife.bind(this, view);
        //declares the toolbar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //enables the toolbar home button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // terminates the activity when the home button is pressed
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        if(mNewsFeed!=null) {

            String story = mNewsFeed.getStory();
            if(story!=null && story.length()>1)
                tvTitle.setText(story);
            else
                tvTitle.setText("Publicación");

            tvUsername.setText(mNewsFeed.getUserName());
            tvDate.setText(mNewsFeed.getDate());
            tvDescription.setText(mNewsFeed.getMessage());

            Bitmap bm = bitmapFromByte(mNewsFeed.getPicture());
            ivPicture.setImageBitmap(bm);
            bm=null;

        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }

    public static List<DBNewsFeed> getNewsfeedList(){
        return newsFeedList;
    }

    public static void updatingNewsfeedList(){
        List<DBNewsFeed> temp = AppicationCore.getAllNewsFeed();

        if(temp!=null && temp.size()>0) {

            if(newsFeedList!= null)
                newsFeedList.clear();
            else
                newsFeedList= new ArrayList<>();

            for (int i = temp.size() - 1; i > -1; i--) {
                newsFeedList.add(temp.get(i));
            }
        }
    }

    public Bitmap bitmapFromByte(byte[] b){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return  BitmapFactory.decodeByteArray(b , 0, b .length, options);
    }
}
