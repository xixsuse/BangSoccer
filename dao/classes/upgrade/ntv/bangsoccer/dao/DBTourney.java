package upgrade.ntv.bangsoccer.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DBTOURNEY".
 */
public class DBTourney {

    private Long id;
    /** Not-null value. */
    private String Name;
    private boolean Active;
    private String Location;
    private String StartDate;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public DBTourney() {
    }

    public DBTourney(Long id) {
        this.id = id;
    }

    public DBTourney(Long id, String Name, boolean Active, String Location, String StartDate) {
        this.id = id;
        this.Name = Name;
        this.Active = Active;
        this.Location = Location;
        this.StartDate = StartDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return Name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String Name) {
        this.Name = Name;
    }

    public boolean getActive() {
        return Active;
    }

    public void setActive(boolean Active) {
        this.Active = Active;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
