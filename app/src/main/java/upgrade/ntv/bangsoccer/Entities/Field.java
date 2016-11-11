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
            name,
            phone,
            schedule,
            story;

    private Double
            latitude,
            longitude;

    public Field() {
    }

    public Field(String URL, String address, String imageURL, Double latitude, Double longitude, String name, String phone, String schedule, String story) {
        this.URL = URL;
        this.address = address;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.phone = phone;
        this.schedule = schedule;
        this.story = story;
    }

    public String getFIELD_ID() {
        return FIELD_ID;
    }

    public void setFIELD_ID(String FIELD_ID) {
        this.FIELD_ID = FIELD_ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
