package com.example.ingrid.ilmest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        loadPage();

    }

    public void initUI(ArrayList<Forecast> results){
        ArrayList<String> dates = new ArrayList<>();
        // ArrayList only for dates so it's easier to access them for naming tabs
        for (Forecast result : results) {
            if(!dates.contains(result.date)){
                dates.add(result.date);
            }
        }

        // for PagerAdapter
        FragmentManager manager=getSupportFragmentManager();
        PagerAdapter adapter=new PagerAdapter(manager, dates, results);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // Uses AsyncTask to download the XML feed
    public void loadPage() {
        if(isOnline()){
            new DownloadXmlTask().execute(getResources().getString(R.string.URL));
        }else{
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.connection_error);
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        }

    }


    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<Forecast>> {
        @Override
        protected ArrayList<Forecast> doInBackground(String... urls) {
            ArrayList<Forecast> fromFeed = new ArrayList<>();
            try {
                fromFeed = loadXmlFromNetwork(urls[0]);
                return fromFeed;
            } catch (IOException e) {
                System.out.println(getResources().getString(R.string.connection_error));
            } catch (XmlPullParserException e) {
                System.out.println(getResources().getString(R.string.xml_error));
            }
            return fromFeed;
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> results) {
            initUI(results);
        }

        private ArrayList<Forecast> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;

            XmlParser parser = new XmlParser();
            ArrayList<Forecast> forecasts = null;

            try {

                stream = downloadUrl(urlString);
                forecasts = parser.parse(stream);

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return forecasts;
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(getString(R.string.GET));
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }

}


