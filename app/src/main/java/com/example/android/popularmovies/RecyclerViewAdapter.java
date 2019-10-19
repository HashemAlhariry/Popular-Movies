package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<ListItem> moviesArray;
    private Context mContext;


    public RecyclerViewAdapter(Context mContext, ArrayList<ListItem> movies) {
        this.moviesArray = movies;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoul_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /** MoviesArray return description of movie , getPoster_path() path return URL Of the posterfil **/

        String moviePosterURL = moviesArray.get(position).getPoster_path();

        if (!TextUtils.isEmpty(moviePosterURL)) {
            System.out.println("movieposterURL" + moviePosterURL);
            Picasso.get().load(moviePosterURL).into(holder.imageView);
        }

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, moviesArray.get(position).getOriginal_title(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailsScreen.class);
                intent.putExtra("image_thumbnail", moviesArray.get(position).getPoster_path());
                intent.putExtra("original_title", moviesArray.get(position).getOriginal_title());
                intent.putExtra("overview", moviesArray.get(position).getOverview());
                intent.putExtra("vote_average", moviesArray.get(position).getVote_average());
                intent.putExtra("release_date", moviesArray.get(position).getRelease_date());

                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        RelativeLayout parentlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            parentlayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
