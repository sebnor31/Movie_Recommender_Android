package com.certified_fresh.movieselector.model;

/**
 * Created by Ryan on 3/1.
 */
public class RatingDB {
    private String movieid;
    private String username;
    private String comment;
    private int score;

    public RatingDB(String movieid, UserDB user, String comment, int score) {
        this.movieid = movieid;
        this.username = user.getUsername();
        this.comment = comment;
        this.score = score;
    }

    /**
     * Gets 'movieid' value
     * @return String of movieid
     */
    public String getMovieid() {
        return movieid;
    }

    /**
     * Gets 'username' value
     * @return String of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets 'comment' value
     * @return String of comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets 'score' value
     * @return String of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets 'movieid' value
     * @param movieid new movieid
     */
    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    /**
     * Sets 'username' value
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets 'comment' value
     * @param comment new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets 'score' value
     * @param score new score
     */
    public void setScore(int score) {
        this.score = score;
    }
}
