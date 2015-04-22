package com.lionel.claudon.android.app;

import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by lclaudon on 17.04.2015.
 */
public class WeatherDataParser {

    private static final String LOG_TAG = WeatherDataParser.class.getSimpleName();

    private String getReadableDateString(long time) {
        SimpleDateFormat dF = new SimpleDateFormat("EEE MMM dd");
        return dF.format(time);
    }

    private String formatMinMaxTemperatures(double min, double max) {
        return Math.round(min) + " / " + Math.round(max);
    }

    public String[] getWeatherDataFromJson(String jsonStr, int numDays) throws JSONException {
        String OWM_LIST="list";
        String OWM_TEMP="temp";
        String OWM_WEATHER="weather";
        String OWM_MIN="min";
        String OWM_MAX="max";
        String OWM_DESCRIPTION="main";

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray weatherArray = jsonObject.getJSONArray(OWM_LIST);

        Time time = new Time();
        time.setToNow();

        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), time.gmtoff);

        time = new Time();

        String[] resultStrs = new String[numDays];

        for(int i = 0; i<numDays; i++) {
            String result = "";
            String day;
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime;
            dateTime = time.setJulianDay(julianStartDay + i);
            result += getReadableDateString(dateTime);

            result += " - " + dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0).getString(OWM_DESCRIPTION);


            double minTemp = dayForecast.getJSONObject(OWM_TEMP).getDouble(OWM_MIN);
            double maxTemp = dayForecast.getJSONObject(OWM_TEMP).getDouble(OWM_MAX);
            result +=  " - " + formatMinMaxTemperatures(minTemp, maxTemp);

            resultStrs[i] = result;
        }

        for(String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }

        return resultStrs;
    }
}
