package com.leadertun.android.multiitemsnavigationdrawer.fragment;

import java.util.ArrayList;

import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadertun.android.multiitemsnavigationdrawer.R;
import com.leadertun.android.multiitemsnavigationdrawer.R.id;
import com.leadertun.android.multiitemsnavigationdrawer.R.layout;
import com.leadertun.android.multiitemsnavigationdrawer.event.MultiItemDrawerEvents;
import com.leadertun.android.multiitemsnavigationdrawer.weather.JSONWeatherParser;
import com.leadertun.android.multiitemsnavigationdrawer.weather.WeatherHttpClient;
import com.leadertun.android.multiitemsnavigationdrawer.wrapper.ItemWrapper;
import com.leadertun.android.multiitemsnavigationdrawer.wrapper.WeatherWrapper;
import com.leadertun.android.multiitemsnavigationdrawer.wrapper.WeatherWrapper.Temperature;

public class MyFragment extends BaseFragment {
    public static final String ARG_NAME_STATE = "name_number";
    public static final String ARG_NAME_VALUE = "value";

    public static final String ARG_LIST_CALENDARS = "listCalendars";
    
    private TextView mCity;
    private TextView mTemperature;
    private ItemWrapper mItemWrapper;
    private ArrayList<ItemWrapper> mListItemWrapper;
    
    public MyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.my_fragment, container, false);
       
        String str = getArguments().getString(ARG_NAME_VALUE);
        String stat = getArguments().getString(ARG_NAME_STATE);
        mListItemWrapper = (ArrayList<ItemWrapper>) getArguments().getSerializable(ARG_LIST_CALENDARS);
        
        TextView text = ((TextView)rootView.findViewById(R.id.fragment_layout_text));
        
        
        String mText = "Stat of calendars : \n ";
        
        for (ItemWrapper itemWrapper : mListItemWrapper) {
            mText = mText + itemWrapper.getName() +" is " + itemWrapper.isSelected()+"\n ";
        }
        
        text.setText(mText);

        
        //text.setText(mListItemWrapper.get(0).getName() +" is " + mListItemWrapper.get(0).isSelected());

        mCity = (TextView) rootView.findViewById(R.id.fragment_layout_city);
        mTemperature = (TextView) rootView
                .findViewById(R.id.fragment_layout_temp);

        String city = "Sfax,TN";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[] { city });

        getActivity().setTitle(str);
        return rootView;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, WeatherWrapper> {

        @Override
        protected WeatherWrapper doInBackground(String... params) {
            WeatherWrapper weather = new WeatherWrapper();
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
        protected void onPostExecute(WeatherWrapper weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0,
                        weather.iconData.length);
                // imgView.setImageBitmap(img);
            }

            mCity.setText(weather.location.getCity() + ","
                    + weather.location.getCountry());
            mTemperature.setText(""
                    + Math.round(convertTempCelsius(weather.temperature.getTemp()))
                    + "°C");
                    
                    

        }

        private double convertTempCelsius(double currentTemp) {
            // TODO Auto-generated method stub
            
            double convertedTemp = currentTemp - 273.15;
            return convertedTemp;
        }
        
       

    }

}
