package com.example.android.popularmovies;

public class ListItem
{
    private String original_title;
    private String poster_path;
    private String overview;
    private int vote_average;
    private String release_date;

    public ListItem(String original_title, String poster_path, String overview, int vote_average, String release_date)
    {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public int getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }
}
