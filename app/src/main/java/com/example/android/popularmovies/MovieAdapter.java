package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Vale on 29/01/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder>{

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private MovieAdapterOnClickHandler mClickHandler;


    private int mNumberItems;


    public interface MovieAdapterOnClickHandler{ void onClick(String str);};

    MovieAdapter(int itemsNumber,MovieAdapterOnClickHandler handler){
        mNumberItems=itemsNumber;
        mClickHandler=handler;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG,"INSIDE ONCREATEVIEWHOLDER");
        Context context = parent.getContext();
        int layoutIdForImageView=R.layout.poster_image;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForImageView,parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        //Log.d(TAG, "INSIDE ONBINDVIEWHOLDER");

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        public ImageViewHolder (View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.tv_poster);

            view.setOnClickListener(this);
        }

        void bind(int i){

            String[] data = PopularMoviesActivity.getDataArray();
            String base= "http://image.tmdb.org/t/p/w342/";
            if(data==null)
                return;
            String [] str =data[i].split("\\$");
            String imageurl =  base+str[0];


            Picasso.with(imageView.getContext()).load(imageurl)       //"http://image.tmdb.org/t/p/w342//vR9YvUJCead23MOWwVzv9776eb1.jpg")
                    .error(R.drawable.error)
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            //Log.d(TAG,"inside onClick");
            int pos = getAdapterPosition();
            mClickHandler.onClick(PopularMoviesActivity.getDataArray()[pos]);
        }
    }

    public void update(){
        notifyDataSetChanged();
    }

}
