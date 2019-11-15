package com.example.android.popularmovies;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmovies.DataBase.AppDatabase;
import com.example.android.popularmovies.DataBase.FavoriteMovies;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class MainActivity extends AppCompatActivity
{
    private ArrayList<ListItem> arrayMovies = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewFavorite;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapterFavorite adapterFavorite;
    private String dataRetrieved;
    private static final String TAG = "MAINACTIVITY";
    private String top_rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=95bc3cb8063677fac3648b921ca60b46";
    private String MoviesURL = "http://api.themoviedb.org/3/movie/popular?api_key=95bc3cb8063677fac3648b921ca60b46";
    private AppDatabase mDb;
   // private List<FavoriteMovies>favoriteMovies=new ArrayList<>();
    private static boolean checkForToprated=false;
    private static boolean checkForFavorites=false;
    private static boolean checkForMostPopular=true;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerViewFavorite = findViewById(R.id.recycler_view);
        prefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavoriteMovies> tasks = adapterFavorite.getTasks();
                        prefs.edit().putBoolean(tasks.get(position).getTitle(), true).apply();
                        mDb.taskDao().deleteMovie(tasks.get(position));
                        // COMPLETED (6) Remove the call to retrieveTasks
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerViewFavorite);

        if(checkForMostPopular)
        {
            networkUtilites(false);
            getArrayMovies();
            RecyclerViewAdapter();
        }
        if(checkForToprated)
        {
            networkUtilites(true);
            getArrayMovies();
            RecyclerViewAdapter();

        }
        if(checkForFavorites)
        {
            mDb = AppDatabase.getInstance(getApplicationContext());
            final LiveData<List<FavoriteMovies>> favoriteMovies= mDb.taskDao().loadAllTasks();
            favoriteMovies.observe(this, new Observer<List<FavoriteMovies>>()
            {
                @Override
                public void onChanged(List<FavoriteMovies> favoriteMovies) {
                    Log.d("From on CHANGED", "onChanged:  receiving database update from liveData");
                    RecyclerViewAdapterFavorite(favoriteMovies);
                }
            });


        }






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
                checkForMostPopular=true;
                checkForFavorites=false;
                checkForToprated=false;

                return true;

            case R.id.top_rated:
                Toast.makeText(this, "TopRatedClicked", Toast.LENGTH_SHORT).show();
                arrayMovies.clear();
                networkUtilites(true);
                getArrayMovies();
                RecyclerViewAdapter();
                checkForFavorites=false;
                checkForToprated=true;
                checkForMostPopular=false;

                return true;

            case R.id.favorite:
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();

                mDb = AppDatabase.getInstance(getApplicationContext());
                final LiveData<List<FavoriteMovies>> favoriteMovies= mDb.taskDao().loadAllTasks();
                favoriteMovies.observe(this, new Observer<List<FavoriteMovies>>() {
                    @Override
                    public void onChanged(List<FavoriteMovies> favoriteMovies) {
                        Log.d("From on CHANGED", "onChanged:  receiving database update from liveData");
                        RecyclerViewAdapterFavorite(favoriteMovies);
                    }
                });

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    // Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        // Here is where you'll implement swipe to delete
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                int position = viewHolder.getAdapterPosition();
                                List<FavoriteMovies> tasks = adapterFavorite.getTasks();
                                mDb.taskDao().deleteMovie(tasks.get(position));
                                // COMPLETED (6) Remove the call to retrieveTasks
                            }
                        });
                    }
                }).attachToRecyclerView(mRecyclerViewFavorite);

                checkForFavorites=true;
                checkForToprated=false;
                checkForMostPopular=false;

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

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteMovies = mDb.taskDao().loadAllTasks();
                for (int i = 0; i < favoriteMovies.size(); i++) {
                    System.out.println(favoriteMovies.get(i).getTitle() + "   " + favoriteMovies.get(i).getOverview());
                }
            }

        });
        t.start();
        */

    }

    public void getArrayMovies() {

        try {


            if (!dataRetrieved.equals(null)) {
                JSONObject parentObject = new JSONObject(dataRetrieved);
                JSONArray parentArray = parentObject.getJSONArray("results");

                //System.out.println(parentArray.length());
                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject movieDetails = parentArray.getJSONObject(i);
                    String poster_path = "http://image.tmdb.org/t/p/w500";
                    poster_path += movieDetails.getString("poster_path");
                    String original_title = movieDetails.getString("original_title");
                    String overview = movieDetails.getString("overview");
                    int vote_average = movieDetails.getInt("vote_average");
                    String release_date = movieDetails.getString("release_date");
                    int id =movieDetails.getInt("id");

                    ListItem ob = new ListItem(original_title, poster_path, overview, vote_average, release_date,id);
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

    public void RecyclerViewAdapterFavorite(List<FavoriteMovies> favoriteMovies)
    {


        System.out.println(favoriteMovies.size() + "--------------------------------------------------------");

        for (int i = 0; i < favoriteMovies.size(); i++) {
            System.out.println("IN RECYCLER VIEW ADAPTER FAVORITE "+favoriteMovies.get(i).getTitle() + "   " + favoriteMovies.get(i).getOverview());
        }


           adapterFavorite = new RecyclerViewAdapterFavorite( this, favoriteMovies);
           mRecyclerViewFavorite = findViewById(R.id.recycler_view);
           mRecyclerViewFavorite.setAdapter(adapterFavorite);
           mRecyclerViewFavorite.setHasFixedSize(true);
           mRecyclerViewFavorite.setLayoutManager(new LinearLayoutManager(this));


    }





}
