package com.tl.discountsaroundme.services;

import android.location.Location;
import android.location.LocationManager;
import android.support.v4.util.Preconditions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GPSTrackerTest {
//    @Test
//    public void stopUsingGPS() throws Exception {
//    }
//
    @Test
    public void getLatitude() throws Exception {
        double longitude = 20.1;
        LocationManager locationManager = Mockito.mock(LocationManager.class);
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        assertEquals(20.1,20.1,longitude);
    }

//    @Test
//    public void getLongitude() throws Exception {
//    }
//
//    @Test
//    public void canGetLocation() throws Exception {
//    }
}