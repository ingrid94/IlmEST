package com.example.ingrid.ilmest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ingrid on 14.09.2016.
 */

//made into parcelable to send data from PagerAdapter to ForecastFragment
public class Forecast implements Parcelable {
    public final String date;
    public final String daynight;
    public final String tempmin;
    public final String tempmax;
    public final String text;
    public final String windmin;
    public final String windmax;

    public Forecast(String date, String daynight, String tempmin, String tempmax, String text, String windmin, String windmax) {
        this.date = date;
        this.daynight = daynight;
        this.tempmin = tempmin;
        this.tempmax = tempmax;
        this.text = text;
        this.windmin = windmin;
        this.windmax = windmax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.daynight);
        dest.writeString(this.tempmin);
        dest.writeString(this.tempmax);
        dest.writeString(this.text);
        dest.writeString(this.windmin);
        dest.writeString(this.windmax);
    }

    protected Forecast(Parcel in) {
        this.date = in.readString();
        this.daynight = in.readString();
        this.tempmin = in.readString();
        this.tempmax = in.readString();
        this.text = in.readString();
        this.windmin = in.readString();
        this.windmax = in.readString();
    }

    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
}