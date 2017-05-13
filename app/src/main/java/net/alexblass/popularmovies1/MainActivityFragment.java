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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import net.alexblass.popularmovies1.data.Movie;
import net.alexblass.popularmovies1.utilities.MovieAdapter;
import net.alexblass.popularmovies1.utilities.MovieLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that shows the grid of movie posters
 * and launches a new activity when the user taps
 * a movie poster.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    // Displays a message when there is no Internet or when there are no Movies found
    private TextView mErrorMessageTextView;

    // Loading indicator for a responsive app experience
    private View mLoadingIndicator;

    // Movie adapter to display the Movies correctly
    private MovieAdapter mAdapter;

    // The URL to fetch the Movie JSON data
    private static final String REQUEST_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    // The query for a popularity sort
    private static final String SORT_BY_POPULARITY_URL = "popular?";

    // The query for a rating sort
    private static final String SORT_BY_RATING_URL = "top_rated?";

    // The API key
    private static final String API_KEY = "&api_key=" + "YOUR_API_KEY_HERE";

    // A string to hold the complete sort preference URL
    private String sortPreference = createUrl(SORT_BY_POPULARITY_URL);

    // Empty constructor
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        // Find the GridView and set our adapter to it so the posters
        // display in a grid format
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_poster_grid);
        gridView.setAdapter(mAdapter);

        // When the user clicks a poster, launch a new activity with the detail view
        // for the selected Movie
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);

                // Pass the current Movie into the new Intent so we can access it's information
                Movie currentMovie = mAdapter.getItem(position);
                intent.putExtra("Movie", currentMovie);

                startActivity(intent);
            }
        });

        mErrorMessageTextView = (TextView) rootView.findViewById(R.id.error_message_tv);
        gridView.setEmptyView(mErrorMessageTextView);

        LoaderManager loaderManager = getLoaderManager();

        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        // If there is no connectivity, show an error message
        if (isConnected) {
            loaderManager.initLoader(0, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mErrorMessageTextView.setText(R.string.no_connection);
        }
        return rootView;
    }

    // Pass the URI to the Movie loader to load the data
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(sortPreference);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new MovieLoader(getContext(), uriBuilder.toString());
    }

    // When the Loader finishes, add the list of Movies to the adapter's data set
    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> moviesList) {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageTextView.setText(R.string.no_results);
        mAdapter.clear();

        if (moviesList != null && !moviesList.isEmpty()){
            mAdapter.addAll(moviesList);
        }

    }

    // Reset the loader to clear existing data
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
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
            sortPreference = createUrl(SORT_BY_POPULARITY_URL);
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        if (id == R.id.action_sort_rating){
            sortPreference = createUrl(SORT_BY_RATING_URL);
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Creates the complete URL for sorting the movies
    public String createUrl(String sortPreference){
        return REQUEST_BASE_URL + sortPreference + API_KEY;
    }
}