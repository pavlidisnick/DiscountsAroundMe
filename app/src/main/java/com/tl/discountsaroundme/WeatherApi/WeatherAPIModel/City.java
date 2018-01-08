package com.tl.discountsaroundme.WeatherApi.WeatherAPIModel;

/**
 * Created by rezu on 8/1/2018.
 */

public class City {
    private int id ;
    private String name;
    private Coord coord;
    private String country;
    private int population;

    public City(int id, String name, Coord coord, String country, int population) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
