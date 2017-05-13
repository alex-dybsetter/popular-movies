package net.alexblass.popularmovies1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.alexblass.popularmovies1.models.Movie;

// This activity shows a detail view of a selected Movie

public class DetailActivity extends AppCompatActivity {

    private Movie currentMovie;
    private ImageView mPoster;
    private TextView mTitle,
        mSynopsis,
        mRating,
        mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();

        // Initialize the views from the layout
        mPoster = (ImageView) findViewById(R.id.detail_imageview);
        mTitle = (TextView) findViewById(R.id.detail_title_tv);
        mSynopsis = (TextView) findViewById(R.id.detail_synopsis_tv);
        mRating = (TextView) findViewById(R.id.detail_rating_tv);
        mReleaseDate = (TextView) findViewById(R.id.detail_release_date_tv);

        if (intentThatStartedThisActivity != null) {
            // If the intent has a Movie object Extra, read the information from it
            // and display it in the correct views
            if (intentThatStartedThisActivity.hasExtra("Movie")) {
                currentMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra("Movie");

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
            }
        }
    }
}
