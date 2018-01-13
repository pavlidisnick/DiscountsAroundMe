package com.tl.discountsaroundme.WeatherApi.WeatherAPIModel;

/**
 * Created by rezu on 8/1/2018.
 */

public class SuggestionPerDay {
    String ItemSuggestion;
    String CategorySuggestion;
    String DateTime;
    String WeatherCondition;
    int    dt;

    public SuggestionPerDay(String itemSuggestion, String categorySuggestion, String dateTime, String weatherCondition, int dt) {
        ItemSuggestion = itemSuggestion;
        CategorySuggestion = categorySuggestion;
        DateTime = dateTime;
        WeatherCondition = weatherCondition;
        this.dt = dt;
    }




    public String getItemSuggestion() {
        return ItemSuggestion;
    }

    public void setItemSuggestion(String itemSuggestion) {
        ItemSuggestion = itemSuggestion;
    }

    public String getCategorySuggestion() {
        return CategorySuggestion;
    }

    public void setCategorySuggestion(String categorySuggestion) {
        CategorySuggestion = categorySuggestion;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getWeatherCondition() {
        return WeatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        WeatherCondition = weatherCondition;
    }
}
