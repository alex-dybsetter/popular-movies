package net.alexblass.popularmovies1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.alexblass.popularmovies1.models.Movie;
import net.alexblass.popularmovies1.utilities.MovieAdapter;
import net.alexblass.popularmovies1.utilities.QueryUtils;

/**
 * A fragment that shows the grid of movie posters
 * and launches a new activity when the user taps
 * a movie poster.
 */
public class MainActivityFragment extends Fragment
        implements MovieAdapter.ItemClickListener {

    // Displays a message when there is no Internet or when there are no Movies found
    private TextView mErrorMessageTextView;

    // Loading indicator for a responsive app experience
    private View mLoadingIndicator;

    // A RecyclerView to hold all of our Movie posters and enable smooth scrolling
    RecyclerView mRecyclerView;

    // Movie adapter to display the Movies correctly
    private MovieAdapter mAdapter;

    // The URL to fetch the Movie JSON data
    private static final String REQUEST_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    // The query for a popularity sort
    private static final String SORT_BY_POPULARITY_URL = "popular?";

    // The query for a rating sort
    private static final String SORT_BY_RATING_URL = "top_rated?";

    // A string to hold the complete sort preference URL
    private String sortPreference = formUrl(SORT_BY_POPULARITY_URL);

    // Empty constructor
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        // Find the RecyclerView and set our adapter to it so the posters
        // display in a grid format
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_poster_grid);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), numberOfColumns(getContext())));

        mAdapter = new MovieAdapter(getActivity(), new Movie[0]);
        mAdapter.setClickListener(this);

        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);
        mErrorMessageTextView = (TextView) rootView.findViewById(R.id.error_message_tv);

        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        // If there is no connectivity, show an error message
        if (isConnected) {
            loadData();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            showErrorMessage();
            mErrorMessageTextView.setText(R.string.no_connection);
        }
        return rootView;
    }

    // When the user clicks a poster, launch a new activity with the detail view
    // for the selected Movie
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);

        // Pass the current Movie into the new Intent so we can access it's information
        Movie currentMovie = mAdapter.getItem(position);
        intent.putExtra("Movie", currentMovie);

        startActivity(intent);
    }

    // Create a new AsyncTask to pull the information from the server
    public void loadData(){
        showRecyclerView();
        new FetchMoviesTask().execute(sortPreference);
    }

    // Set the data view to visible and the error message view to invisible
    public void showRecyclerView(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    // Set the data view to invisible and the error message to visible
    public void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String requestUrl = params[0];
            return QueryUtils.fetchMovieData(requestUrl);
        }

        @Override
        protected void onPostExecute(Movie[] moviesList) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (moviesList != null) {
                showRecyclerView();
                mAdapter.setMovies(moviesList);
            } else {
                showErrorMessage();
                mErrorMessageTextView.setText(R.string.no_results);
            }
        }
    }

    // Create a menu to display the sort options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Sort the movies by the user's sort preference
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            sortPreference = formUrl(SORT_BY_POPULARITY_URL);
            new FetchMoviesTask().execute(sortPreference);
            return true;
        }

        if (id == R.id.action_sort_rating) {
            sortPreference = formUrl(SORT_BY_RATING_URL);
            new FetchMoviesTask().execute(sortPreference);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Creates the complete URL string for sorting the movies
    public String formUrl(String sortPreference) {
        String stringUrl = REQUEST_BASE_URL + sortPreference +
                "&api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;

        return stringUrl;
    }

    // Calculates the number of columns in the RecyclerView
    public static int numberOfColumns(Context context) {
        int columnWidth = 150;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberofColumns = (int) (dpWidth / columnWidth);
        return numberofColumns;
    }
}