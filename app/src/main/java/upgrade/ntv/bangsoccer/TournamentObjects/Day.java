package upgrade.ntv.bangsoccer.TournamentObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 8/20/16.
 */

public class Day implements Parcelable{
    private String id, date;
    private Map<String,Match> games = new HashMap<>();

    public Day() {
    }

    protected Day(Parcel in) {
        id = in.readString();
        date = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String,Match> getGames() {
        return games;
    }

    public void setGames(Map<String, Match> games) {
        this.games = games;
    }

    public void addMatch(String key, Match game) {
        this.games.put(key,game);
    }
    public void getMatch(int index) {
        this.games.get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(date);
    }
}
