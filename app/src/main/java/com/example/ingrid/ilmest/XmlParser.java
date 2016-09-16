package com.example.ingrid.ilmest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ingrid on 10.09.2016.
 */

public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public ArrayList<Forecast> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<Forecast> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Forecast> forecasts = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "forecasts");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the forecast tag and adds Forecasts to ArrayList
            if (name.equals("forecast")) {
                forecasts.addAll(readForecast(parser));
            } else {
                skip(parser);
            }
        }
        return forecasts;
    }



    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private ArrayList<Forecast> readForecast(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "forecast");
        ArrayList<Forecast> forecasts = new ArrayList<>();
        // gets the date in forecast tag attribute
        String date = parser.getAttributeValue(0);
        String finalDate;
        String[] parts = date.split("-");
        // makes it look more user friendly (DD.MM)
        finalDate = parts[2] + "." + parts[1];

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            switch (name) {
                // works with tag "night"
                case "night":
                    Forecast night = readNight(parser, finalDate);
                    forecasts.add(night);
                    break;
                // works with tag "day"
                case "day":
                    Forecast day = readDay(parser, finalDate);
                    forecasts.add(day);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return forecasts;
    }





    private Forecast readNight(XmlPullParser parser, String date) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "night");
        String dayOrNight = "night";
        String tempmin = "";
        String tempmax = "";
        String text = null;
        String windmin = null;
        String windmax = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case "tempmin":
                    tempmin = readTempmin(parser);
                    break;
                case "tempmax":
                    tempmax = readTempmax(parser);
                    break;
                case "text":
                    text = readDesc(parser);
                    break;
                case "wind":
                    String[] wind = readWind(parser).split(",");
                    if(windmin == null) {
                        windmin = wind[0];
                        windmax = wind[1];
                    }else{
                        if(Integer.parseInt(wind[0]) < Integer.parseInt(windmin)){
                            windmin = wind[0];
                        }
                        if(Integer.parseInt(wind[1])> Integer.parseInt(windmax)){
                            windmax = wind[1];
                        }
                    }
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Forecast(date, dayOrNight, tempmin, tempmax, text, windmin, windmax);
    }

    private Forecast readDay(XmlPullParser parser, String date) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "day");
        String dayOrNight = "day";
        String tempmin = "";
        String tempmax = "";
        String text = null;
        String windmin = null;
        String windmax = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case "tempmax":
                    tempmax = readTempmax(parser);
                    break;
                case "tempmin":
                    tempmin = readTempmin(parser);
                    break;
                case "text":
                    text = readDesc(parser);
                    break;
                case "wind":
                    String[] wind = readWind(parser).split(",");
                    if(windmin == null) {
                        windmin = wind[0];
                        windmax = wind[1];
                    }else{
                        if(Integer.parseInt(wind[0]) < Integer.parseInt(windmin)){
                            windmin = wind[0];
                        }
                        if(Integer.parseInt(wind[1])> Integer.parseInt(windmax)){
                            windmax = wind[1];
                        }
                    }
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Forecast(date, dayOrNight, tempmin, tempmax, text, windmin, windmax);
    }


    // Processes tempmin tags in the feed.
    private String readTempmin(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "tempmin");
        String tempString = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "tempmin");
        return tempString;
    }

    // Processes tempmax tags in the feed.
    private String readTempmax(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "tempmax");
        String tempString = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "tempmax");
        return tempString;
    }

    // Processes summary tags in the feed.
    private String readDesc(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "text");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "text");
        return text;
    }

    private String readWind(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "wind");
        String wind = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case "speedmin":
                    wind = readWindmin(parser);
                    break;
                case "speedmax":
                    wind += "," + readWindmax(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return wind;
    }

    private String readWindmin(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "speedmin");
        String windString = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "speedmin");
        return windString;
    }

    private String readWindmax(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "speedmax");
        String windString = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "speedmax");
        return windString;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
