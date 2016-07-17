package upgrade.ntv.bangsoccer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import upgrade.ntv.bangsoccer.NewsFeed.NewsFeedItem;
import upgrade.ntv.bangsoccer.R;


public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>{

    private List<NewsFeedItem> newsFeedItems;

    public NewsFeedAdapter(List<NewsFeedItem> newsFeedItems){
        this.newsFeedItems = newsFeedItems;
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
        holder.ID =position;
    }

    // Provides a reference to the views for each data item
    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder{

        protected ImageView vImage;
        protected TextView vTitleTextView;
        protected int ID;

        public NewsFeedViewHolder(View v){
            super(v);
            vImage = (ImageView) v.findViewById(R.id.newsfeed_imageView);
            vTitleTextView = (TextView) v.findViewById(R.id.newsfeed_text_news_title);

        }
    }
}