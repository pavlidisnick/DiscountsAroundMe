package com.tl.discountsaroundme.WeatherApi.WeatherAPIModel;

import java.util.List;

/**
 * Created by rezu on 7/1/2018.
 */

public class OpenWeatherMap {
    private int cod;
    private double message;
    private int cnt;
    private List<DayList> list;
    private City city;
    public OpenWeatherMap () {

    }

    public OpenWeatherMap(int cod, double message, int cnt, List<DayList> list, City city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<DayList> getList() {
        return list;
    }

    public void setList(List<DayList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
