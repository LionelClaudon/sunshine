package com.lionel.claudon.android.app.sunshine;

/**
 * Created by lionel on 16/04/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, new ArrayList<String>());

        ListView forecastListView = (ListView) rootView.findViewById(R.id.listView_forecast);
        forecastListView.setAdapter(forecastAdapter);

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = forecastAdapter.getItem(position);
                Intent showDetailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(showDetailIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            updateWeather();
            return true;
        } else if (id == R.id.action_view_location) {
            return viewUserLocation();
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private boolean viewUserLocation() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String locationPref = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Uri geo = Uri.parse("geo:0,0?git status").buildUpon()
                .appendQueryParameter("q", locationPref).build();

        Intent viewLocationIntent = new Intent(Intent.ACTION_VIEW);
        viewLocationIntent.setData(geo);


        if(viewLocationIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(viewLocationIntent);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private void updateWeather() {
        FetchWeatherTask fetchTask = new FetchWeatherTask(getActivity(), forecastAdapter);

        //Get the locaton from settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String locationPref = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Log.i(LOG_TAG, "Fetching forecast for location " + locationPref);

        fetchTask.execute(locationPref);
    }
}
