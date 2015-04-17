package com.lionel.claudon.android.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lionel on 16/04/15.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            // Default values
            String postalCode = params[0];
            String mode = "json";
            String units = "metric";
            int daysCount = 7;

            String QUERY_POSTAL_CODE="q";
            String QUERY_MODE="mode";
            String QUERY_UNITS="units";
            String QUERY_DAYS_NUMBER="cnt";
            String BASE_FORECAST_FETCH_URI="http://api.openweathermap.org/data/2.5/forecast/daily";

            Uri builtUri = Uri.parse(BASE_FORECAST_FETCH_URI).buildUpon()
                    .appendQueryParameter(QUERY_POSTAL_CODE, postalCode)
                    .appendQueryParameter(QUERY_MODE, mode)
                    .appendQueryParameter(QUERY_UNITS, units)
                    .appendQueryParameter(QUERY_DAYS_NUMBER, String.valueOf(daysCount)).build();


            Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.v(LOG_TAG, "JSON received: " + forecastJsonStr);

        return forecastJsonStr;
    }
}
