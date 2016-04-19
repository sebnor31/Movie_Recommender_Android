package com.certified_fresh.movieselector.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.model.MovieInfoHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsebkhi3 on 2/19/2016.
 *
 * Adapter for the list of movies to be displayed
 * on the movie_item_layout.
 */
public class MovieListAdapter extends ArrayAdapter<MovieInfoHolder> {

    // List of movies
    List<MovieInfoHolder> list = new ArrayList<>();

    /* Convenience data holder of UI belts
       of the movie_item_layout */
    static class DataHolder {
        NetworkImageView poster;
        TextView title;
        TextView year;
    }

    /**
     * Constructor of this ArrayAdapter
     * @param context inputs context for adapter
     * @param resource integer resource
     */
    public MovieListAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Add a new movie entry in the list
     * @param movie a data holder of movie attributes
     */
    @Override
    public void add(MovieInfoHolder movie) {
        super.add(movie);
        list.add(movie);
    }

    /**
     * Get the number of movies in the list
     * @return size of movie list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Clears adapter to prevent duplicates
     */
    @Override
    public void clear() {
        list.clear();
        super.clear();
    }


    /**
     * Get a movie from list
     * @param position Position of the movie in the list
     * @return a movie
     */
    @Override
    public MovieInfoHolder getItem(int position) {
        return super.getItem(position);
    }

    /**
     * Set or recycle an item of the movie ListView
     * @param position position of the movie in the list to be displayed
     * @param convertView a ViewGroup to be populated with a movie's attributes
     * @param parent ListView parent
     * @return a ViewGroup populated with a movie's attributes
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Initialize the convenience movie ListView's items data holder
        DataHolder movieDataHolder;

        // Set the ViewGroup convertView
        if (convertView == null) {
            // Set the layout of a new item to the movie_item_layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_item_layout, parent, false);

            // Set the movie data holder attributes to this view's UI elements
            movieDataHolder = new DataHolder();
            movieDataHolder.poster = (NetworkImageView) convertView.findViewById(R.id.movie_poster);
            movieDataHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
            movieDataHolder.year = (TextView) convertView.findViewById(R.id.movie_year);

            // Not sure what Tags are for...but needed
            convertView.setTag(movieDataHolder);
        }
        else {
            // Set the movie data holder attributes to this existing view's UI elements
            movieDataHolder = (DataHolder) convertView.getTag();
        }

        // Get the movie at specified position
        MovieInfoHolder movie = getItem(position);

        // Set the view's UI elements to the selected movie's attributes
        movieDataHolder.title.setText(movie.getTitle());
        movieDataHolder.year.setText(movie.getYear());

        // Set the poster preview
        // If no URL available, set the default no_poster image
        // otherwise, get the poster preview from url
        if (movie.getPoster().equals("N/A")) {
            movieDataHolder.poster.setImageResource(R.drawable.no_poster);
        }
        else {
            ImageLoader imageLoader = RestRequest.getInstance().getImageLoader();
            movieDataHolder.poster.setImageUrl(movie.getPoster(), imageLoader);
        }

        return convertView;
    }
}
