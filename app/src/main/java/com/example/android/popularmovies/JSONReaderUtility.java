package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Vale on 29/01/2017.
 */

public class JSONReaderUtility {

    public static String[] getStringDataFromJSON(Context context,String jsonString)
        throws JSONException{
        String PAGE = "page", RES="results",P_PATH="poster_path",AD="adult",OVER="overview",
                DATE="release_date",GEN="genre_ids",ID="id",OR_TI="original_title",OR_LA="original_language",
                TIT="title",B_PATH="backdrop_path",POP="popularity",V="vote_count",VID="video",V_A="vote_average";

        String[] parsedData=null;

        JSONObject jsonRootObject = new JSONObject(jsonString);
        //JSONArray jsonArray = new JSONArray(jsonString);
        //jsonObject=jsonArray.getJSONObject(0);
        //Log.d("LALA","LAND"+jsonRootObject.has(PAGE));

       /* if(jsonObject.has(PAGE)){
            int errorCode = jsonObject.getInt(PAGE);

            switch (errorCode){
                case HttpURLConnection.HTTP_OK:{Log.d("ERR","1");} break;
                case HttpURLConnection.HTTP_NOT_FOUND: {Log.d("ERR","2");return null;} //location invalid
                default:
                    Log.d("ERR","3");
                    return null;

            }
        }*/

        JSONArray mMovieArray = jsonRootObject.getJSONArray(RES);
        parsedData= new String[mMovieArray.length()];

        for(int i=0;i<mMovieArray.length();i++){
            JSONObject jsonObject = mMovieArray.getJSONObject(i);

            parsedData[i]= jsonObject.getString(P_PATH)+"$"     //0
                    +jsonObject.getString(OVER)+"$"             //1
                    +jsonObject.getString(DATE)+"$"             //2
                    +jsonObject.getString(ID)+"$"               //3
                    +jsonObject.getString(OR_TI)+"$"            //4
                    +jsonObject.getString(OR_LA)+"$"            //5
                    +jsonObject.getString(TIT)+"$"              //6
                    +jsonObject.getString(B_PATH)+"$"           //7
                    +jsonObject.getDouble(POP)+"$"              //8
                    +jsonObject.getDouble(V_A)+"$"              //9
                    +jsonObject.getBoolean(VID)+"$"             //10
                    +jsonObject.getString(V)+"$";               //11
            //Log.d("LALALAND",parsedData[i].toString());
        }
        //Log.d("BLABLA","LENGTH "+parsedData.length);
        return parsedData;
    }
}
