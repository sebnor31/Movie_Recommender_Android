package com.certified_fresh.movieselector.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.certified_fresh.movieselector.model.UserDB;

import java.util.ArrayList;

/**
 * Created by Ryan on 3/1.
 */
public class UserManagerDB extends SQLiteOpenHelper {
    public static UserDB currentUser;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "certified_fresh.db";

    public static final String TABLE_USERS = "users";
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_FIRSTNAME = "firstname";
    public static final String USERS_LASTNAME = "lastname";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_TYPE = "type";
    public static final String USERS_MAJOR = "major";
    public static final String USERS_INTERESTS = "interests";
    public static final String USERS_LOCKED = "locked";
    public static final String USERS_BANNED = "banned";
    public static final String USERS_CREATEDDATE = "createddate";

    // Creating the Database Handler
    public UserManagerDB(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        onCreate(getWritableDatabase());
    }

    // Whenever table created first time creates table here:
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query for SQL, see SQL documentation on CREATE TABLE
        String create_users_table = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                USERS_USERNAME + " VARCHAR(15) PRIMARY KEY, " +
                USERS_PASSWORD + " VARCHAR(15) NOT NULL, " +
                USERS_FIRSTNAME + " VARCHAR(31) NOT NULL, " +
                USERS_LASTNAME + " VARCHAR(31) NOT NULL, " +
                USERS_EMAIL + " VARCHAR(63) NOT NULL, " +
                USERS_TYPE + " VARCHAR(15) DEFAULT 'S', " +
                USERS_MAJOR + " VARCHAR(4), " +
                USERS_INTERESTS + " VARCHAR(255), " +
                USERS_LOCKED + " INTEGER DEFAULT 0, " +
                USERS_BANNED + " INTEGER DEFAULT 0, " +
                USERS_CREATEDDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                ");";
        db.execSQL(create_users_table);

    }

    // Whenever upgrading version, calls this:
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS); // deletes current table
        onCreate(db); // creates new database, or upgrades if that's the case
    }

    public void recreateDB() {
        onUpgrade(getWritableDatabase(), 0, 1);
    }

    public boolean add(UserDB user) {
        ContentValues values = new ContentValues(); // puts together all data in one structure
        values.put(USERS_USERNAME, user.getUsername());
        values.put(USERS_PASSWORD, user.getPassword());
        values.put(USERS_FIRSTNAME, user.getFirstName());
        values.put(USERS_LASTNAME, user.getLastName());
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_TYPE, user.getType());
        values.put(USERS_MAJOR, user.getMajor());
        values.put(USERS_INTERESTS, user.getInterests());
        values.put(USERS_LOCKED, user.getLocked());
        values.put(USERS_BANNED, user.getBanned());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_USERS, null, values);
        db.close(); // closes database, memory savings
        return result != -1; // returns true if user is successfully added, else false
    }

    public boolean remove(UserDB user) {
        SQLiteDatabase db = getWritableDatabase();
        String filter = USERS_USERNAME + "='" + user.getUsername() + "'";
        int result = db.delete(TABLE_USERS, filter, null);
        return result > 0;
    }

    public boolean update(UserDB user) {
        ContentValues values = new ContentValues(); // puts together all data in one structure
        values.put(USERS_USERNAME, user.getUsername());
        values.put(USERS_PASSWORD, user.getPassword());
        values.put(USERS_FIRSTNAME, user.getFirstName());
        values.put(USERS_LASTNAME, user.getLastName());
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_TYPE, user.getType());
        values.put(USERS_MAJOR, user.getMajor());
        values.put(USERS_INTERESTS, user.getInterests());
        values.put(USERS_LOCKED, user.getLocked());
        values.put(USERS_BANNED, user.getBanned());

        SQLiteDatabase db = getWritableDatabase();
        String filter = USERS_USERNAME + "='" + user.getUsername() + "'";
        int result = db.update(TABLE_USERS, values, filter, null);
        db.close();
        return result > 0;
    }

    public int authenticate(String username, String password) {
        String authQuery = "SELECT * FROM " +  TABLE_USERS + " WHERE " +
                USERS_USERNAME + "='" + username + "' AND " +
                USERS_PASSWORD + "='" + password + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(authQuery, null);
        result.moveToFirst();

        if (result.getCount() == 0) {
            incrementLock(username);
            result.close();
            return 0;

        } else {
            result.moveToFirst();
            int usernameIndex = result.getColumnIndex(USERS_USERNAME);
            int passwordIndex = result.getColumnIndex(USERS_PASSWORD);
            int firstIndex = result.getColumnIndex(USERS_FIRSTNAME);
            int lastIndex = result.getColumnIndex(USERS_LASTNAME);
            int emailIndex = result.getColumnIndex(USERS_EMAIL);
            int typeIndex = result.getColumnIndex(USERS_TYPE);
            int majorIndex = result.getColumnIndex(USERS_MAJOR);
            int interestsIndex = result.getColumnIndex(USERS_INTERESTS);
            int lockedIndex = result.getColumnIndex(USERS_LOCKED);
            int bannedIndex = result.getColumnIndex(USERS_BANNED);

            if (result.getString(typeIndex).equals("admin")) {

                currentUser = new UserDB(result.getString(usernameIndex),
                        result.getString(passwordIndex), result.getString(firstIndex),
                        result.getString(lastIndex), result.getString(emailIndex),
                        result.getString(typeIndex), result.getString(majorIndex),
                        result.getString(interestsIndex));
                return 2;
            }

            else if (result.getInt(lockedIndex) >= 3) {
                return 3;
            }

            else if (result.getInt(bannedIndex) == 1) {
                return 4;
            }

            // Create a new student
            currentUser = new UserDB(result.getString(usernameIndex),
                    result.getString(passwordIndex), result.getString(firstIndex),
                    result.getString(lastIndex), result.getString(emailIndex),
                    result.getString(typeIndex), result.getString(majorIndex),
                    result.getString(interestsIndex));

            // Student entered correct password and num of login attempt is less than 3
            setLock(username, 0);

            return 1;
        }
    }
    public void selectAll() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM users;", null);
        Log.d("TEST", DatabaseUtils.dumpCursorToString(result));
    }
    public void dropStudents() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE not " + USERS_TYPE + "='admin';");
    }

    /**
     * Increment the number of login attempt if the username exists
     * @param username of users in database
     */
    private void incrementLock(String username) {
        String userQuery = "SELECT " + USERS_LOCKED + " FROM " +  TABLE_USERS + " WHERE " +
                USERS_USERNAME + "='" + username + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(userQuery, null);
        result.moveToFirst();

        // Case this username does not exist
        if (result.getCount() == 0) {
            return;
        }

        int previousLock = result.getInt(result.getColumnIndex(USERS_LOCKED));
        setLock(username, previousLock + 1);
    }

    /**
     * Reset the lock counter to 0
     * @param username of users in database
     */
    private void setLock(String username, int lock) {
        String update = "UPDATE " + TABLE_USERS + " SET " + USERS_LOCKED + "=" + lock + " WHERE " +
                USERS_USERNAME + "='" + username + "';";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(update);
    }

    private void setBan(String username, int ban) {
        String update = "UPDATE " + TABLE_USERS + " SET " + USERS_BANNED + "=" + ban + " WHERE " +
                USERS_USERNAME + "='" + username + "';";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(update);
    }

    public void setBan(String username, boolean banned) {
        setBan(username, banned ? 1 : 0);
    }

    public void setLock(String username, boolean locked) {
        setLock(username, locked ? 3 : 0);
    }

    public ArrayList<UserDB> getUsersList() {
        String getUsersQuery = "SELECT * FROM " + TABLE_USERS + " WHERE not " + USERS_TYPE +
                "='admin';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(getUsersQuery, null);
        result.moveToFirst();
        ArrayList<UserDB> out = new ArrayList<>();
        while (!result.isAfterLast()) {
            out.add(new UserDB(result.getString(result.getColumnIndex(USERS_USERNAME)),
                    result.getString(result.getColumnIndex(USERS_PASSWORD)),
                    result.getString(result.getColumnIndex(USERS_FIRSTNAME)),
                    result.getString(result.getColumnIndex(USERS_LASTNAME)),
                    result.getString(result.getColumnIndex(USERS_EMAIL)),
                    result.getString(result.getColumnIndex(USERS_TYPE)),
                    result.getString(result.getColumnIndex(USERS_MAJOR)),
                    result.getString(result.getColumnIndex(USERS_INTERESTS)),
                    result.getInt(result.getColumnIndex(USERS_LOCKED)),
                    result.getInt(result.getColumnIndex(USERS_BANNED))));
            result.moveToNext();
        }
        db.close();
        return out;
    }
}
