package com.tl.discountsaroundme.Controllers;

import android.location.LocationManager;

public class CheckController implements CheckInterface {
    public boolean areGPSandNetworkEnabled(LocationManager lm) {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        return (gps_enabled && network_enabled);
    }
}
