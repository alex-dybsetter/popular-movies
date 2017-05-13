package net.alexblass.popularmovies1.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import net.alexblass.popularmovies1.models.Movie;

import java.util.List;

/**
 * Loads a list of Movies using the AsyncTask to perform the
 * network request to the given URL
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
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
    public List<Movie> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        List<Movie> movies = QueryUtils.fetchMovieData(mUrl);
        return movies;
    }
}
