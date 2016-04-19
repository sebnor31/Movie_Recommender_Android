package com.certified_fresh.movieselector.model;

/**
 * Created by Ryan on 3/1.
 */
public class UserDB {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String type;
    private String major;
    private String interests;
    private int locked;
    private int banned;

    public UserDB(String username, String password, String firstName, String lastName, String email,
                  String type, String major, String interests) {
        this(username, password, firstName, lastName, email, type, major, interests, 0, 0);
    }

    public UserDB(String username, String password, String firstName, String lastName, String email,
                  String type, String major, String interests, int locked, int banned) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.major = major;
        this.interests = interests;
        this.locked = locked;
        this.banned = banned;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getMajor() {
        return major;
    }

    public String getInterests() {
        return interests;
    }

    public int getLocked() {return locked;}

    public int getBanned() {return banned;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

}

