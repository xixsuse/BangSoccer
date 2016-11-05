package upgrade.ntv.bangsoccer.Entities;

/**
 * Created by root on 11/3/16.
 */

public class StatisticTable {

    private String ID="id", name="name", imageurl="image";
    private int pj, pe, pg, pp, dg, gc, gf, points;

    public StatisticTable() {
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getPj() {
        return pj;
    }

    public void setPj(int pj) {
        this.pj = pj;
    }

    public int getPe() {
        return pe;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getDg() {
        return dg;
    }

    public void setDg(int dg) {
        this.dg = dg;
    }

    public int getGc() {
        return gc;
    }

    public void setGc(int gc) {
        this.gc = gc;
    }

    public int getGf() {
        return gf;
    }

    public void setGf(int gf) {
        this.gf = gf;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    @Override
    public String toString() {
        return "StatisticTable{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", pj=" + pj +
                ", pe=" + pe +
                ", pg=" + pg +
                ", pp=" + pp +
                ", dg=" + dg +
                ", gc=" + gc +
                ", gf=" + gf +
                ", points=" + points +
                '}';
    }
}

