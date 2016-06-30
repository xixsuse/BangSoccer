package upgrade.ntv.bangsoccer.Adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import upgrade.ntv.bangsoccer.NewsFeed.OpenLeagueItem;

/**
 * Created by lopez1407 on 2/20/2016.
 */
public abstract class StickyListAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private List<OpenLeagueItem> items = new ArrayList<>();

    public StickyListAdapter() {
        setHasStableIds(true);
    }

    public void add(OpenLeagueItem elem) {
        items.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends OpenLeagueItem> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(OpenLeagueItem... items) {
        addAll(Arrays.asList(items));
    }


    public OpenLeagueItem getItem(int position) {

        return items.get(position);
    }

    public List<OpenLeagueItem> getItemList() {

        return items;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
