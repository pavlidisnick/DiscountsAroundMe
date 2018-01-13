package com.tl.discountsaroundme.WeatherApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.OpenWeatherMap;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ImageView IvWeather = findViewById(R.id.IVWeather);
        Glide.with(getApplicationContext()).load("http://openweathermap.org/img/w/10n.png").into(IvWeather);
    }
}
