package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.DataBase.AppDatabase;
import com.example.android.popularmovies.DataBase.FavoriteMovies;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DetailsScreen extends AppCompatActivity
{

    private ImageView mImageview;
    private TextView original_title;
    private TextView overview;
    private TextView vote_average;
    private TextView release_date;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterDetailsScreen adapter;
    private Button mbuttonFavorite,deleteFavoriteButton;
    private RecyclerViewAdapterReviews adapterReviews;
    private RecyclerView mRcyclerViewReviews;
    private AppDatabase mDb;
    private SharedPreferences prefs;
    private String dataRetrived="";
    private int id;
    private String link="",linkForReviews="";
    private int counterTrailers;
    private String [] keys;
    private String [] authors;
    private String [] contents;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        prefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        mDb=AppDatabase.getInstance(getApplicationContext());


        getIncomingIntent();
        try {
            networkUtils();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerViewAdapter1();

        try {
            networkUtils1();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerViewAdapter2();

        final String nameOffilm=original_title.getText().toString();

        mbuttonFavorite=(Button)findViewById(R.id.favorite_button);
        deleteFavoriteButton=(Button)findViewById(R.id.deletefavorite_button);

        // for saving button prefrences after pressing on it
        mbuttonFavorite.setVisibility(prefs.getBoolean(nameOffilm, true) ? View.VISIBLE : View.INVISIBLE);
        deleteFavoriteButton.setVisibility(prefs.getBoolean(nameOffilm, true) ? View.INVISIBLE : View.VISIBLE);

        //prefs.edit().putBoolean(nameOffilm, true).apply();
        mbuttonFavorite.setOnClickListener(new View.OnClickListener()
        {
            final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                 onSaveButtonClicked();
            }
        });
            @Override
            public void onClick(View v)
            {
                prefs.edit().putBoolean(nameOffilm, false).apply();
                mbuttonFavorite.setVisibility(View.INVISIBLE);
                thread.start();
            }
        });

        deleteFavoriteButton.setOnClickListener(new View.OnClickListener() {

            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    deleteMovieByTitle();
                }
            });
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(nameOffilm, true).apply();
                deleteFavoriteButton.setVisibility(View.INVISIBLE);
                thread.start();
            }
        });
    }


    public void deleteMovieByTitle()
    {

        mDb.taskDao().deleteMoviebyTitle(getIntent().getStringExtra("original_title"));
        finish();

    }


    //for saving data in DB
    public void onSaveButtonClicked()
    {
        if (getIntent().hasExtra("image_thumbnail") && getIntent().hasExtra("original_title") && getIntent().hasExtra("overview"))
        {
            String image = getIntent().getStringExtra("image_thumbnail");
            String title = getIntent().getStringExtra("original_title");
            String overview = getIntent().getStringExtra("overview");
            id=getIntent().getIntExtra("id",0);

            FavoriteMovies favoriteMovies=new FavoriteMovies(id,image,title,overview);
            mDb.taskDao().insertMovie(favoriteMovies);
            finish();
        }
    }


      /*** to delete all records in the table if you want to check all replace onSaveButtonClicked with deleteAll ***/
    public void deleteAll()
    {

            mDb.taskDao().deleteallTasks();
            finish();

    }
    private void getIncomingIntent()
    {
        if (getIntent().hasExtra("image_thumbnail") && getIntent().hasExtra("original_title") && getIntent().hasExtra("overview") && getIntent().hasExtra("vote_average") && getIntent().hasExtra("release_date")) {


            String image = getIntent().getStringExtra("image_thumbnail");
            String title = getIntent().getStringExtra("original_title");
            String overview = getIntent().getStringExtra("overview");
            int vote = getIntent().getIntExtra("vote_average", 0);
            String release = getIntent().getStringExtra("release_date");
            id=getIntent().getIntExtra("id",0);
            link+="https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=95bc3cb8063677fac3648b921ca60b46";
            setdetails(image, title, overview, vote, release);
            linkForReviews="https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=95bc3cb8063677fac3648b921ca60b46";

        }
    }

    private void setdetails(String image, String title, String over_view, int vote, String release)
    {
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

    /*** NETWORK UTILS FOR THE TRAILERS TO OPEN YOUTUBE VIDEOS ***/
    public void networkUtils() throws JSONException
    {
        NetworkUtilites ob = new NetworkUtilites();
        ob.setMoviesURL(link);
        try {
            dataRetrived = ob.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!dataRetrived.equals(null))
        {
            JSONObject parentObject = new JSONObject(dataRetrived);
            JSONArray parentArray = parentObject.getJSONArray("results");
            counterTrailers = parentArray.length();
            keys = new String[parentArray.length()];
            for (int i = 0; i < parentArray.length(); i++)
            {
                JSONObject movieDetails = parentArray.getJSONObject(i);
                keys[i]="https://www.youtube.com/watch?v=";
                keys[i]+=movieDetails.getString("key");
            }
        }





    }
    public void networkUtils1() throws JSONException
    {
        /*** NETWORK UTILS FOR REVIEW API AND GETTING INFROMATION FOR AUTHORS AND CONTENTS ***/
        NetworkUtilites ob = new NetworkUtilites();
        dataRetrived="";
        ob.setMoviesURL(linkForReviews);
        try {
            dataRetrived = ob.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!dataRetrived.equals(null))
        {
            JSONObject parentObject = new JSONObject(dataRetrived);
            JSONArray parentArray = parentObject.getJSONArray("results");

            authors = new String[parentArray.length()];
            contents=new String[parentArray.length()];

            for (int i = 0; i < parentArray.length(); i++)
            {
                JSONObject movieDetails = parentArray.getJSONObject(i);
                authors[i]=movieDetails.getString("author");
                contents[i]=movieDetails.getString("content");
            }
        }
    }

    public void RecyclerViewAdapter1()
    {
        adapter = new RecyclerViewAdapterDetailsScreen(this, counterTrailers,keys);
        mRecyclerView = findViewById(R.id.recycler_view_details);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    public void RecyclerViewAdapter2()
    {
        adapterReviews=new RecyclerViewAdapterReviews(this,authors,contents);
        mRcyclerViewReviews=findViewById(R.id.recycler_view_review);
        mRcyclerViewReviews.setAdapter(adapterReviews);
        mRcyclerViewReviews.setHasFixedSize(true);
        mRcyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
    }



}
