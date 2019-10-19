package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private ArrayList<ListItem> arrayMovies = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private String dataRetrieved;
    private static final String TAG = "MAINACTIVITY";
    private String top_rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=95bc3cb8063677fac3648b921ca60b46";
    private String MoviesURL = "http://api.themoviedb.org/3/movie/popular?api_key=95bc3cb8063677fac3648b921ca60b46";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        networkUtilites(false);
        getArrayMovies();
        RecyclerViewAdapter();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                Toast.makeText(this, "MostPopularClicked", Toast.LENGTH_SHORT).show();
                arrayMovies.clear();
                networkUtilites(false);
                getArrayMovies();
                RecyclerViewAdapter();

                return true;

            case R.id.top_rated:
                Toast.makeText(this, "TopRatedClicked", Toast.LENGTH_SHORT).show();
                arrayMovies.clear();
                networkUtilites(true);
                getArrayMovies();
                RecyclerViewAdapter();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void networkUtilites(Boolean check) {

        NetworkUtilites ob = new NetworkUtilites();
        if (check) {
            ob.setMoviesURL(top_rated);
        } else {
            ob.setMoviesURL(MoviesURL);
        }

        try {
            dataRetrieved = ob.execute().get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void getArrayMovies() {

        try {


            if (!dataRetrieved.equals(null)) {
                JSONObject parentObject = new JSONObject(dataRetrieved);
                JSONArray parentArray = parentObject.getJSONArray("results");

                System.out.println(parentArray.length());
                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject movieDetails = parentArray.getJSONObject(i);
                    String poster_path = "http://image.tmdb.org/t/p/w500";
                    poster_path += movieDetails.getString("poster_path");
                    String original_title = movieDetails.getString("original_title");
                    String overview = movieDetails.getString("overview");
                    int vote_average = movieDetails.getInt("vote_average");
                    String release_date = movieDetails.getString("release_date");

                    ListItem ob = new ListItem(original_title, poster_path, overview, vote_average, release_date);
                    arrayMovies.add(ob);
                }

            }
        } catch (Exception e) {

        }


    }

    public void RecyclerViewAdapter() {
        adapter = new RecyclerViewAdapter(this, arrayMovies);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

}
