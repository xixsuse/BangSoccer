package upgrade.ntv.bangsoccer.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import upgrade.ntv.bangsoccer.dao.DBPlayer;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBPLAYER".
*/
public class DBPlayerDao extends AbstractDao<DBPlayer, Long> {

    public static final String TABLENAME = "DBPLAYER";

    /**
     * Properties of entity DBPlayer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FirstName = new Property(1, String.class, "FirstName", false, "FIRST_NAME");
        public final static Property LastName = new Property(2, String.class, "LastName", false, "LAST_NAME");
        public final static Property Alias = new Property(3, String.class, "Alias", false, "ALIAS");
        public final static Property Gender = new Property(4, String.class, "Gender", false, "GENDER");
        public final static Property Nationality = new Property(5, String.class, "Nationality", false, "NATIONALITY");
        public final static Property Number = new Property(6, String.class, "Number", false, "NUMBER");
        public final static Property Position = new Property(7, String.class, "Position", false, "POSITION");
        public final static Property Email = new Property(8, String.class, "Email", false, "EMAIL");
        public final static Property BirthDate = new Property(9, java.util.Date.class, "BirthDate", false, "BIRTH_DATE");
        public final static Property Height = new Property(10, String.class, "Height", false, "HEIGHT");
        public final static Property Weight = new Property(11, String.class, "Weight", false, "WEIGHT");
        public final static Property Feet = new Property(12, String.class, "Feet", false, "FEET");
        public final static Property Active = new Property(13, boolean.class, "Active", false, "ACTIVE");
        public final static Property Avatar = new Property(14, byte[].class, "Avatar", false, "AVATAR");
        public final static Property TeamID = new Property(15, long.class, "TeamID", false, "TEAM_ID");
    };

    private DaoSession daoSession;


    public DBPlayerDao(DaoConfig config) {
        super(config);
    }
    
    public DBPlayerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBPLAYER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FIRST_NAME\" TEXT NOT NULL ," + // 1: FirstName
                "\"LAST_NAME\" TEXT," + // 2: LastName
                "\"ALIAS\" TEXT," + // 3: Alias
                "\"GENDER\" TEXT," + // 4: Gender
                "\"NATIONALITY\" TEXT," + // 5: Nationality
                "\"NUMBER\" TEXT," + // 6: Number
                "\"POSITION\" TEXT," + // 7: Position
                "\"EMAIL\" TEXT," + // 8: Email
                "\"BIRTH_DATE\" INTEGER," + // 9: BirthDate
                "\"HEIGHT\" TEXT," + // 10: Height
                "\"WEIGHT\" TEXT," + // 11: Weight
                "\"FEET\" TEXT," + // 12: Feet
                "\"ACTIVE\" INTEGER NOT NULL ," + // 13: Active
                "\"AVATAR\" BLOB," + // 14: Avatar
                "\"TEAM_ID\" INTEGER NOT NULL );"); // 15: TeamID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBPLAYER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBPlayer entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFirstName());
 
        String LastName = entity.getLastName();
        if (LastName != null) {
            stmt.bindString(3, LastName);
        }
 
        String Alias = entity.getAlias();
        if (Alias != null) {
            stmt.bindString(4, Alias);
        }
 
        String Gender = entity.getGender();
        if (Gender != null) {
            stmt.bindString(5, Gender);
        }
 
        String Nationality = entity.getNationality();
        if (Nationality != null) {
            stmt.bindString(6, Nationality);
        }
 
        String Number = entity.getNumber();
        if (Number != null) {
            stmt.bindString(7, Number);
        }
 
        String Position = entity.getPosition();
        if (Position != null) {
            stmt.bindString(8, Position);
        }
 
        String Email = entity.getEmail();
        if (Email != null) {
            stmt.bindString(9, Email);
        }
 
        java.util.Date BirthDate = entity.getBirthDate();
        if (BirthDate != null) {
            stmt.bindLong(10, BirthDate.getTime());
        }
 
        String Height = entity.getHeight();
        if (Height != null) {
            stmt.bindString(11, Height);
        }
 
        String Weight = entity.getWeight();
        if (Weight != null) {
            stmt.bindString(12, Weight);
        }
 
        String Feet = entity.getFeet();
        if (Feet != null) {
            stmt.bindString(13, Feet);
        }
        stmt.bindLong(14, entity.getActive() ? 1L: 0L);
 
        byte[] Avatar = entity.getAvatar();
        if (Avatar != null) {
            stmt.bindBlob(15, Avatar);
        }
        stmt.bindLong(16, entity.getTeamID());
    }

    @Override
    protected void attachEntity(DBPlayer entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBPlayer readEntity(Cursor cursor, int offset) {
        DBPlayer entity = new DBPlayer( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // FirstName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // LastName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Alias
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Gender
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Nationality
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Number
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Position
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Email
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // BirthDate
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // Height
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Weight
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Feet
            cursor.getShort(offset + 13) != 0, // Active
            cursor.isNull(offset + 14) ? null : cursor.getBlob(offset + 14), // Avatar
            cursor.getLong(offset + 15) // TeamID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBPlayer entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFirstName(cursor.getString(offset + 1));
        entity.setLastName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAlias(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGender(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNationality(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNumber(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPosition(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setEmail(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBirthDate(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setHeight(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWeight(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFeet(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setActive(cursor.getShort(offset + 13) != 0);
        entity.setAvatar(cursor.isNull(offset + 14) ? null : cursor.getBlob(offset + 14));
        entity.setTeamID(cursor.getLong(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBPlayer entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBPlayer entity) {
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
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDBTeamDao().getAllColumns());
            builder.append(" FROM DBPLAYER T");
            builder.append(" LEFT JOIN DBTEAM T0 ON T.\"TEAM_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DBPlayer loadCurrentDeep(Cursor cursor, boolean lock) {
        DBPlayer entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DBTeam team = loadCurrentOther(daoSession.getDBTeamDao(), cursor, offset);
         if(team != null) {
            entity.setTeam(team);
        }

        return entity;    
    }

    public DBPlayer loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DBPlayer> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DBPlayer> list = new ArrayList<DBPlayer>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DBPlayer> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DBPlayer> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
