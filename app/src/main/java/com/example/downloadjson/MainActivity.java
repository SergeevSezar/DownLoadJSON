package com.example.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadJSONTask task = new DownloadJSONTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=Moscow&appid=6a2a6408e25378d2153ccb8ac7ae0aac");
    }

    private static class DownloadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
               if (urlConnection != null) {
                   urlConnection.disconnect();
               }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String name = jsonObject.getString("name");
                String timeZone = jsonObject.getString("timezone");

                JSONObject main = jsonObject.getJSONObject("main");
                String temp = main.getString("temp");
                String pressure = main.getString("pressure");

                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject weather = jsonArray.getJSONObject(0);
                String Main  = weather.getString("main");
                String description = weather.getString("description");

                Log.i("My result","Город: " + name + " " + "Часовой пояс: " + timeZone + " " + "Температура: " + temp + " " + "Давление воздуха: " + pressure + " " + "Погода: " + Main + " " + description);
                Log.i("My result","Город: " + name + " " + "Часовой пояс: " + timeZone + " " + "Температура: " + temp + " " + "Давление воздуха: " + pressure);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}