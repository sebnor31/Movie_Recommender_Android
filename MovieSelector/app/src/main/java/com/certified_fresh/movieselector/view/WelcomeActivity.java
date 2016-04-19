package com.certified_fresh.movieselector.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.certified_fresh.movieselector.R;


public class WelcomeActivity extends AppCompatActivity {

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Takes user to the LoginActivity when button is clicked
     * @param button to go to LoginActivity
     */
    public void onLoginButtonClicked(View button) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
