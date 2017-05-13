package net.alexblass.popularmovies1.models;

import java.io.Serializable;

/**
 * Movie object to store JSON values pulled from the network.
 * Implements Serializable so that Movie objects can be passed
 * via Intent to open a new activity.
 */

public class Movie implements Serializable {
    // Information about the movie relevant to our app
    // Pulled from the JSON objects into Movie objects
    private String mId;
    private String mTitle;
    private String mImagePath;
    private String mOverview;
    private double mRating;
    private String mReleaseDate;

    // Base URL for image paths
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    // The size image we need for our app
    private static final String IMAGE_SIZE = "w185";

    // Constructor to set the values of a particular Movie
    public Movie(String id, String title, String img, String overview, double rating, String releaseDate){
        mId = id;
        mTitle = title;
        mImagePath = img;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public String getId() { return mId; }

    public String getTitle() {
        return mTitle;
    }

    public String getImagePath() {
        if (mImagePath.isEmpty()){
            return null;
        } else {
            return BASE_IMAGE_URL + IMAGE_SIZE + mImagePath;
        }
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
}
