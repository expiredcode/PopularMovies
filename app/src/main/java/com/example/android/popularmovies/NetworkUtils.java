package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Vale on 29/01/2017.
 */

public class NetworkUtils {
    final static String TAG = NetworkUtils.class.getSimpleName();

    final static String API_KEY = "XXXXX";
    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";



    public static URL buildListUrl(String query){
        Uri builrUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter("api_key",API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(builrUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally{
            urlConnection.disconnect();
        }
    }
}
