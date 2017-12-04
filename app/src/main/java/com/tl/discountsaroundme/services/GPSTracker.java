package com.tl.discountsaroundme.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.fragments.DiscountsTab;
import com.tl.discountsaroundme.fragments.MapTab;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 2;
    // GPS status
    private Location location;

    private Activity activity;
    private StoreManager storeManager;
    private DiscountsManager discountsManager;
    private GoogleMap googleMap;

    private boolean isNotificationsEnabled = true;

    public GPSTracker(Activity activity, StoreManager storeManager, DiscountsManager discountsManager, GoogleMap googleMap) {
        this.storeManager = storeManager;
        this.discountsManager = discountsManager;
        this.googleMap = googleMap;
        this.activity = activity;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            AtomicReference<LocationManager> atomicReference;
            atomicReference = new AtomicReference<>((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE));

            LocationManager locationManager = atomicReference.get();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleNotifications(boolean notificationStatus) {
        isNotificationsEnabled = notificationStatus;
    }

    // Function to get latitude
    public double getLatitude() throws NullPointerException {
        return location.getLatitude();
    }

    // Function to get longitude
    public double getLongitude() throws NullPointerException {
        return location.getLongitude();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (isNotificationsEnabled) {
            try {
                createNotification();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            getLocation();
            createNotification();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void createNotification() {
        ArrayList<Store> stores = storeManager.getNearbyStores(getLatitude(), getLongitude(), MapTab.distance * 1000);
        ArrayList<Store> topStores = new ArrayList<>();

        for (Store store : stores) {
            ArrayList<Item> items = discountsManager.getTopDiscountsByStore(store.getName(), DiscountsTab.discountValue);
            if (!items.isEmpty())
                topStores.add(store);
        }

        googleMap.clear();

        int identifier = 0;
        for (Store store : topStores) {
            Item item = discountsManager.getTopItemByStore(store.getName());
            String contentText = item.getName() + " " + item.getDiscount();

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_shop);

            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(store.getLat(), store.getLng()))
                    .title(store.getName())
                    .snippet(contentText)
                    .flat(true)
                    .icon(icon);
            googleMap.addMarker(marker);

            Notification notification = new Notification.Builder(activity.getApplicationContext())
                    .setSmallIcon(R.mipmap.icon_circle)
                    .setContentTitle(store.getName())
                    .setContentText(contentText)
                    .build();
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
            // hide the notification after its selected
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            if (notificationManager != null) {
                notificationManager.notify(identifier, notification);
            }
            identifier++;
        }
    }

}
