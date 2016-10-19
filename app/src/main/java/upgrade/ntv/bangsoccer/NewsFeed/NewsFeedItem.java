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
    public String postID;
    public boolean like;
    public long id;

    public NewsFeedItem(long id, Bitmap image, String tittle, String user, String postID, boolean like) {
        this.id=id;
        this.image = image;
        this.tittle = tittle;
        this.user=user;
        this.postID=postID;
        this.like=like;
    }

    public  void setLike(boolean like){
        this.like=like;
    }


}
