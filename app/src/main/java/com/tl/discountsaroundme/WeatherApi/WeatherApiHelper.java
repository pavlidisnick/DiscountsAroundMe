package com.tl.discountsaroundme.WeatherApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rezu on 7/1/2018.
 */

public class WeatherApiHelper {
    static String stream = null;

    public WeatherApiHelper(){
    }

    /**
     * Make a request  to openweatheMap API and get result
     */

    public String getHTTPData (String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode() == 200){
                BufferedReader r = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line= r.readLine())!= null){
                    sb.append(line);
                    stream = sb.toString();
                    httpURLConnection.disconnect();
                }
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
