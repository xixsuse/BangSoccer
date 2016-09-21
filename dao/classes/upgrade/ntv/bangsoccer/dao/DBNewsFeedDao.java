package upgrade.ntv.bangsoccer.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import upgrade.ntv.bangsoccer.dao.DBNewsFeed;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBNEWS_FEED".
*/
public class DBNewsFeedDao extends AbstractDao<DBNewsFeed, Long> {

    public static final String TABLENAME = "DBNEWS_FEED";

    /**
     * Properties of entity DBNewsFeed.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PostID = new Property(1, String.class, "PostID", false, "POST_ID");
        public final static Property UserName = new Property(2, String.class, "UserName", false, "USER_NAME");
        public final static Property Message = new Property(3, String.class, "Message", false, "MESSAGE");
        public final static Property Story = new Property(4, String.class, "Story", false, "STORY");
        public final static Property Picture = new Property(5, byte[].class, "Picture", false, "PICTURE");
        public final static Property Date = new Property(6, String.class, "Date", false, "DATE");
        public final static Property Like = new Property(7, Boolean.class, "Like", false, "LIKE");
    };


    public DBNewsFeedDao(DaoConfig config) {
        super(config);
    }
    
    public DBNewsFeedDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBNEWS_FEED\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"POST_ID\" TEXT," + // 1: PostID
                "\"USER_NAME\" TEXT," + // 2: UserName
                "\"MESSAGE\" TEXT," + // 3: Message
                "\"STORY\" TEXT," + // 4: Story
                "\"PICTURE\" BLOB," + // 5: Picture
                "\"DATE\" TEXT," + // 6: Date
                "\"LIKE\" INTEGER);"); // 7: Like
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBNEWS_FEED\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBNewsFeed entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String PostID = entity.getPostID();
        if (PostID != null) {
            stmt.bindString(2, PostID);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(3, UserName);
        }
 
        String Message = entity.getMessage();
        if (Message != null) {
            stmt.bindString(4, Message);
        }
 
        String Story = entity.getStory();
        if (Story != null) {
            stmt.bindString(5, Story);
        }
 
        byte[] Picture = entity.getPicture();
        if (Picture != null) {
            stmt.bindBlob(6, Picture);
        }
 
        String Date = entity.getDate();
        if (Date != null) {
            stmt.bindString(7, Date);
        }
 
        Boolean Like = entity.getLike();
        if (Like != null) {
            stmt.bindLong(8, Like ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBNewsFeed readEntity(Cursor cursor, int offset) {
        DBNewsFeed entity = new DBNewsFeed( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // PostID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UserName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Message
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Story
            cursor.isNull(offset + 5) ? null : cursor.getBlob(offset + 5), // Picture
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Date
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0 // Like
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBNewsFeed entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPostID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMessage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStory(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPicture(cursor.isNull(offset + 5) ? null : cursor.getBlob(offset + 5));
        entity.setDate(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLike(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBNewsFeed entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBNewsFeed entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
