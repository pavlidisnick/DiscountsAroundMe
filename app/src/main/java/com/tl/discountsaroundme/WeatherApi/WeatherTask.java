package com.tl.discountsaroundme.WeatherApi;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.OpenWeatherMap;

import java.lang.reflect.Type;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WeatherTask extends AsyncTask<String, Void, String> {

    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        toast("Please wait..");
    }


    @Override
    protected String doInBackground(String... params) {
        String stream;
        String urlString = params[0];

        WeatherApiHelper http = new WeatherApiHelper();
        stream = http.getHTTPData(urlString);
        return stream;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.contains("Error: not found city")) {
            return;
        }
        //Save data string under user preferences for offline use
        UserPreferences.saveDataString("Forecast", s);
        Gson gson = new Gson();
        Type mType = new TypeToken<OpenWeatherMap>() {
        }.getType();
        OpenWeatherMap openWeatherMap = gson.fromJson(s, mType);
        toast("We successfully got the forecast!");
        //Calculate Item per day suggestion
        WeatherItemCalc weatherItemCalc = new WeatherItemCalc();
        weatherItemCalc.CalculateForecastSuggestions(openWeatherMap.getList());
    }
}