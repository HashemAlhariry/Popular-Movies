package com.example.android.popularmovies.DataBase;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao
{

    @Query("SELECT * From movies")
    LiveData<List<FavoriteMovies>> loadAllTasks();

    @Query("DELETE From movies")
    void deleteallTasks();

    @Query("DELETE FROM movies Where movies.title = :title")
    void deleteMoviebyTitle(String title);

    @Insert
    void insertMovie(FavoriteMovies favoriteMovies);

    @Delete
    void deleteMovie(FavoriteMovies favoriteMovies);



}
