package upgrade.ntv.bangsoccer.Entities;

/**
 * Created by Paulino on 10/23/2016.
 */

public class Field {

    private String
            FIELD_ID;

    private String
            URL,
            address,
            imageURL,
            latitude,
            longitude,
            name,
            phone,
            schedule;

    public Field() {
    }

    public Field(String URL, String address, String imageURL, String latitude, String longitude, String name, String phone, String schedule) {
        this.URL = URL;
        this.address = address;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.phone = phone;
        this.schedule = schedule;
    }

    public void setFIELD_ID(String FIELD_ID) {
        this.FIELD_ID = FIELD_ID;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getFIELD_ID() {
        return FIELD_ID;
    }

    public String getURL() {
        return URL;
    }

    public String getAddress() {
        return address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSchedule() {
        return schedule;
    }
}
