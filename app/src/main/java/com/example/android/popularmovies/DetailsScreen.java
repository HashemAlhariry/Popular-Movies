package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsScreen extends AppCompatActivity {
    private ImageView mImageview;
    private TextView original_title;
    private TextView overview;
    private TextView vote_average;
    private TextView release_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);


        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("image_thumbnail") && getIntent().hasExtra("original_title") && getIntent().hasExtra("overview") && getIntent().hasExtra("vote_average") && getIntent().hasExtra("release_date")) {


            String image = getIntent().getStringExtra("image_thumbnail");
            String title = getIntent().getStringExtra("original_title");
            String overview = getIntent().getStringExtra("overview");
            int vote = getIntent().getIntExtra("vote_average", 0);
            String release = getIntent().getStringExtra("release_date");

            Log.d("GETINCOMINGINTENT", image + " " + title + " " + overview + " " + vote + " " + release);
            setdetails(image, title, overview, vote, release);

        }
    }


    private void setdetails(String image, String title, String over_view, int vote, String release) {
        mImageview = (ImageView) findViewById(R.id.image_thumbnail);
        Picasso.get().load(image).into(mImageview);
        original_title = (TextView) findViewById(R.id.original_title);
        original_title.setText(title);
        overview = (TextView) findViewById(R.id.overview);
        overview.setText(over_view);
        vote_average = (TextView) findViewById(R.id.vote_average);
        vote_average.setText(String.valueOf(vote));
        release_date = (TextView) findViewById(R.id.release_date);
        release_date.setText(release);

    }

}
