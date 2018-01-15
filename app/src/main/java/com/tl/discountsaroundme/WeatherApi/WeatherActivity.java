package com.tl.discountsaroundme.WeatherApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.OpenWeatherMap;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    TextView tvWeatherCond, tvTemperature, tvCityName, tvForecastTime, tvCurrentTime;
    Button btNotifybutton;
    CheckBox cbPerDay;
    ListView listView;
    public OpenWeatherMap openWeatherMapActivity = new OpenWeatherMap();
    public boolean PerDayList = false;
    ArrayList<String> forecastList = new ArrayList<>();
    DateFormat CurrentTimeFormated = new SimpleDateFormat("EEE dd.MM.yyyy 'at' HH:mm z");
    Date CurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        btNotifybutton = findViewById(R.id.btNotify);
        btNotifybutton.setOnClickListener(this);

        tvCityName = findViewById(R.id.tvCityName);
        tvWeatherCond = findViewById(R.id.tvWeatherCondition);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvForecastTime = findViewById(R.id.tvForecastTime);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        cbPerDay = findViewById(R.id.cbPerday);
        cbPerDay.setOnCheckedChangeListener(this);
        CurrentTime = Calendar.getInstance().getTime();
        try {
            CurrentTime = CurrentTimeFormated.parse(CurrentTimeFormated.format(CurrentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!UserPreferences.getDataString("Forecast").equals("Null")) {
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>() {
            }.getType();
            openWeatherMapActivity = gson.fromJson(UserPreferences.getDataString("Forecast"), mType);
            WeatherItemCalc weatherItemCalc = new WeatherItemCalc();
            weatherItemCalc.CalculateForecastSuggestions(openWeatherMapActivity.getList());
        }

            setTodayForecast();


        listView = findViewById(R.id.lvListview);
        SuggestionList();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btNotify) {
            WeatherBasedNotifier.NotificationTestin();
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PerDayList = isChecked;
        forecastList.clear();
        SuggestionList();
    }

    /**
     * Populates the Listview With Daily/3hour weather forecasts
     */
    public void SuggestionList() {
        for (int i = 0; i <= WeatherItemCalc.suggestionPerDayList.size() - 1; i++) {
            //If Perday cb is checked then sort the list By day
            if (PerDayList) {
                // Compare with the next Date
                int nextdate = i + 1;
                //If the list is to the end, add the last date to the list
                if (i == WeatherItemCalc.suggestionPerDayList.size() - 1) {
                    nextdate = i;
                    forecastList.add(WeatherItemCalc.suggestionPerDayList.get(i).getItemSuggestion() + " " + WeatherItemCalc.suggestionPerDayList.get(i).getWeatherCondition()
                            + " " + WeatherItemCalc.suggestionPerDayList.get(i).getDateTime().toString());
                }
                // 0 is equal dates 1 is a date  to come and -1 is a date  past
                int dateComparison = 0;
                //Change the format of the dates to dd-mm-yyyy thus we check only if the day changes
                try {
                    dateComparison = formatDatesForOrdering(WeatherItemCalc.suggestionPerDayList.get(nextdate).getDateTime())
                            .compareTo(formatDatesForOrdering(WeatherItemCalc.suggestionPerDayList.get(i).getDateTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Compare the dates and when there is a date > of the one we check add it to the list
                if (dateComparison == 0) {
                    //Equal dates
                } else if (dateComparison <= 0) {
                    //a Date Past
                } else {
                    forecastList.add(WeatherItemCalc.suggestionPerDayList.get(i).getItemSuggestion() + " " + WeatherItemCalc.suggestionPerDayList.get(i).getWeatherCondition()
                            + " " + WeatherItemCalc.suggestionPerDayList.get(i).getDateTime().toString());
                }
            }//If the CB is not checked just add all the dates to the list
            else {
                forecastList.add(WeatherItemCalc.suggestionPerDayList.get(i).getItemSuggestion() + " " + WeatherItemCalc.suggestionPerDayList.get(i).getWeatherCondition()
                        + " " + WeatherItemCalc.suggestionPerDayList.get(i).getDateTime().toString());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, forecastList);
        listView.setAdapter(adapter);
    }

    public Date formatDatesForOrdering(Date date) throws ParseException {
        DateFormat orderFormat = new SimpleDateFormat("dd.MM.yyy");
        Date finalFormat = orderFormat.parse(orderFormat.format(date));
        return finalFormat;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();
        Toast.makeText(getApplicationContext(),
                WeatherItemCalc.suggestionPerDayList.get(position).getItemCalculated().getName() + position + WeatherItemCalc.suggestionPerDayList.get(position).getDateTime(), Toast.LENGTH_LONG)
                .show();
        //aawjeahweah
    }

    public int getTodayForecast(Date currentTime) throws ParseException {
        Date currentTimeFormated = currentTime;
        int todaysForecast = 0;
        for (int i = 0; i <= WeatherItemCalc.suggestionPerDayList.size() - 1; i++) {
            Date listDateFormated = WeatherItemCalc.suggestionPerDayList.get(i).getDateTime();
            int dateComparison = currentTimeFormated.compareTo(listDateFormated);
            if (dateComparison < 0) {
                todaysForecast = i;
                break;
            }
        }
        return todaysForecast;
    }

    public void setTodayForecast()  {
        if (!(openWeatherMapActivity == null)) {
            ImageView IvWeather = findViewById(R.id.IVWeather);
            int todaysForecast = 0;
            try {
                todaysForecast = getTodayForecast(CurrentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(getApplicationContext()).load(WeatherApiCommon.getImage(openWeatherMapActivity.getList().get(todaysForecast).getWeather().get(0).getIcon())).into(IvWeather);
            tvCityName.setText(openWeatherMapActivity.getCity().getName());
            tvWeatherCond.setText(openWeatherMapActivity.getList().get(todaysForecast).getWeather().get(0).getDescription());
            tvTemperature.setText(Double.toString(openWeatherMapActivity.getList().get(todaysForecast).getMain().getTemp()) + " °C");
            tvForecastTime.setText("Forecast: " + WeatherItemCalc.suggestionPerDayList.get(todaysForecast).getDateTime());
            tvCurrentTime.setText(CurrentTimeFormated.format(CurrentTime));
        }

    }


}
