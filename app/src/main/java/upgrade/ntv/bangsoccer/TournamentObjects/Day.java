package upgrade.ntv.bangsoccer.TournamentObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/20/16.
 */

public class Day {
    private String id, date;
    private List<Match> games = new ArrayList<Match>();

    public Day() {
    }

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

    public void addMatch(Match game) {
        this.games.add(game);
    }
    public void getMatch(int index) {
        this.games.get(index);
    }

}
