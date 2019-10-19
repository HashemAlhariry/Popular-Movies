package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




public class NetworkUtilites extends AsyncTask<Void,Void,String>
{



    private String MoviesURL="http://api.themoviedb.org/3/movie/popular?api_key=95bc3cb8063677fac3648b921ca60b46";

        private String sent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


    @Override
        protected String doInBackground(Void... strings)
        {

            try
            {

                URL url = new URL(MoviesURL);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                sent=StreamtoString(in);
                in.close();


            }
            catch (Exception e)
            {

            }
            return sent;
        }


        @Override
        protected void onPostExecute(String  s)
        {

            super.onPostExecute(String.valueOf(s));
        }


        public String StreamtoString(InputStream inputStream)
        {
            BufferedReader bureader= new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String Text = "";
            try{
                while((line=bureader.readLine())!=null)
                {
                    Text+=line;
                }
            } catch (Exception e) {

            }

            return  Text;

        }




    public void setMoviesURL(String moviesURL) {
        MoviesURL = moviesURL;
    }





}
