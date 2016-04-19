package com.certified_fresh.movieselector.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.certified_fresh.movieselector.model.RatingDB;
import com.certified_fresh.movieselector.model.UserDB;

import java.util.ArrayList;

/**
 * Created by Ryan on 3/1.
 */
public class RatingManagerDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "certified_fresh.db";

    public static final String TABLE_RATINGS = "ratings";
    public static final String RATINGS_ID = "id";
    public static final String RATINGS_MOVIEID = "movieid";
    public static final String RATINGS_USERNAME = "username";
    public static final String RATINGS_COMMENT = "comment";
    public static final String RATINGS_SCORE = "score";
    public static final String RATINGS_CREATEDDATE = "createddate";


    // Creating the Database Handler
    public RatingManagerDB(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        onCreate(getWritableDatabase());
    }


    /**
     * Whenever table created first time creates table here:
     * @param db database of rating
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query for SQL, see SQL documentation on CREATE TABLE
        String create_ratings_table = "CREATE TABLE IF NOT EXISTS " + TABLE_RATINGS + "(" +
                RATINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RATINGS_MOVIEID + " VARCHAR(15) NOT NULL, " +
                RATINGS_USERNAME + " VARCHAR(15) NOT NULL, " +
                RATINGS_COMMENT + " VARCHAR(255), " +
                RATINGS_SCORE + " INTEGER NOT NULL, " +
                RATINGS_CREATEDDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + RATINGS_USERNAME + ") REFERENCES " + UserManagerDB.TABLE_USERS + "(" +
                UserManagerDB.USERS_USERNAME + "));";
        db.execSQL(create_ratings_table);
        db.close();

    }

    /**
     * Whenever upgrading version, calls this:
     * @param db database of rating
     * @param oldVersion old version of table
     * @param newVersion new version of table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        onCreate(db); // creates new database, or upgrades if that's the case
    }

    /**
     * Retrieves recommendations for user
     * @param user the user responsible for the rating
     * @return ArrayList of string output for recommendation
     */
    public ArrayList<String> recommend(UserDB user) {
        String query = "SELECT AVG("+TABLE_RATINGS+"."+RATINGS_SCORE+") as  average, "+TABLE_RATINGS+
                "."+RATINGS_MOVIEID+" FROM "+TABLE_RATINGS+" left join "+UserManagerDB.TABLE_USERS+
                " ON "+UserManagerDB.TABLE_USERS+"."+UserManagerDB.USERS_USERNAME+"="+TABLE_RATINGS+
                "."+RATINGS_USERNAME+" GROUP BY "+TABLE_RATINGS+"."+RATINGS_MOVIEID+" HAVING "+
                UserManagerDB.TABLE_USERS+"."+UserManagerDB.USERS_MAJOR+"='"+user.getMajor()+"'"+
                " ORDER BY average DESC LIMIT 10";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        result.moveToFirst();
        Log.i("Recommendations", DatabaseUtils.dumpCursorToString(result));
        ArrayList<String> out = new ArrayList<String>();
        while (!result.isAfterLast()) {
            out.add(result.getString(result.getColumnIndex(RATINGS_MOVIEID)));
            result.moveToNext();
        }
        db.close();
        Log.i("Recommendations", out.toString());
        return out;
    }
//SELECT DISTINCT MOVIEID FROM RATINGS LEFT JOIN USERS ON USERNAME = USERNAME WHERE MAJOR = CS
//SELECT AVG(SCORE) as average, movieid from ratings left join users on username = username Group by movieid having major = CS order by average limit 10;

    /**
     * Returns average rating value from database for particular movie
     * @param movieid the id tag of the movie
     * @return double value representing average rating
     */
    public double getAverageRatingByMovieId(String movieid){
        String query = "SELECT AVG(" + RATINGS_SCORE + ") AS average FROM " + TABLE_RATINGS +
                " WHERE "+RATINGS_MOVIEID+" = '"+movieid+"'" +";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        result.moveToFirst();
        Log.i("AVG", DatabaseUtils.dumpCursorToString(result));
        Log.i("AVG", movieid);
        db.close();
        try {
            return result.getDouble(result.getColumnIndex("average"));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieves a user's rating for a movie
     * @param user the user responsible for the rating
     * @param movieid the id tag of the movie
     * @return int value -1 if not successful
     */
    public int getUserRating(UserDB user, String movieid) {
        String query = "SELECT " + RATINGS_SCORE + " FROM " + TABLE_RATINGS + " WHERE " +
                RATINGS_USERNAME + "='" + UserManagerDB.currentUser.getUsername() + "' AND " +
                RATINGS_MOVIEID + "='" + movieid + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        result.moveToFirst();
        db.close();
        try {
            return result.getInt(0);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Formats the ratings information for each movie
     * @param movieid the id tag of the movie that's being rated
     * @return String of rating information
     */
    public String getRatingsFormatted(String movieid) {
        String query = "SELECT " + RATINGS_USERNAME + "," + RATINGS_COMMENT + "," + RATINGS_SCORE +
                "," + RATINGS_CREATEDDATE + " FROM " + TABLE_RATINGS +
                " WHERE " + RATINGS_MOVIEID + "='" + movieid + "';";
        String[] args = new String[]{RATINGS_USERNAME, RATINGS_COMMENT, RATINGS_SCORE,
                RATINGS_CREATEDDATE, TABLE_RATINGS, RATINGS_MOVIEID, movieid};
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        result.moveToFirst();
        String out = "~~~~~~~~~~~~~~~~Ratings~~~~~~~~~~~~~~~~\n";
        while (!result.isAfterLast()) {
            out += result.getString(result.getColumnIndex(RATINGS_USERNAME)) +
                    " gave this movie " +
                    result.getString(result.getColumnIndex(RATINGS_SCORE)) + " stars on " +
                    result.getString(result.getColumnIndex(RATINGS_CREATEDDATE)) + ":\n\t\t" +
                    result.getString(result.getColumnIndex(RATINGS_COMMENT)) + "\n\n";
            result.moveToNext();
        }
        db.close();
        return out;
    }

    /**
     * Add a new rating to the database
     * @param rating a data holder of rating attributes
     * @return boolean value
     */
    public boolean add(RatingDB rating) {
        ContentValues values = new ContentValues();
        values.put(RATINGS_MOVIEID, rating.getMovieid());
        values.put(RATINGS_USERNAME, rating.getUsername());
        values.put(RATINGS_COMMENT, rating.getComment());
        values.put(RATINGS_SCORE, rating.getScore());
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_RATINGS, null, values);
        db.close();
        return result != -1;
    }

    /**
     * Remove an old rating from the database
     * @param rating a data holder of rating attributes
     * @return boolean value
     */
    public boolean remove(RatingDB rating) {
        SQLiteDatabase db = getWritableDatabase();
        String filter = RATINGS_USERNAME + "='" + rating.getUsername() + "' AND " +
                RATINGS_MOVIEID + "='" + rating.getMovieid() + "'";
        int result = db.delete(TABLE_RATINGS, filter, null);
        db.close();
        return result > 0;
    }

    /**
     * Update existing rating in the database
     * @param rating a data holder of rating attributes
     * @return boolean value if updated
     */
    public boolean update(RatingDB rating) {
        ContentValues values = new ContentValues();
        values.put(RATINGS_MOVIEID, rating.getMovieid());
        values.put(RATINGS_USERNAME, rating.getUsername());
        values.put(RATINGS_COMMENT, rating.getComment());
        values.put(RATINGS_SCORE, rating.getScore());
        SQLiteDatabase db = getWritableDatabase();
        String filter = RATINGS_USERNAME + "='" + rating.getUsername() + "' AND " +
                RATINGS_MOVIEID + "='" + rating.getMovieid() + "'";
        int result = db.update(TABLE_RATINGS, values, filter, null);
        db.close();
        return result > 0;
    }

    /**
     * Selects all ratings in the database
     */
    public void selectAll() {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("ENTIRE DB", DatabaseUtils.dumpCursorToString(db.rawQuery("SELECT * FROM ratings;", null)));
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.close();
    }
}