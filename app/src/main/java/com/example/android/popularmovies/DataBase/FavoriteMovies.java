package com.example.android.popularmovies.DataBase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class FavoriteMovies
{


    @PrimaryKey
    private int id;
    private  String image;
    private  String title;
    private  String overview;

    @Ignore
    public FavoriteMovies(String image, String title, String overview) {
        this.image = image;
        this.title = title;
        this.overview = overview;
    }

    public FavoriteMovies(int id, String image, String title, String overview) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }




}
