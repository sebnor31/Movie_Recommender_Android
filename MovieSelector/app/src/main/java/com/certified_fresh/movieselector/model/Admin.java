package com.certified_fresh.movieselector.model;

import com.certified_fresh.movieselector.model.User;

import java.util.List;

/**
 * Created by nsebkhi3 on 2/11/2016.
 */
public class Admin extends User {

    public Admin(List<String> args) {
        super(args);
    }

    public Admin (String firstName, String lastName, String username, String password, String email, String type) {
        super(firstName,lastName,username, password, email, type);
    }

}
