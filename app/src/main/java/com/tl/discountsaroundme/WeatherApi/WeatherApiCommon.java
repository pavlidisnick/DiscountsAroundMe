package com.tl.discountsaroundme.WeatherApi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.OpenWeatherMap;
import com.tl.discountsaroundme.ui_controllers.GlideApp;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 7/1/2018.
 */

public  class WeatherApiCommon{
    public static String API_KEY = "8b89382c1ebd94c568f5cfbb3be4174e";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/forecast";
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    ProgressDialog pd = new ProgressDialog(getApplicationContext());

    /**
     * Create a functional link to the weather API path
     */

    @NonNull
    public static String apiRequest(String lat, String lng) {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric", lat, lng, API_KEY));
        return sb.toString();
    }

    /**
     * Convert unix time stamp to Date type with formal HH:mm
     */
    public static String unixTimeStampToDateTime(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 1000L);
        return dateFormat.format(date);
    }

    /**
     * Get a link image from OpenWeatherMap
     */
    public static String getImage(String icon) {
        return String.format("http://openweathermap.org/img/w/%s.png", icon);
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }

}


