package upgrade.ntv.bangsoccer.TournamentObjects;

/**
 * Created by root on 8/27/16.
 */

public class Divisions {
    private String name, firebasekey;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirebasekey(String firebasekey) {
        this.firebasekey = firebasekey;
    }

    public String getName() {
        return name;
    }

    public String getFirebasekey() {
        return firebasekey;
    }


}
