package com.certified_fresh.movieselector.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.UserManagerDB;

public class LoginActivity extends AppCompatActivity {

    // Instance variables
    EditText username;
    EditText password;
    UserManagerDB userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate variables from this activity views
        username = (EditText) findViewById(R.id.usernameEdit);
        password = (EditText) findViewById(R.id.pwdEdit);
        userManager = new UserManagerDB(this, null);
    }

    /**
     * Resumes activity
     */
    protected void onResume() {
        super.onResume();

        // userManager.recreateDB();


        // Clear previous entry
        username.setText("");
        password.setText("");
    }

    /**
     * Return to welcome page when cancel button is clicked
     * @param view cancel button
     */
    public void onLoginCancelButtonClicked(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    /**
     * Authenticate user and start proper activity
     * @param view login button
     */
    public void onLoginOkButtonClicked(View view) {

        // Verify if login credentials exist in user database
        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();
        int loginStatus = userManager.authenticate(usernameTxt, passwordTxt);

        // Log login info
        Log.i("Fresh", String.format("Username: %s\tPassword: %s\tLogin Status? %d", usernameTxt, passwordTxt, loginStatus));
        setLoginMsg(loginStatus);

        // Set action based on login success
        if (loginStatus == 1) {
            // Login as Student
            startActivity(new Intent(this, MainActivity.class));

        } else if (loginStatus == 2) {
            // Login as Admin
            startActivity(new Intent(this, UserListActivity.class));

        } else {
            // Login unsuccessful
            username.setText("");
            password.setText("");
        }
    }

    /**
     * Start the registration activity
     * @param view link to register
     */
    public void onRegisterTextClicked(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }


    /**
     * Set a message about login status
     * @param status login status
     *               (1 = successful login as Student)
     *               (2 = successful login as Admin)
     *               (3 = locked Student)
     *               (4 = banned Student)
     *               (other = unsuccessful login)
     */
    public void setLoginMsg(int status) {
        // Content of the login message
        String msg;

        // Set content and color depending of login status
        if (status == 1) {
            msg = getString(R.string.login_msg_success_student);

        } else if (status == 2){
            msg = getString(R.string.login_msg_success_admin);

        } else if (status == 3) {
            msg = getString(R.string.login_msg_locked_student);

        } else if (status == 4) {
            msg = getString(R.string.login_msg_banned_student);

        } else {
            msg = getString(R.string.login_msg_unsuccess);
        }

        // Set the view text and color
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast t = Toast.makeText(context, msg, duration);
        t.show();
    }

}
