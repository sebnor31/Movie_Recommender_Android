package com.certified_fresh.movieselector.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.certified_fresh.movieselector.R;

public class MovieSearchActivity extends AppCompatActivity {
    public final static String EXTRA_TITLE = "com.certified_fresh.movieselector.view.MovieSearchActivity.TITLE";
    public final static String EXTRA_YEAR = "com.certified_fresh.movieselector.view.MovieSearchActivity.YEAR";

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviesearch);
    }

    /**
     * Engages search feature on activity, exciting!
     * @param view of search button
     */
    public void searchMovie(View view) {

        EditText title = (EditText) findViewById(R.id.movie_search_title);
        String titleTxt = title.getText().toString();

        // Verify Title is not empty as it is required for a movie search as per OmdB requirement
        if(titleTxt == null || titleTxt.isEmpty()) {
            String msg = "Provide a Title";
            Toast t = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        // Create an intent
        Intent intent = new Intent(this, MainActivity.class);

        // Provide a title extra
        intent.putExtra(EXTRA_TITLE, titleTxt);

        // Provide a year extra
        EditText year = (EditText) findViewById(R.id.movie_search_year);
        String yearTxt = year.getText().toString();
        intent.putExtra(EXTRA_YEAR, yearTxt);

        startActivity(intent);
        finish();
    }

    /**
     * Goes back to main activity when 'back' button pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

}
