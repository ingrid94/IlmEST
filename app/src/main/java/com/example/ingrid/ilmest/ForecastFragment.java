package com.example.ingrid.ilmest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ForecastFragment extends Fragment {


    public ForecastFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(Forecast forecast_night, Forecast forecast_day) {
        ForecastFragment f = new ForecastFragment();
        // gets arguments from PagerAdapter
        Bundle args = new Bundle();
        args.putParcelable("forecast_night", forecast_night);
        args.putParcelable("forecast_day", forecast_day);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //one Forecast is for night and other for day
        Forecast result_night = getArguments().getParcelable("forecast_night");
        Forecast result_day = getArguments().getParcelable("forecast_day");

        View view;
        view = inflater.inflate(R.layout.fragment_forecast, container, false);

        TextView temp_night = (TextView) view.findViewById(R.id.temp_night);
        TextView temptext_night = (TextView) view.findViewById(R.id.temptext_night);
        TextView wind_night = (TextView) view.findViewById(R.id.wind_night);
        TextView text_night = (TextView) view.findViewById(R.id.text_night);
        TextView temp_day = (TextView) view.findViewById(R.id.temp_day);
        TextView temptext_day = (TextView) view.findViewById(R.id.temptext_day);
        TextView wind_day = (TextView) view.findViewById(R.id.wind_day);
        TextView text_day = (TextView) view.findViewById(R.id.text_day);

        assert result_night != null;
        temp_night.setText(String.format(view.getResources().getString(R.string.temp),
                result_night.tempmin,
                result_night.tempmax));
        temptext_night.setText(turnTempIntoText(result_night.tempmin, result_night.tempmax));
        if(result_night.windmin != null && result_night.windmax != null){
            wind_night.setText(String.format(view.getResources().getString(R.string.wind),
                    result_night.windmin,
                    result_night.windmax));
        }
        text_night.setText(result_night.text);


        assert result_day != null;
        temp_day.setText(String.format(view.getResources().getString(R.string.temp),
                result_day.tempmin,
                result_day.tempmax));
        temptext_day.setText(turnTempIntoText(result_day.tempmin, result_day.tempmax));
        if(result_day.windmin != null && result_day.windmax != null) {
            wind_day.setText(String.format(view.getResources().getString(R.string.wind),
                    result_day.windmin,
                    result_day.windmax));
        }
        text_day.setText(result_day.text);

        return view;



    }

    // Mixes together tempmin and tempmax values in words
    private String turnTempIntoText(String tempmin, String tempmax) {
        String min = turnNumberIntoWord(tempmin);
        String max = turnNumberIntoWord(tempmax);
        return String.format(getContext().getString(R.string.from_to),min, max);
    }

    // turns number into word form
    private String turnNumberIntoWord(String temp) {
        String word = "";
        String newTemp;
        // takes sign into consideration
        if(temp.charAt(0) == '-'){
            word += getContext().getString(R.string.negative);
            newTemp = temp.substring(1);
        }else{
            word += getContext().getString(R.string.positive);
            newTemp = temp;
        }

        int wordLen = newTemp.length();
        // if there's a one-digit number
        if(wordLen == 1){
            word += getIntegerWord(newTemp);
        // if there's a two-digit number
        }else if(wordLen == 2){
            if(newTemp.charAt(0) == '1'){
                word += getIntegerWord(newTemp.substring(1, 2)) + getContext().getString(R.string.teist);
            } else {
                if(newTemp.charAt(1) == '0') {
                    word += getIntegerWord(newTemp.substring(0, 1)) + getContext().getString(R.string.tens);
                }else {
                    word += getIntegerWord(newTemp.substring(0, 1)) + getContext().getString(R.string.tens) 
                            + " " + getIntegerWord(newTemp.substring(1, 2));
                }
            }
        }
        return word;
    }

    // one-digit numbers
    private String getIntegerWord(String num) {
        String integer = "";
        switch (num) {
            case "0":
                integer = getContext().getString(R.string.zero);
                break;
            case "1":
                integer = getContext().getString(R.string.one);
                break;
            case "2":
                integer = getContext().getString(R.string.two);
                break;
            case "3":
                integer = getContext().getString(R.string.three);
                break;
            case "4":
                integer = getContext().getString(R.string.four);
                break;
            case "5":
                integer = getContext().getString(R.string.five);
                break;
            case "6":
                integer = getContext().getString(R.string.six);
                break;
            case "7":
                integer = getContext().getString(R.string.seven);
                break;
            case "8":
                integer = getContext().getString(R.string.eight);
                break;
            case "9":
                integer = getContext().getString(R.string.nine);
                break;

        }
        return integer;
    }
}
