package com.lionel.claudon.android.app.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import com.lionel.claudon.android.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by lclaudon on 17.04.2015.
 */
public class WeatherDataParser {

    private static final String LOG_TAG = WeatherDataParser.class.getSimpleName();
    private final Context context;

    public WeatherDataParser(Context context) {
        this.context = context;
    }

    private String getReadableDateString(long time) {
        SimpleDateFormat dF = new SimpleDateFormat("EEE MMM dd");
        return dF.format(time);
    }

    private String formatMinMaxTemperatures(double min, double max) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String unitPref = prefs.getString(context.getString(R.string.pref_unit_key), context.getString(R.string.pref_unit_value_metric));

        if(unitPref.equals(context.getString(R.string.pref_unit_value_imperial))) {
            min = min * 1.8 + 32;
            max = max * 1.8 + 32;
        } else if(!unitPref.equals(context.getString(R.string.pref_unit_value_metric))) {
            Log.i(LOG_TAG, "Unit type not found: " + unitPref);

        }

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

        return resultStrs;
    }
}
