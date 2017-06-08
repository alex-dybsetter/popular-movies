package net.alexblass.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import net.alexblass.popularmovies.models.Movie;

/**
 * Loads a list of Movies using the AsyncTask to perform the
 * network request to the given URL
 */

public class MovieLoader extends AsyncTaskLoader<Movie[]> {
    // Query URL
    private String mUrl;

    // Constructs a new MovieLoader
    public MovieLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Movie[] loadInBackground() {
        if (mUrl == null){
            return null;
        }

        Movie[] movies = QueryUtils.fetchMovieData(mUrl);
        return movies;
    }
}