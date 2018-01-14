package com.tl.discountsaroundme.WeatherApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.OpenWeatherMap;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    TextView tvWheatherCond,tvTemperature,tvCityName,tvForecastTime,tvCurrentTime;
    public OpenWeatherMap openWeatherMapActivity = new OpenWeatherMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvCityName = findViewById(R.id.tvCityName);
        tvWheatherCond = findViewById(R.id.tvWeatherCondition);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvForecastTime = findViewById(R.id.tvForecastTime);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        Date CurrentTime = Calendar.getInstance().getTime();

        if(!UserPreferences.getDataString("Forecast").equals("null")){
        Gson gson = new Gson();
        Type mType = new TypeToken<OpenWeatherMap>() {
        }.getType();
        openWeatherMapActivity = gson.fromJson(UserPreferences.getDataString("Forecast"), mType);}
        else{
            openWeatherMapActivity = WeatherTask.openWeatherMap;
        }

        if (!(openWeatherMapActivity == null)) {
        ImageView IvWeather = findViewById(R.id.IVWeather);
        Glide.with(getApplicationContext()).load(WeatherApiCommon.getImage(openWeatherMapActivity.getList().get(0).getWeather().get(0).getIcon())).into(IvWeather);
        tvCityName.setText(openWeatherMapActivity.getCity().getName());
        tvWheatherCond.setText(openWeatherMapActivity.getList().get(0).getWeather().get(0).getDescription());
        tvTemperature.setText(Double.toString(openWeatherMapActivity.getList().get(0).getMain().getTemp())+ " °C");
        tvForecastTime.setText(openWeatherMapActivity.getList().get(0).getDt_txt());
        tvCurrentTime.setText(CurrentTime.toString());}
    }
}
