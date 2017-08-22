package com.example.android.quakereport_V2;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

import static android.content.ContentValues.TAG;
//import static com.example.android.quakereport.QueryUtils.readInputStream;
import static com.example.android.quakereport_V2.MainEarthquakeActivity.LimitReached;
import static com.example.android.quakereport_V2.R.id.mag;
import static com.example.android.quakereport_V2.R.string.earthquake;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building in the Url:", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e(TAG, "httpRequest: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem getting earthquake json response.: " + e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed)
     */

//    private QueryUtils() {
//
//    }

    /**
     * Return a list of {@link Word} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Word> extractEarthquakesFromJson(String earthquakeJson) {

        if (TextUtils.isEmpty(earthquakeJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Word> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject baseJsonResponse = new JSONObject(earthquakeJson);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

//            if(earthquakeArray.length()>2000) {
//                LimitReached=true;
//            }

            for (int i = 0; i < earthquakeArray.length(); i++) {

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                Long time = properties.getLong("time");
                String url = properties.getString("url");

                JSONObject geometry = currentEarthquake.getJSONObject("geometry");
                JSONArray cordinates=geometry.getJSONArray("coordinates");
                Double longi=cordinates.getDouble(0);
                Double lat=cordinates.getDouble(1);

                Word earthquake = new Word(mag, place, time, url,lat,longi);
                earthquakes.add(earthquake);
                Log.i(TAG, "extractEarthquakes:" + earthquake.getTimeInMilliseconds() + " " + earthquake.getPlace() + " " + earthquake.getMag()+" "+earthquake.getLat()+" "+earthquake.getLongi());

                if(i>9998)
                    break;
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    public static List<Word> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "http request error: " + e);
        }

        List<Word> earthquakes = extractEarthquakesFromJson(jsonResponse);

        return earthquakes;
    }
}