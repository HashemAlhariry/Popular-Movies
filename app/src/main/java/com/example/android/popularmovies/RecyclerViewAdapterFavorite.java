package com.example.android.popularmovies;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.DataBase.FavoriteMovies;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerViewAdapterFavorite.ViewHolder> {
    private List<FavoriteMovies> favoriteMovies;
    private Context context;
    public RecyclerViewAdapterFavorite(Context context, List<FavoriteMovies> favoriteMovies) {
        this.context= context;
        this.favoriteMovies = favoriteMovies;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    public List<FavoriteMovies> getTasks() {
        return favoriteMovies;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



            String moviePosterURL = favoriteMovies.get(position).getImage();

            if (!TextUtils.isEmpty(moviePosterURL)) {
                //System.out.println("movieposterURL" + moviePosterURL);
                Picasso.get().load(moviePosterURL).into(holder.imageView);
            }
            holder.title.setText(favoriteMovies.get(position).getTitle());
            holder.id.setText(Integer.toString(favoriteMovies.get(position).getId()));
            System.out.println("Froooooom on bindddddddddddd"+favoriteMovies.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView id;
        RelativeLayout parentlayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentlayout=itemView.findViewById(R.id.favoritelayout);
            imageView = itemView.findViewById(R.id.movieimage);
            title=itemView.findViewById(R.id.favoriteMovieTitle);
            id=itemView.findViewById(R.id.movieid);
        }
    }

    public void setFavoriteMovies(List<FavoriteMovies> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }
}
