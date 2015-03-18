package com.leadertun.android.multiitemsnavigationdrawer;

import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadertun.android.multiitemsnavigationdrawer.model.Weather;

public class MyFragment extends BaseFragment {
    static final String ARG_NAME_NUMBER = "name_number";

    private TextView mCity;
    private TextView mTemperature;

    public MyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.my_fragment, container, false);
        int i = getArguments().getInt(ARG_NAME_NUMBER);
        String str = getArguments().getString("addaccount");

        ((TextView) rootView.findViewById(R.id.fragment_layout_text))
                .setText(str);

        mCity = (TextView) rootView.findViewById(R.id.fragment_layout_city);
        mTemperature = (TextView) rootView
                .findViewById(R.id.fragment_layout_temp);

        String city = "Sfax,TN";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[] { city });

        getActivity().setTitle(str);
        return rootView;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient())
                        .getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0,
                        weather.iconData.length);
                // imgView.setImageBitmap(img);
            }

            mCity.setText(weather.location.getCity() + ","
                    + weather.location.getCountry());
            mTemperature.setText(""
                    + Math.round((weather.temperature.getTemp() - 273.15))
                    + "°C");

        }

    }

}
