package upgrade.ntv.bangsoccer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.RecyclerItemClickLister;


public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private List<NewsFeedItem> newsFeedItems;
    private Context context;
    static private ClickListener clickListener;

    public NewsFeedAdapter(List<NewsFeedItem> newsFeedItems, Context context){
        this.newsFeedItems = newsFeedItems;
        this.context = context;
    }


    public void setClickListener(ClickListener clickListener){
        this.clickListener= clickListener;
    }

    @Override
    public int getItemCount() {
        return newsFeedItems.size();
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_newsfeed, parent, false);
        // set the view's size, margins, paddings and app_bar_teams parameters

        return new NewsFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsFeedViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        NewsFeedItem newsFeedItem = newsFeedItems.get(position);

        holder.vImage.setImageBitmap(newsFeedItem.image);
        // holder.vImage.setImageResource(newsFeedItem.image);
        holder.vTitleTextView.setText(newsFeedItem.tittle);
        holder.vClubNameTextView.setText(newsFeedItem.user);
        holder.ID =position;

        if(newsFeedItem.like){
            holder.vLike.setImageResource(R.drawable.ic_like_selected);
        }

        else{
            holder.vLike.setImageResource(R.drawable.ic_like_not_selected);
        }
    }


    public interface ClickListener{
        public void itemClicked(View view, int position);
    }


    // Provides a reference to the views for each data item
    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        protected ImageView vImage;
        protected TextView vTitleTextView;
        protected TextView vClubNameTextView;
        protected int ID;
        protected ImageView vLike;
        protected ImageView vShare;


        public NewsFeedViewHolder(View v){
            super(v);
            vImage = (ImageView) v.findViewById(R.id.newsfeed_imageView);
            vTitleTextView = (TextView) v.findViewById(R.id.newsfeed_text_news_title);
            vClubNameTextView = (TextView) v.findViewById(R.id.newsfeed_facebook_club_name);
            vLike = (ImageView) v.findViewById(R.id.newsfeed_likes_button);
            vShare = (ImageView) v.findViewById(R.id.newsfeed_share);

            vImage.setOnClickListener(this);
            vTitleTextView.setOnClickListener(this);
            vClubNameTextView.setOnClickListener(this);
            vLike.setOnClickListener(this);
            vShare.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {

            if(clickListener!=null){
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }
    }
