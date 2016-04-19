package com.certified_fresh.movieselector.model;

import java.util.List;

/**
 * Created by nsebkhi3 on 2/10/2016.
 */
public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String type;    // s = student, a = admin

    public User(List<String> args) {
        this(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
    }

    public User(String firstName, String lastName, String username, String password, String email, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    public void updateUser(List<String> args) {
        this.firstName = args.get(0);
        this.lastName = args.get(1);
        this.username = args.get(2);
        this.password = args.get(3);
        this.email = args.get(4);
        this.type = args.get(5);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

}
