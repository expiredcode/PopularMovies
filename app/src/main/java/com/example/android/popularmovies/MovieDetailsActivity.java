package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Iterator;

/**
 * Created by Vale on 30/01/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity{

    private TextView mYear, mDescription,mTitle,mVoteAv,mSaveFavorite;
    private ImageView wide, poster;
    private String[] data;
    private String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetails_layout);

        wide = (ImageView) findViewById(R.id.dt_wide);
        poster = (ImageView) findViewById(R.id.dt_poster);

        mYear = (TextView) findViewById(R.id.dt_year);
        mDescription = (TextView) findViewById(R.id.dt_description);
        mTitle = (TextView) findViewById(R.id.dt_title);
        mVoteAv = (TextView) findViewById(R.id.dt_vote);
        mSaveFavorite = (TextView) findViewById(R.id.dt_save);

        Intent intent = getIntent();
        if(intent!=null)
            if(intent.hasExtra(Intent.EXTRA_TEXT)){
                message = intent.getStringExtra(Intent.EXTRA_TEXT);

                processMessage();
            }
    }

    private void processMessage(){
        data =message.split("\\$") ;
        String baseImageUrl = "http://image.tmdb.org/t/p/w342/";
       // wide.setImageResource(R.drawable.placeholder);
        loadPictureIntoImageView(wide,baseImageUrl+data[7]);

        mTitle.setText(data[6]);

        loadPictureIntoImageView(poster,baseImageUrl+data[0]);

        mYear.setText(data[2].substring(0,4));
        mVoteAv.setText(data[9]+"/10");
        mDescription.setText(data[1]);

        checkFavorites();
    }

    private void loadPictureIntoImageView(ImageView imageView,String imageurl){

        Picasso.with(imageView.getContext()).load(imageurl)       //"http://image.tmdb.org/t/p/w342//vR9YvUJCead23MOWwVzv9776eb1.jpg")
                .error(R.drawable.error)
                .into(imageView);
    }

    private void checkFavorites(){
        Iterator iterator = PopularMoviesActivity.getFav().iterator();

        while(iterator.hasNext()){
            if(((String)iterator.next()).equals(data[3])){
                markAsFavorite();
            }
        }
    }

    private void markAsFavorite(){
        mSaveFavorite.setBackgroundColor(Color.GRAY);
        mSaveFavorite.setText("Saved as Favorite");
    }

    public void saveFavorite(View view){
        markAsFavorite();
        PopularMoviesActivity.addFav(data[3]);
    }
}
