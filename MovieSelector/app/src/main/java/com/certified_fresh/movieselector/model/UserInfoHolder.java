package com.certified_fresh.movieselector.model;

/**
 * Created by Aysha on 3/16/16.
 */
public class UserInfoHolder {
    private String username;
    private String locked;
    private String banned;


    public UserInfoHolder(String username, String locked, String banned) {
        this.username = username;
        this.locked = locked;
        this.banned = banned;
    }

    /**
     * Gets username
     * @return String of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets 'locked' value
     * @return String of locked
     */
    public String getLocked() {
        return locked;
    }

    /**
     * Gets 'banned' value
     * @return String of banned
     */
    public String getBanned() {
        return banned;
    }

    /**
     * Sets 'username' value
     * @param username new username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets 'locked' value
     * @param locked new locked status
     */
    public void setLocked(String locked) {
        this.locked = locked;
    }

    /**
     * Sets 'banned' value
     * @param banned new banned status
     */
    public void setBanned(String banned) {
        this.banned = banned;
    }
}
