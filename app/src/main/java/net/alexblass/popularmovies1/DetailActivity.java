package net.alexblass.popularmovies1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.alexblass.popularmovies1.models.Movie;
import net.alexblass.popularmovies1.utilities.TrailerListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

// This activity shows a detail view of a selected Movie

public class DetailActivity extends AppCompatActivity implements TrailerListAdapter.ItemClickListener {
    @BindView(R.id.detail_imageview) ImageView mPoster;
    @BindView(R.id.detail_title_tv) TextView mTitle;
    @BindView(R.id.detail_synopsis_tv) TextView mSynopsis;
    @BindView(R.id.detail_rating_tv) TextView mRating;
    @BindView(R.id.detail_release_date_tv) TextView mReleaseDate;
    @BindView(R.id.detail_duration_tv) TextView mDuration;

    @BindView(R.id.movie_trailers_list) RecyclerView mTrailersList;

    private Movie currentMovie;

    // The URL to Youtube to get the trailers
    private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            // If the intent has a Movie object Extra, read the information from it
            // and display it in the correct views
            if (intentThatStartedThisActivity.hasExtra("Movie")) {
                currentMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");

                if (currentMovie.getImagePath() != null) {
                    Picasso.with(this)
                            .load(currentMovie.getImagePath())
                            .placeholder(R.drawable.ic_photo_white_48dp)
                            .error(R.drawable.ic_photo_white_48dp)
                            .into(mPoster);
                }
                mTitle.setText(currentMovie.getTitle());

                if (!currentMovie.getOverview().isEmpty()) {
                    mSynopsis.setText(currentMovie.getOverview());
                } else {
                    mSynopsis.setText(R.string.empty_value);
                }

                mRating.setText(Double.toString(currentMovie.getRating()));

                if (!currentMovie.getReleaseDate().isEmpty()) {
                    mReleaseDate.setText(currentMovie.getReleaseDate());
                } else {
                    mReleaseDate.setText(R.string.empty_value);
                }

                mDuration.setText(getString(
                        R.string.duration_units, Integer.toString(currentMovie.getDuration())));
            }

            // Inflate the recycler view with the trailers for the movie
            TrailerListAdapter mAdapter = new TrailerListAdapter(this, currentMovie.getTrailerKeys());
            mAdapter.setClickListener(this);
            mTrailersList.setAdapter(mAdapter);
            mTrailersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    // When the user clicks a trailer, play the trailer in Youtube or on a web browser
    @Override
    public void onItemClick(View view, int position) {
        // Get the selected trailer's trailer key
        String youtubeUrlString = TRAILER_BASE_URL + currentMovie.getTrailerKeys().get(position);

        // Launch intent to open video trailer in youtube or on the internet app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrlString));
        startActivity(intent);
    }
}
