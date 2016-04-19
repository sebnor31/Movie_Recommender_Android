package com.certified_fresh.movieselector.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.UserManager;
import com.certified_fresh.movieselector.model.UserDB;
import com.certified_fresh.movieselector.controller.UserManagerDB;

public class RegistrationActivity extends AppCompatActivity {

    // Handles to Views
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText email;
    EditText major;
    EditText interests;
    UserManagerDB userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstName = (EditText) findViewById(R.id.registrationFirstNameEdit);
        lastName = (EditText) findViewById(R.id.registrationLastNameEdit);
        username = (EditText) findViewById(R.id.registrationUsernameEdit);
        password = (EditText) findViewById(R.id.registrationPwdEdit);
        email = (EditText) findViewById(R.id.registrationEmailEdit);
        major = (EditText) findViewById(R.id.registrationMajorEdit);
        interests = (EditText) findViewById(R.id.registrationInterestsEdit);
        userManager = new UserManagerDB(this, null);
    }

    /**
     * Create new user selected
     * @param button inputs view of button element
     */
    public void onCreateClicked(View button) {

        String firstNameTxt = firstName.getText().toString();
        String lastNameTxt = lastName.getText().toString();
        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();
        String emailTxt = email.getText().toString();
        String majorTxt = major.getText().toString();
        String interestsTxt = interests.getText().toString();

        // Find type of user (Student or Admin)
        UserDB newUser;

        if (((RadioButton) findViewById(R.id.registrationAdminRadio)).isChecked()) {
            newUser = new UserDB(usernameTxt, passwordTxt, firstNameTxt, lastNameTxt,
                    emailTxt, "admin", null, null);
        }
        else {
            newUser = new UserDB(usernameTxt, passwordTxt, firstNameTxt, lastNameTxt,
                    emailTxt, "student", majorTxt, interestsTxt);
        }

       // Add user to data base
        if (userManager.add(newUser)) {
            // Display user message
            Context context = getApplicationContext();
            CharSequence text = "User created!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Unable to add user!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    /**
     * Cancel button clicked
     * @param button inputs view of button element
     */
    public void onCancelClicked(View button) {
        finish();
    }

    /**
     * Format UI elements depending on type of user
     * For Admin, hide interests and major
     *
     * @param userTypeRadioButton radio button for user type
     */
    public void onUserTypeClicked(View userTypeRadioButton) {

        // Is the button now checked?
        boolean checked = ((RadioButton) userTypeRadioButton).isChecked();

        // Check which radio button was clicked
        switch(userTypeRadioButton.getId()) {

            case R.id.registrationStudentRadio:
                if (checked) {
                    major.setVisibility(View.VISIBLE);
                    interests.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.registrationAdminRadio:
                if (checked) {
                    major.setVisibility(View.GONE);
                    interests.setVisibility(View.GONE);
                }
                break;
        }
    }
}
