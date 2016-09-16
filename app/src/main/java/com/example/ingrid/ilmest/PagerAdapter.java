package com.example.ingrid.ilmest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Ingrid on 14.09.2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Forecast> forecasts = new ArrayList<>();

    public PagerAdapter(FragmentManager fm, ArrayList<String> resultDates, ArrayList<Forecast> resultForecasts) {
        super(fm);
        dates = resultDates;
        forecasts = resultForecasts;
    }

    @Override
    public Fragment getItem(int position) {

        // makes fragments for different dates. Depending on what date user wants to see.
        // sends arguments to ForecastFragment
        Fragment frag=null;
        switch (position){
            case 0:
                frag= ForecastFragment.newInstance(forecasts.get(0), forecasts.get(1));
                break;
            case 1:
                frag = ForecastFragment.newInstance(forecasts.get(2), forecasts.get(3));
                break;
            case 2:
                frag = ForecastFragment.newInstance(forecasts.get(4), forecasts.get(5));
                break;
            case 3:
                frag = ForecastFragment.newInstance(forecasts.get(6), forecasts.get(7));
                break;
        }
        return frag;
    }

    @Override
    // how many tabs there are
    public int getCount() {
        return 4;
    }

    @Override
    // names tabs with dates
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title = dates.get(0);
                break;
            case 1:
                title = dates.get(1);
                break;
            case 2:
                title = dates.get(2);
                break;
            case 3:
                title = dates.get(3);
                break;
        }

        return title;
    }
}
