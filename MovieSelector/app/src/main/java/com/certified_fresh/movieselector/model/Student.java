package com.certified_fresh.movieselector.model;

import java.util.List;

/**
 * Created by nsebkhi3 on 2/10/2016.
 */
public class Student extends User {

    private String major;
    private String interest;

    public Student(List<String> args) {
        this(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));

        if (args.get(6) != null){
            major = args.get(6);
        }

        if (args.get(7) != null){
            interest = args.get(7);
        }
    }

    public Student (String firstName, String lastName, String username, String password, String email, String type) {
        super(firstName,lastName,username, password, email, type);
        major = "CS";
        interest = "none";
    }

    /**
     * Updates user values with new list values
     * @param args list of new values to update
     */
    @Override
    public void updateUser(List<String> args) {

        // Update User supclass variables
        super.updateUser(args);

        // Update student specific variable
        major = args.get(6);
        interest = args.get(7);
    }


    /**
     * Gets 'major' value
     * @return String of major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Gets 'interest' value
     * @return String of interest
     */
    public String getInterest() {
        return interest;
    }
}
