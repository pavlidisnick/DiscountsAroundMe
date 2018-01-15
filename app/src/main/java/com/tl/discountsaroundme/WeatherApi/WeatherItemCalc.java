package com.tl.discountsaroundme.WeatherApi;

import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.DayList;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.SuggestionPerDay;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 8/1/2018.
 */

public class WeatherItemCalc {
    private Integer conditionCode;
    private String CategorySuggestion;
    private String ItemSuggestion;
    public static ArrayList<SuggestionPerDay> suggestionPerDayList;
    public String[] Rain = {"umbrella","raincoat","sweater","hoodie","rain","boots","coat","boat","shelter","hat"} ;
    public String[] Snow = {"gloves","snow","ski","hoodie","thermal","boots","coat","cold","scarf","hat"};
    public String[] Clear = {"jeans","jacket","sun","glasses","phone","laptop","shoes","running","stars"};

    public DiscountsManager ItemManager = new DiscountsManager();

    public Integer getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(Integer conditionCode) {
        this.conditionCode = conditionCode;
    }

    public String getCategorySuggestion() {
        return CategorySuggestion;
    }

    public void setCategorySuggestion(String categorySuggestion) {
        CategorySuggestion = categorySuggestion;
    }

    public String getItemSuggestion() {
        return ItemSuggestion;
    }

    public void setItemSuggestion(String itemSuggestion) {
        ItemSuggestion = itemSuggestion;
    }


    public WeatherItemCalc() {

    }

    public String conditionCodeToGroup(int conditionCode) {
        String ConditionGroup = null;
        if (conditionCode <= 232) {
            ConditionGroup = "Thunderstorm";
        } else if (conditionCode >= 300 && conditionCode <= 321) {
            ConditionGroup = "Drizzle";
        } else if (conditionCode >= 500 && conditionCode <= 531) {
            ConditionGroup = "Rain";
        } else if (conditionCode >= 600 && conditionCode <= 622) {
            ConditionGroup = "Snow";
        } else if (conditionCode >= 700 && conditionCode <= 781) {
            ConditionGroup = "Atmosphere";
        } else if (conditionCode == 800) {
            ConditionGroup = "Clear";
        } else if (conditionCode >= 801 && conditionCode <= 804) {
            ConditionGroup = "Clouds";
        } else if (conditionCode >= 900 && conditionCode <= 906) {
            ConditionGroup = "Extreme";
        } else if (conditionCode >= 951 && conditionCode <= 962) {
            ConditionGroup = "Additional";
        }
        return ConditionGroup;
    }

    public String ItemCalculator(int conditionCode , int i) {
        switch (conditionCodeToGroup(conditionCode)) {
            case "Thunderstorm":
                ShowItemByWeather(Rain,i);
                ItemSuggestion ="rain";
                break;
            case "Drizzle":
                 ShowItemByWeather(Rain,i);
                ItemSuggestion ="rain";
                break;
            case "Rain":
                 ShowItemByWeather(Rain,i);
                ItemSuggestion ="Rain";
                break;
            case "Snow":
                 ShowItemByWeather(Snow,i);
                ItemSuggestion ="snow";
                break;
            case "Atmosphere":
               ShowItemByWeather(Clear,i);
                ItemSuggestion ="clear";
                break;
            case "Clear":
                ShowItemByWeather(Clear,i);
                ItemSuggestion ="clear";
                break;
            case "Clouds":
                ShowItemByWeather(Clear,i);
                ItemSuggestion ="clear";
                break;
            case "Extreme":
                 ShowItemByWeather(Clear,i);
                ItemSuggestion ="clear";
                break;
            case "Additional":
                ShowItemByWeather(Clear,i);
                ItemSuggestion ="clear";
                break;
        }
        return ItemSuggestion;
    }


    public void CalculateForecastSuggestions(List<DayList> weatherList) {
        suggestionPerDayList = new ArrayList<>();
        Item item = new Item(null,null,null,34,34,null,null,null,null);
        for (int i = 0; i <= weatherList.size() - 1; i++) {
            suggestionPerDayList.add(i, new SuggestionPerDay(
                    ItemCalculator(weatherList.get(i).getWeather().get(0).getId(),i),
                    dateFormatChange(weatherList.get(i).getDt_txt()),
                    conditionCodeToGroup(weatherList.get(i).getWeather().get(0).getId()),
                    weatherList.get(i).getDt(),
                    item)
            );

        }
    }

    public void ShowItemByWeather(final String[] weather , final int i ) {
        String dbPath = "/items";
        DatabaseReference mdb = FirebaseDatabase.getInstance().getReference(dbPath);
        mdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Item item = child.getValue(Item.class);
                        for (int j = 0; j<=weather.length- 1; j++) {
                            if (item.getName().toLowerCase().contains(weather[j])) {
                                WeatherBasedNotifier.itemFound = item;
                                ItemSuggestion = item.getName();
                                suggestionPerDayList.get(i).ItemCalculated = item;
                            } else {

                            }

                        }

                    }
                    WeatherBasedNotifier.scheduleNotification(WeatherBasedNotifier.getNotification(1, "Three Hour Interval Notification!"), 10800000);
                    WeatherBasedNotifier.RepeatingNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),
                        "Canceled  ", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private Date dateFormatChange(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outFormat = new SimpleDateFormat("EEE dd.MM.yyyy 'at' HH:mm z");
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDate = outFormat.format(date);
        try {
            date = outFormat.parse(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }
}


