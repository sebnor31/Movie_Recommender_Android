package com.certified_fresh.movieselector.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.MovieListAdapter;
import com.certified_fresh.movieselector.controller.RatingManagerDB;
import com.certified_fresh.movieselector.controller.RestRequest;
import com.certified_fresh.movieselector.controller.UserManagerDB;
import com.certified_fresh.movieselector.model.MovieInfoHolder;
import com.certified_fresh.movieselector.model.UserDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Instance variables
    ListView movieListView;     // Handle to the list view where movies are displayed
    MovieListAdapter adapter;   // Custom ArrayAdapter for the List View
    ArrayList<String> searchKeys;
    RatingManagerDB ratingsManager;

    // Extra Messages to pass to Movie Profile Activity
    public final static String EXTRA_MOVIE_ID = "com.certified_fresh.movieselector.view.MainActivity.MOVIE_ID";

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set our custom adapter to the movieListView
        movieListView = (ListView) findViewById(R.id.main_movie_list);
        adapter = new MovieListAdapter(getApplicationContext(), R.layout.movie_item_layout);
        movieListView.setAdapter(adapter);
        movieListView.setOnItemClickListener(this);

        // Populate the list of REST argument keys specific to target movie database (e.g., OmdB, Rotten Tomatoes)
        searchKeys = new ArrayList<>(Arrays.asList("s", "y", "type", "tomatoes", "page", "t" ));

        //Start Rating database
        ratingsManager = new RatingManagerDB(this, null);
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Verify if internet connectivity is working, alert user if not
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            String msg = "Device not connected to internet";
            Toast t = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        // Extract movie search args
        Intent intent = getIntent();
        String title = intent.getStringExtra(MovieSearchActivity.EXTRA_TITLE);
        String year = intent.getStringExtra(MovieSearchActivity.EXTRA_YEAR);

        if(title == null && year == null) {
            // If caller is not search movie activity, display recommended movies
            List<String> movieIds = ratingsManager.recommend(UserManagerDB.currentUser);

            if (adapter.getCount() > 0) {
                parseIds(movieIds);
            }
            
        } else {
            // Send a rest call to movie dd with search args
            // Create the list of search arguments
            Map<String,String> movieSearchArgs = new HashMap<>();
            movieSearchArgs.put("s", title);
            movieSearchArgs.put("y", year);
            getMovies(movieSearchArgs);
        }
    }

    /**
     * Recommends movies based on user data
     * @param user that is being analyzed
     */
    public void recommendMovies(UserDB user) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            String msg = "Device not connected to internet";
            Toast t = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        List<String> movieIds = ratingsManager.recommend(user);
        if (adapter.getCount() > 0) {
            adapter.clear();
        }
        parseIds(movieIds);
    }

    /**
     * Send a REST request to the movie database
     * based on search arguments
     * @param args search arguments
     */
    public void getMovies(Map<String,String> args) {

        // For OmdB, the type of response (e.g, JSON) must be specified
        String restArg = "r=json";

        // Add the key/value pairs to rest call argument
        for (String key : args.keySet()) {

            if (searchKeys.contains(key)) {

                String arg = args.get(key);
                if (key.equals("s")) {
                    // Replace whitespaces as required for search arguments
                    arg = arg.replace(" ", "+");
                }

                restArg += String.format("&%s=%s", key, arg);
            }
        }

        // Create REST request Url
        String url = "http://www.omdbapi.com/?" + restArg;
        Log.d("Fresh", "URL: " + url);

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

    }

    /**
     * Send a REST request based on movie ids given
     * @param ids search arguments
     */
    private void parseIds(List<String> ids) {

            if (ids.isEmpty()) {
                Toast t = Toast.makeText(this.getApplicationContext(), "There is currently nothing to recommend. Reverting to default search", Toast.LENGTH_SHORT);
                Map<String,String> movieSearchArgs = new HashMap<>();
                String title = "star";
                movieSearchArgs.put("s", title);
                String year = "";
                movieSearchArgs.put("y", year);
                getMovies(movieSearchArgs);

            } else {
                for (String id : ids) {
                    String url = "http://www.omdbapi.com/?r=json&i=" + id;
                    RestRequest helper = RestRequest.getInstance();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Parse the response
                                    parseJSONSingle(response);
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
                }
            }
    }

    /**
     * Parses that JSON son (single version)
     * @param response dat response
     */
    private void parseJSONSingle(JSONObject response) {
        try {
            String title = response.getString("Title");
            String year = response.getString("Year");
            String id = response.getString("imdbID");
            String posterURL = response.getString("Poster");

            MovieInfoHolder mh = new MovieInfoHolder(id, title, year, posterURL);
            adapter.add(mh);

        } catch(JSONException e) {
            Log.e("Fresh", e.getMessage());
        }
    }

    /**
     * Parse JSON response from movie database
     * @param response JSON response object
     */
    private void parseJSON(JSONObject response) {

        try {

            // Get the list of search results
            JSONArray movieList = response.getJSONArray("Search");

            /* Extract information from each movie result,
                create a movie object and add it to our custom ListView adapter
             */
            for (int i = 0; i < movieList.length(); i++) {

                JSONObject movieItem = movieList.getJSONObject(i);
                String id           = movieItem.getString("imdbID");
                String title        = movieItem.getString("Title");
                String year         = movieItem.getString("Year");
                String posterUrl    = movieItem.getString("Poster");

                MovieInfoHolder movie = new MovieInfoHolder(id, title, year, posterUrl);
                adapter.add(movie);
            }

        } catch (JSONException e) {
            Log.e("Fresh", e.getMessage());
        }
    }

    /**
     * Start Search Movie activity
     * @param view inputs view
     */
    public void onSearchMovieClicked(View view) {
        Intent intent = new Intent(this, MovieSearchActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Populate the Option menu (top-right) with items
     * @param menu inputs menu view
     * @return boolean value
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        //call the base class to include system menus
        super.onCreateOptionsMenu(menu);

        menu.add(0           // Group
                , 1             // item id
                , 0             //order
                , "Edit Profile");    // title
        menu.add(0
                , 2
                , 1
                , "View Recommendations"
        );
        menu.add(0           // Group
                , 3            // item id
                , 2             //order
                , "Logout");    // title

        return true;
    }

    /**
     * React to a selected Option item
     * @param item option item
     * @return true if event has been processed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case 2:
                recommendMovies(UserManagerDB.currentUser);
                return true;
            case 3:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Response to user clicking logout button
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Resumes activity
     * @param parent of view
     * @param view of movie image
     * @param position in list of moview
     * @param id of moview selected
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieInfoHolder movie = adapter.getItem(position);

        ImageView image = (ImageView) view.findViewById(R.id.movie_poster);
        Drawable poster = image.getDrawable();

        Intent intent = new Intent(this, MovieProfileActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie.getId());

        startActivity(intent);
    }
}
