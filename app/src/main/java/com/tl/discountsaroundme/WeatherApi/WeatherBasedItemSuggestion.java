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
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 8/1/2018.
 */

public class WeatherBasedItemSuggestion {
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


    public WeatherBasedItemSuggestion() {

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

    public String ItemCalculator(int conditionCode) {
        switch (conditionCodeToGroup(conditionCode)) {
            case "Thunderstorm":
                ShowItemByWeather(Rain);
                break;
            case "Drizzle":
                 ShowItemByWeather(Rain);
                break;
            case "Rain":
                 ShowItemByWeather(Rain);
                break;
            case "Snow":
                 ShowItemByWeather(Snow);
                break;
            case "Atmosphere":
               ShowItemByWeather(Clear);
                break;
            case "Clear":
                ShowItemByWeather(Clear);
                break;
            case "Clouds":
                ShowItemByWeather(Clear);
                break;
            case "Extreme":
                 ShowItemByWeather(Clear);
                break;
            case "Additional":
                ShowItemByWeather(Clear);
                break;
        }
        return ItemSuggestion;
    }


    public void CalculateForecastSuggestions(List<DayList> weatherList) {
        suggestionPerDayList = new ArrayList<>();
        for (int i = 0; i <= weatherList.size() - 1; i++) {
            suggestionPerDayList.add(i, new SuggestionPerDay(
                    ItemCalculator(weatherList.get(i).getWeather().get(0).getId()),
                    ItemCalculator(weatherList.get(i).getWeather().get(0).getId()),
                    weatherList.get(i).getDt_txt(),
                    conditionCodeToGroup(weatherList.get(i).getWeather().get(0).getId()),
                    weatherList.get(i).getDt())
            );
        }
    }

    public void ShowItemByWeather(final String[] weather) {
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
                                break;
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

            }
        });
    }


    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }
}


