package com.tl.discountsaroundme.WeatherApi.WeatherAPIModel;

import com.tl.discountsaroundme.entities.Item;

import java.util.Date;

/**
 * Created by rezu on 8/1/2018.
 */

public class SuggestionPerDay {
    public String ItemSuggestion;
    public   Item ItemCalculated;
    Date DateTime;
    String WeatherCondition;
    int    dt;

    public SuggestionPerDay(String itemSuggestion, Date dateTime, String weatherCondition, int dt,Item itemCalculated) {
        ItemSuggestion = itemSuggestion;
        DateTime = dateTime;
        WeatherCondition = weatherCondition;
        this.dt = dt;
        ItemCalculated= itemCalculated;
    }


    public  Item getItemCalculated() {
        return ItemCalculated;
    }

    public  void setItemCalculated(Item itemCalculated) {
        ItemCalculated = itemCalculated;
    }

    public String getItemSuggestion() {
        return ItemSuggestion;
    }

    public void setItemSuggestion(String itemSuggestion) {
        ItemSuggestion = itemSuggestion;
    }



    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
    }

    public String getWeatherCondition() {
        return WeatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        WeatherCondition = weatherCondition;
    }
}
