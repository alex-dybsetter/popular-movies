package net.alexblass.popularmovies1.utilities;

import android.text.TextUtils;
import android.util.Log;

import net.alexblass.popularmovies1.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Helper methods to request and receive Movie data
 */

public class QueryUtils {
    // Log tag for error messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Empty private constructor because no QueryUtils
    // object should be initialized
    private QueryUtils(){}

    // Query the dataset and return a list of Movies
    public static Movie[] fetchMovieData(String requestUrl){
        URL url = createUrl(requestUrl);

        // perform the HTTP request to the URL to receive a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract the relevant fields and create a list of Movies
        Movie[] movies = extractFeatureFromJson(jsonResponse);

        return movies;
    }

    // Returns the new URL object from the given String url
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful, then read the input stream
            // and parse the response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Convert the InputStream into a String which contains the whole
    // JSON response from the server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    // Return a list of Movie objects built from parsing the JSON response
    private static Movie[] extractFeatureFromJson(String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        Movie[] movies = new Movie[0];

        // Try to parse the JSON response  If there's a formatting problem,
        // an exception will be thrown
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            JSONArray results = baseJsonResponse.getJSONArray("results");
            movies = new Movie[results.length()];

            for (int i = 0; i < results.length(); i++){
                JSONObject currentMovie = results.getJSONObject(i);

                String movieId = currentMovie.getString("id");
                String movieTitle = currentMovie.getString("original_title");

                String movieImagePath = "";
                if (!currentMovie.getString("poster_path").equalsIgnoreCase("null")){
                    movieImagePath = currentMovie.getString("poster_path");
                }

                String movieOverview = currentMovie.getString("overview");
                Double movieRating = currentMovie.getDouble("vote_average");
                String movieReleaseDate = currentMovie.getString("release_date");

                // Format the release date
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
                String formattedDate = "";

                try {

                    formattedDate = myFormat.format(oldFormat.parse(movieReleaseDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Movie newMovie = new Movie(movieId, movieTitle, movieImagePath,
                                movieOverview, movieRating, formattedDate);
                movies[i] = newMovie;
            }
        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the JSON response.");
        }
        return movies;
    }
}
