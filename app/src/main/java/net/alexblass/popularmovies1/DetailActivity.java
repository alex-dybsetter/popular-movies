package net.alexblass.popularmovies1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.alexblass.popularmovies1.models.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

// This activity shows a detail view of a selected Movie

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_imageview) ImageView mPoster;
    @BindView(R.id.detail_title_tv) TextView mTitle;
    @BindView(R.id.detail_synopsis_tv) TextView mSynopsis;
    @BindView(R.id.detail_rating_tv) TextView mRating;
    @BindView(R.id.detail_release_date_tv) TextView mReleaseDate;
    @BindView(R.id.detail_duration_tv) TextView mDuration;

    private Movie currentMovie;

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
        }
    }
}
