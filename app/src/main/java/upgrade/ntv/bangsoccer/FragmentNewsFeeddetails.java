package upgrade.ntv.bangsoccer;

import android.content.Context;
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

import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentNewsFeeddetails extends Fragment {
    private static NewsFeedItem  myitem;
    private NewsFeedItem currentItem;
    private DrawerLayout drawer;
    // TODO: Customize parameter argument names
    private static final String NEWS_ID = "news-id";
    private static int mNewsID;
    private Toolbar toolbar;
    private static Context mContext;
    private OnListFragmentInteractionListener mListener;
    private List<NewsFeedItem> newsFeedItems = new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentNewsFeeddetails() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentNewsFeeddetails newInstance(NewsFeedItem newsItem) {
        FragmentNewsFeeddetails fragment = new FragmentNewsFeeddetails();
        myitem=newsItem;
        Bundle args = new Bundle();
        args.putParcelable("item", myitem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentItem = getArguments().getParcelable("item");
        }


    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_news_details, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        ImageView imageViewCover = (ImageView) view.findViewById(R.id.app_barr_news_details_imageheader);
        imageViewCover.setImageResource(currentItem.getImage());
       TextView textViewTitle = (TextView) view.findViewById(R.id.newsfeeddetails_news_title);
        textViewTitle.setText(currentItem.getTittle());
        TextView textViewDescription = (TextView) view.findViewById(R.id.newsfeeddetails_news_description);
        textViewDescription.setText(currentItem.getDescription());


   /*     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
