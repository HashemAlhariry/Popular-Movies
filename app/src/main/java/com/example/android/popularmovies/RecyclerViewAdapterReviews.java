package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterReviews extends RecyclerView.Adapter<RecyclerViewAdapterReviews.ViewHolder> {

    Context mContext;
    String []authors;
    String []content;

    public RecyclerViewAdapterReviews(Context mContext, String[] authors, String[] content) {
        this.mContext = mContext;
        this.authors = authors;
        this.content = content;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReviews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReviews.ViewHolder holder, int position) {

        holder.mTextViewAuthor.setText(authors[position]);
        holder.mTextViewContent.setText(content[position]);

    }

    @Override
    public int getItemCount() {
        return authors.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTextViewAuthor;
        TextView mTextViewContent;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mTextViewAuthor=(TextView) itemView.findViewById(R.id.author);
            mTextViewContent=(TextView)itemView.findViewById(R.id.content);
        }
    }



}

