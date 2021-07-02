package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Earthquake_data extends AppCompatActivity {
    private EarthArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_data);
        ListView earthquakeView = (ListView) findViewById(R.id.list_view);
        mAdapter = new EarthArrayAdapter(this, new ArrayList<Earthquake>());
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();
        earthquakeView.setAdapter(mAdapter);


    }

    private class TsunamiAsyncTask extends AsyncTask<URL, Void, List<Earthquake>> {

        private static final String EARTHQUAKE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2021-06-01&limit=50";
        @Override
        protected List<Earthquake> doInBackground(URL... urls) {
            URL url = createUrl(EARTHQUAKE_URL);
            String jsonResponse = "";
            try {
                jsonResponse = performHttpRequest(url);
            }
            catch(IOException e){

            }

            List<Earthquake> list = null;
            if (!jsonResponse.isEmpty()) {
                list = extractFeaturesFromJson(jsonResponse);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            mAdapter.clear();
            mAdapter.addAll(earthquakes);
        }

        private String performHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout((10000));
                connection.setConnectTimeout(15000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            }
            catch(IOException e) {
                Log.e("HTTRequest", "Input output exception", e);
            }

            finally {
                if(connection != null){
                    connection.disconnect();
                }
                if(inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private URL createUrl(String stringurl) {
            URL url = null;
            try {
                url = new URL(stringurl);
            }
            catch(MalformedURLException e){
                Log.e("HttpRequest", "Error with creating URL", e);
            }

            return url;
        }

        private List<Earthquake> extractFeaturesFromJson(String earthquakeJson){
            List<Earthquake> list = new ArrayList<Earthquake>();

            try{
                JSONObject baseJsonObject = new JSONObject(earthquakeJson);
                JSONArray earthquakeArray = baseJsonObject.getJSONArray("features");

                for ( int i = 0; i < earthquakeArray.length(); i++) {
                    JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                    JSONObject properties = currentEarthquake.getJSONObject("properties");
                    double magnitude = properties.getDouble("mag");
                    String location = properties.getString("place");
                    long time = properties.getLong("time");
                    String url = properties.getString("url");
                    list.add(new Earthquake(magnitude, location, time, url));
                }
            }
            catch(JSONException e){
                Log.e("Tsunami Async Task", "There was a Json exception", e);
            }
            return list;
        }


    }

}