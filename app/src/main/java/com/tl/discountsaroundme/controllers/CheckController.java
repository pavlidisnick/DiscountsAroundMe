package com.tl.discountsaroundme.controllers;

import android.location.LocationManager;

public class CheckController implements CheckInterface {
    public boolean areGPSandNetworkEnabled(LocationManager lm) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        return (gpsEnabled && networkEnabled);
    }
}
