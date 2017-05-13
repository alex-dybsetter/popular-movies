package net.alexblass.popularmovies1.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.alexblass.popularmovies1.R;
import net.alexblass.popularmovies1.data.Movie;

import java.util.List;

/**
 * Displays an array list of Movie posters in a GridView.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    // Construct a new MovieAdapter
    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the currently selected movie
        Movie currentMovie = getItem(position);

        // If there's an existing view we can reuse, use it otherwise
        // inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        // Find the ImageView to set the current Movie poster to
        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
        if (currentMovie.getImagePath() != null) {
            Picasso.with(getContext()).load(currentMovie.getImagePath()).into(moviePoster);
        } else {
            moviePoster.setColorFilter(R.color.colorAccent);
        }


        return convertView;
    }
}
