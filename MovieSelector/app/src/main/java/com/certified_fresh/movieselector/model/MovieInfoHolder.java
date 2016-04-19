package com.certified_fresh.movieselector.model;

/**
 * Created by nsebkhi3 on 2/19/2016.
 */
public class MovieInfoHolder {

    private String title;
    private String year;
    private String poster;
    private String id;


    public MovieInfoHolder(String id, String title, String year, String poster) {
        this.setId(id);
        this.setTitle(title);
        this.setYear(year);
        this.setPoster(poster);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


}
