package upgrade.ntv.bangsoccer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jfrom on 5/26/2016.
 */

public class FavoritesViewHolder extends RecyclerView.ViewHolder{
   private View FavView;
    public FavoritesViewHolder(View itemView) {
        super(itemView);
        setFavView(itemView);
    }

    public View getFavView() {
        return FavView;
    }

    public void setFavView(View favView) {
        FavView = favView;
    }
}
