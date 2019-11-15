package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterDetailsScreen extends RecyclerView.Adapter<RecyclerViewAdapterDetailsScreen.ViewHolder> {
    private Context mContext;
    private int numberOfButtons;
    private String [] trailers;


    public RecyclerViewAdapterDetailsScreen(Context mContext, int numberOfButtons,String [] trailers) {
        this.numberOfButtons=numberOfButtons;
        this.mContext = mContext;
        this.trailers=trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem2, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {



        String trailer ="Trailer " + (position+1);
        holder.mTextview.setText(trailer);
        holder.mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                Toast.makeText(mContext, trailers[position], Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers[position]));
                mContext.startActivity(browserIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return trailers.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Button mbutton;
        TextView mTextview;
        RelativeLayout parentlayout;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mbutton=itemView.findViewById(R.id.button);
            parentlayout = itemView.findViewById(R.id.parent_layout2);
            mTextview=itemView.findViewById(R.id.textview);
        }
    }
}
