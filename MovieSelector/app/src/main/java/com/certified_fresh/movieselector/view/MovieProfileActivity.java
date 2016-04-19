package com.certified_fresh.movieselector.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.RatingManagerDB;
import com.certified_fresh.movieselector.controller.RestRequest;
import com.certified_fresh.movieselector.controller.UserManagerDB;
import com.certified_fresh.movieselector.model.RatingDB;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieProfileActivity extends AppCompatActivity {

    NetworkImageView posterImg;
    TextView titleView;
    RatingBar ratingSelector;
    TextView cfratingView;
    TextView imdbratingView;
    TextView plotView;
    EditText commentText;
    TextView ratingsView;
    Button submitButton;
    RatingManagerDB ratingManager;
    private String movieId;

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);

        // Set View handles
        posterImg = (NetworkImageView) findViewById(R.id.movie_profile_poster);
        titleView = (TextView) findViewById(R.id.movie_profile_title);
        ratingSelector = (RatingBar) findViewById(R.id.movie_profile_rating_bar);
        cfratingView = (TextView) findViewById(R.id.movie_profile_cf_rating);
        imdbratingView = (TextView) findViewById(R.id.movie_profile_imdb_rating);
        plotView = (TextView) findViewById(R.id.movie_profile_plot);
        commentText = (EditText) findViewById(R.id.movie_profile_comment);
        ratingsView = (TextView) findViewById(R.id.movie_profile_ratings);
        submitButton = (Button) findViewById(R.id.movie_profile_submit_review);
        ratingManager = new RatingManagerDB(this, null);

    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();



        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID);
        movieId = id;

        // Create REST request Url
        String url = "http://www.omdbapi.com/?i=" + id;
        Log.d("Fresh", "Movie Profile URL: " + url);

        // Send a REST request and get a JSON response
        RestRequest helper = RestRequest.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the response
                        parseJSON(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Fresh", "Cannot perform REST call\n" + error.toString());
                    }
                }
        );

        helper.add(request);
        ratingManager.recommend(UserManagerDB.currentUser);
        ratingsView.setText(ratingManager.getRatingsFormatted(movieId));
        cfratingView.setText("Student rating: " + ratingManager.getAverageRatingByMovieId(movieId) + "/5.0");
        int userRating = ratingManager.getUserRating(UserManagerDB.currentUser, movieId);
        if (userRating != -1) {
            ratingSelector.setIsIndicator(true);
            ratingSelector.setNumStars(userRating);
            submitButton.setEnabled(false);
            commentText.setEnabled(false);

        }
    }

    /**
     * Adds new rating when rating button clicked
     * @param button view of rating button that is clicked
     */
    public void onRateClicked(View button) {
        RatingDB rating = new RatingDB(movieId,
                UserManagerDB.currentUser,
                commentText.getText().toString(), (int)ratingSelector.getRating());
        ratingManager.add(rating);
        finish();

    }

    /**
     * Parses that JSON son
     * @param response dat response
     */
    private void parseJSON(JSONObject response) {
        try {

            // Get movie details
            String title          = response.getString("Title");
            String year           = response.getString("Year");
            String plot           = response.getString("Plot");
            String rating         = response.getString("imdbRating");
            String posterUrl      = response.getString("Poster");

            // Set title and plot text
            titleView.setText(String.format("%s (%s)", title, year));
            plotView.setText("Plot:\n" + plot);

            // Set Rating
            float ratingFormatted =  Float.valueOf(rating);
            imdbratingView.setText("IMDb rating: " + ratingFormatted + "/10.0");

            // Set poster
            ImageLoader imageLoader = RestRequest.getInstance().getImageLoader();
            posterImg.setImageUrl(posterUrl, imageLoader);

        } catch (JSONException e) {
            Log.e("Fresh", e.getMessage());
        }
    }
}
