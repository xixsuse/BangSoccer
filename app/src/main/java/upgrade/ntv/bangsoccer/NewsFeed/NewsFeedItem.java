package upgrade.ntv.bangsoccer.NewsFeed;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Paulino on 10/9/2015.
 */
public class NewsFeedItem implements Parcelable {

    public int image, newsID;
    public String tittle;
    public String description;

    public NewsFeedItem(int image, String tittle) {
        this.image = image;
        this.tittle = tittle;
    }

    public NewsFeedItem(int image, String tittle, String description) {
        this.image = image;
        this.tittle = tittle;
        this.description = description;
    }

    protected NewsFeedItem(Parcel in) {
        image = in.readInt();
        newsID = in.readInt();
        tittle = in.readString();
        description = in.readString();
    }

    public static final Creator<NewsFeedItem> CREATOR = new Creator<NewsFeedItem>() {
        @Override
        public NewsFeedItem createFromParcel(Parcel in) {
            return new NewsFeedItem(in);
        }

        @Override
        public NewsFeedItem[] newArray(int size) {
            return new NewsFeedItem[size];
        }
    };

    public int getNewsID() {
        return newsID;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }

    public int getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeInt(newsID);
        dest.writeString(tittle);
        dest.writeString(description);
    }
}
