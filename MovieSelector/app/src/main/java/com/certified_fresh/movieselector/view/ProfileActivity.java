package com.certified_fresh.movieselector.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.Spinner;

import com.certified_fresh.movieselector.R;
//import com.certified_fresh.movieselector.controller.UserManager;
import com.certified_fresh.movieselector.controller.UserManagerDB;
import com.certified_fresh.movieselector.model.UserDB;

public class ProfileActivity extends AppCompatActivity {

    // Handles to Views
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText email;
    EditText interests;
    EditText major;
    UserManagerDB userManager;


    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName   = (EditText) findViewById(R.id.profileFirstNameEdit);
        lastName    = (EditText) findViewById(R.id.profileLastNameEdit);
        username    = (EditText) findViewById(R.id.profileUsernameEdit);
        password    = (EditText) findViewById(R.id.profilePwdEdit);
        email       = (EditText) findViewById(R.id.profileEmailEdit);
        interests   = (EditText) findViewById(R.id.profileInterestEdit);
        major       = (EditText) findViewById(R.id.profileMajorEdit);
        userManager = new UserManagerDB(this, null);
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        UserDB user = UserManagerDB.currentUser;

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        email.setText(user.getEmail());
        interests.setText(user.getInterests());
        major.setText(user.getMajor());
    }

    /**
     * Saves info in text fields to profile
     * @param button to save info
     */
    public void onSaveClicked(View button) {
        String firstNameTxt = firstName.getText().toString();
        String lastNameTxt  = lastName.getText().toString();
        String usernameTxt  = username.getText().toString();
        String passwordTxt  = password.getText().toString();
        String emailTxt     = email.getText().toString();
        String interestsTxt = interests.getText().toString();
        String majorTxt     = major.getText().toString();

        UserDB newUser = new UserDB(usernameTxt, passwordTxt, firstNameTxt, lastNameTxt,
                emailTxt, "student", majorTxt, interestsTxt);
        userManager.update(newUser);
        userManager.currentUser = newUser;
        userManager.selectAll();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Cancels information editing
     * @param button to cancel info
     */
    public void onCancelClicked(View button) {
        finish();
    }
}
