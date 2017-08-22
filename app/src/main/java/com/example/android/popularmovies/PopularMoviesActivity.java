package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class PopularMoviesActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private static final int NUM_LIST_ITEMS = 20;

    private ProgressBar mProgressBar;

    private MovieAdapter mAdapter;

    private RecyclerView mPosters;

    private static String[] dataArray=null;

    private PopupMenu popup;

    private TextView t;

    public static List<String> favoriteMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        favoriteMovies=new ArrayList<String>();

        mPosters = (RecyclerView) findViewById(R.id.tv_movie_posters);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);

        t = (TextView)findViewById(R.id.tv_error);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        mPosters.setLayoutManager(layoutManager);
        mPosters.setHasFixedSize(true);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS,this);

        //loadFavorites();

        mPosters.setAdapter(mAdapter);
        if(checkNet())
            getPopular();

    }

    private boolean checkNet(){
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if(networkInfo!=null&&  networkInfo.isConnectedOrConnecting()) {
            if(t.getVisibility()==View.VISIBLE)
                t.setVisibility(View.GONE);

            return true;
        }
        else {

            t.setVisibility(View.VISIBLE);
            t.setText("ERROR: \n Internet connection is needed");
            t.setTextSize(30.0f);
            t.setGravity(Gravity.CENTER);

            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pop_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.s_showPopular:{
                if(checkNet())
                    getPopular();
                return true;
            }
            case R.id.s_showTopRated:{
                if(checkNet())
                    getTopRated();
                return true;
            }
            case R.id.s_fav:{
                break; //TODO read fav json
            }

        }

        return super.onOptionsItemSelected(item);
    }


    private void getPopular(){
        try {
            dataArray = new FetchMovieData().execute("popular").get();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getTopRated(){
        try {
            dataArray = new FetchMovieData().execute("top_rated").get();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(String str) {

        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,str);
        startActivity(intent);

    }

    public class FetchMovieData extends AsyncTask<String,Void,String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String[] doInBackground(String... params) {
            if(params.length==0)
                return null;
            String preference = params[0];
            URL movieRequest= NetworkUtils.buildListUrl(preference);
            try{
                String data = NetworkUtils.getResponseFromHttpUrl(movieRequest);

                return JSONReaderUtility.getStringDataFromJSON(PopularMoviesActivity.this,data);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter.update();
        }
    }

    public static String[] getDataArray(){
        return dataArray;
    }

    public void loadFavorites(){
        String filename = "myfavs.txt";
        String str="";
        favoriteMovies.clear();

        FileInputStream in;
        try {
            in = openFileInput(filename);
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if(hasInput){
               str=scanner.next().toString();
            }else{
                str="";
            }
            String[] s =str.split("\\$");
            for(int i=0;i<s.length;i++){
                favoriteMovies.add(s[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void writeFavorites(){
        String filename = "myfavs.txt";
        String s = "";

        Iterator i = favoriteMovies.iterator();
        while(i.hasNext()){
            s= ((String)i.next())+"$";
        }

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List getFav(){
        return favoriteMovies;
    }

    public static void addFav(String movieId){
        favoriteMovies.add(movieId);
    }

}
