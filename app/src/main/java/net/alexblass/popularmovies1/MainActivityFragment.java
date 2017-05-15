package net.alexblass.popularmovies1;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import net.alexblass.popularmovies1.utilities.MovieLoader;

/**
 * A fragment that shows the grid of movie posters
 * and launches a new activity when the user taps
 * a movie poster.
 */
public class MainActivityFragment extends Fragment
        implements MovieAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<Movie[]>{

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

    // The ID for the MovieLoader
    private static final int MOVIE_LOADER_ID = 0;

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

        LoaderManager loaderManager = getLoaderManager();

        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        // If there is no connectivity, show an error message
        if (isConnected) {
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            showErrorMessage();
            mErrorMessageTextView.setText(R.string.no_connection);
        }
        return rootView;
    }

    // Pass the URI to the Movie loader to load the data
    @Override
    public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(sortPreference);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new MovieLoader(getContext(), uriBuilder.toString());
    }

    // When the Loader finishes, add the list of Movies to the adapter's data set
    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] moviesList) {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageTextView.setText(R.string.no_results);
        mAdapter.setMovies(new Movie[0]);

        if (moviesList != null && moviesList.length > 0){
            mAdapter.setMovies(moviesList);
        }

    }

    // Reset the loader to clear existing data
    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        mAdapter.setMovies(new Movie[0]);
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
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_sort_rating) {
            sortPreference = formUrl(SORT_BY_RATING_URL);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
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