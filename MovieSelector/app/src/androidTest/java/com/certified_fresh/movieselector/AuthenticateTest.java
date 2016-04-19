package com.certified_fresh.movieselector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.certified_fresh.movieselector.controller.UserManagerDB;
import com.certified_fresh.movieselector.model.UserDB;

/**
 * Created by Nordine Sebkhi on 4/3/2016.
 */
@RunWith(AndroidJUnit4.class)

public class AuthenticateTest extends InstrumentationTestCase {

    UserManagerDB users;

    @Before
    public void setUserManager() {

        users = new UserManagerDB(InstrumentationRegistry.getTargetContext(), null);

        /* Add a Student (unlocked, unbanned) */
        String firstName = "Nordine";
        String lastName = "Sebkhi";
        String email = "sebnor31@gatech.edu";
        String username = "sebnor";
        String password = "abcd";
        String type = "s";
        String major = "ECE";
        String interests = "Java coding";
        int locked = 0;
        int banned = 0;

        UserDB student = new UserDB(username, password, firstName, lastName, email, type, major, interests, locked, banned);
        users.add(student);

        /* Add a Locked Student */
        UserDB lockedStudent = new UserDB("lockedUser", password, firstName, lastName, email, "student", major, interests, 4, 0);
        users.add(lockedStudent);

        /* Add a Banned Student */
        UserDB bannedStudent = new UserDB("bannedUser", password, firstName, lastName, email, "student", major, interests, 0, 1);
        users.add(bannedStudent);

        /* Add an Admin */
        firstName = "Bob";
        lastName = "Walters";
        email = "bob@gatech.edu";
        username = "bobW";
        password = "android";

        UserDB admin = new UserDB(username, password, firstName, lastName, email, "admin", null, null);
        users.add(admin);
    }

    @Test
    public void studentAuthenticate() {
        // Successful Student authentication
        int successLogin = users.authenticate("sebnor", "abcd");
        assertEquals(1, successLogin);

        // unsuccessful Student authentication
        int failLogin = users.authenticate("sebnor", "a");
        assertEquals(0, failLogin);

        // Locked student
        int lockedLogin = users.authenticate("lockedUser", "abcd");
        assertEquals(3, lockedLogin);

        // Banned student
        int bannedLogin = users.authenticate("bannedUser", "abcd");
        assertEquals(4, bannedLogin);
    }

    @Test
    public void adminAuthenticate() {
        // Successful Admin authentication
        int successLogin = users.authenticate("bobW", "android");
        assertEquals(2, successLogin);

        // unsuccessful Admin authentication
        int failLogin = users.authenticate("bobW", "iOS");
        assertEquals(0, failLogin);
    }

}
