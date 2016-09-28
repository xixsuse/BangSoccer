package upgrade.ntv.bangsoccer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 9/25/16.
 */

public class LeadersIndex  implements Parcelable {

    private long pos = 0;
    private String playerid ="playerid", key;
    private Map<String, Map<String, LeadersIndex>> leadersIndexMap = new HashMap<>();

    public LeadersIndex() {
    }

    protected LeadersIndex(Parcel in) {
        pos = in.readInt();
        playerid = in.readString();
    }

    public static final Creator<LeadersIndex> CREATOR = new Creator<LeadersIndex>() {
        @Override
        public LeadersIndex createFromParcel(Parcel in) {
            return new LeadersIndex(in);
        }

        @Override
        public LeadersIndex[] newArray(int size) {
            return new LeadersIndex[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Map<String, LeadersIndex>>getLeadersIndexMap() {
        return leadersIndexMap;
    }

    public void setLeadersIndexMap(Map<String, Map<String, LeadersIndex>> leadersIndexMap) {
        this.leadersIndexMap = leadersIndexMap;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    @Override
    public String toString() {
        return "LeadersIndex{" +
                "pos=" + pos +
                ", playerid='" + playerid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(pos);
        parcel.writeString(playerid);
    }
}
