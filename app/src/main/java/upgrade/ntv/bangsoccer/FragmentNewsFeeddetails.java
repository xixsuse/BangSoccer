package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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

import upgrade.ntv.bangsoccer.dao.DBNewsFeed;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentNewsFeeddetails extends Fragment implements CollapsingToolbarLayout.OnClickListener {
    private DrawerLayout drawer;
    // TODO: Customize parameter argument names
    private static final String NEWS_ID = "news-id";
    private static Toolbar toolbar;
    private static Context mContext;
    private OnListFragmentInteractionListener mListener;
    private DBNewsFeed mNewsFeed;
    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvDescription;
    private ImageView ivPicture;
    private TextView tvUsername;
    private static List<DBNewsFeed> newsFeedList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentNewsFeeddetails() {
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

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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
            // mNewsFeed= AppicationCore.getNewsFeed(mNewsID+1);
            mNewsFeed = newsFeedList.get((int)(mNewsID));
        }


    }

    public static Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_news_details, container, false);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapse_toolbar);
        toolbar = (Toolbar) collapsingToolbar.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     /*   toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);*/

/*
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(newsFeedAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/

        tvDate = (TextView) view.findViewById(R.id.newsfeeddetails_news_timestamp);
        tvTitle = (TextView) view.findViewById(R.id.newsfeeddetails_news_title);
        tvDescription = (TextView) view.findViewById(R.id.newsfeeddetails_news_description);
        ivPicture = (ImageView) view.findViewById(R.id.app_barr_news_details_imageheader);
        tvUsername = (TextView) view.findViewById(R.id.newsfeeddetails_news_publisher);



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

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }


    public Bitmap bitmapFromByte(byte[] b){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return  BitmapFactory.decodeByteArray(b , 0, b .length, options);
    }
}
