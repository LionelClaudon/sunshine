package com.lionel.claudon.android.app;

/**
 * Created by lionel on 16/04/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

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


        List<String> forecasts = new ArrayList<String>();
        forecasts.add("Today - Sunny - 15/23");
        forecasts.add("Tomorrow - Cloudy - 12/15");
        forecasts.add("Weds - Foggy - 5/12");
        forecasts.add("Thurs - Rainy - 12/15");
        forecasts.add("Fri - Foggy - 5/15");
        forecasts.add("Sat - Sunny - 21/27");
        forecasts.add("Sun - Rainy - 1/2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, forecasts);

        ListView forecastListView = (ListView) rootView.findViewById(R.id.listView_forecast);
        forecastListView.setAdapter(adapter);


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
            FetchWeatherTask fetchTask = new FetchWeatherTask();
            fetchTask.execute();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
