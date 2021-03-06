package upgrade.ntv.bangsoccer.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DBNEWS_FEED".
 */
public class DBNewsFeed {

    private Long id;
    private String PostID;
    private String UserName;
    private String Message;
    private String Story;
    private byte[] Picture;
    private String Date;
    private Boolean Like;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public DBNewsFeed() {
    }

    public DBNewsFeed(Long id) {
        this.id = id;
    }

    public DBNewsFeed(Long id, String PostID, String UserName, String Message, String Story, byte[] Picture, String Date, Boolean Like) {
        this.id = id;
        this.PostID = PostID;
        this.UserName = UserName;
        this.Message = Message;
        this.Story = Story;
        this.Picture = Picture;
        this.Date = Date;
        this.Like = Like;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String PostID) {
        this.PostID = PostID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStory() {
        return Story;
    }

    public void setStory(String Story) {
        this.Story = Story;
    }

    public byte[] getPicture() {
        return Picture;
    }

    public void setPicture(byte[] Picture) {
        this.Picture = Picture;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public Boolean getLike() {
        return Like;
    }

    public void setLike(Boolean Like) {
        this.Like = Like;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
