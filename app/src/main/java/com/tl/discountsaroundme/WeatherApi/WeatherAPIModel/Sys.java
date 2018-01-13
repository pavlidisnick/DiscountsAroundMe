package com.tl.discountsaroundme.WeatherApi.WeatherAPIModel;

/**
 * Created by rezu on 7/1/2018.
 */

public class Sys {
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Sys(String pod) {

        this.pod = pod;
    }
}
