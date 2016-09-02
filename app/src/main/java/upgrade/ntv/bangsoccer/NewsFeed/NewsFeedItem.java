package upgrade.ntv.bangsoccer.NewsFeed;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Paulino on 10/9/2015.
 * Modified by Ramon on 7/16/2016
 */
public class NewsFeedItem {

    public String tittle;
    public String user;
    public Bitmap image;

    public NewsFeedItem(Bitmap image, String tittle, String user) {
        this.image = image;
        this.tittle = tittle;
        this.user=user;
    }


}
