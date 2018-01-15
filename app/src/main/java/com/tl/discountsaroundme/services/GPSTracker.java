package com.tl.discountsaroundme.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.WeatherApi.WeatherIntentReciever;
import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.fragments.MapTab;
import com.tl.discountsaroundme.map.MarkerHelper;
import com.tl.discountsaroundme.map.NearbyStoreList;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tl.discountsaroundme.fragments.MapTab.isNotificationsEnabled;
import static com.tl.discountsaroundme.fragments.MapTab.notifyEvery;


public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 2;
    // GPS status
    private Location location;
    private Location lastLocation;
    private Activity activity;
    private StoreManager storeManager;
    private DiscountsManager discountsManager;
    private MarkerHelper markerHelper;
    private NearbyStoreList nearbyStoreList;

    private int notifyOnceFlag = 1;
  
    public GPSTracker(Activity activity, StoreManager storeManager,
                      DiscountsManager discountsManager, MarkerHelper markerHelper, NearbyStoreList nearbyStoreList) {
        this.activity = activity;
        this.storeManager = storeManager;
        this.discountsManager = discountsManager;
        this.markerHelper = markerHelper;
        this.nearbyStoreList = nearbyStoreList;
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
            lastLocation = location;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getLatitude() throws NullPointerException {
        return location.getLatitude();
    }

    public double getLongitude() throws NullPointerException {
        return location.getLongitude();
    }

    private void showGpsDialog() {
        @SuppressLint("InflateParams")
        LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_open_gps, null);
        Button enableGps = (Button) linearLayout.getChildAt(2);

        final AlertDialog builder = new AlertDialog.Builder(activity).setView(linearLayout).create();
        enableGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                builder.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        nearbyStoreList.showNearbyStores(latLng, MapTab.distance);

        float[] results = new float[1];
        if (lastLocation != null)
            Location.distanceBetween(latLng.latitude, latLng.longitude, lastLocation.getLatitude(), lastLocation.getLongitude(), results);

        if (isNotificationsEnabled && (notifyOnceFlag == 1 || results[0] >= notifyEvery)) {
            notifyOnceFlag = 0;
            lastLocation = location;
            try {
                showNearbyAndNotify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            showGpsDialog();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            getLocation();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void showNearbyAndNotify() {
        ArrayList<Store> stores = storeManager.getNearbyStores(getLatitude(), getLongitude(), MapTab.distance);

        markerHelper.addMarkersFromList(stores);

        int identifier = 2;
        for (Store store : stores) {
            Item item = discountsManager.getTopItemByStore(store.getName());
            String contentText = item.getName() + " " + item.getDiscount() + "% off";
                 ShopNotify(store.getName(),contentText,item, identifier);
                 identifier++;
        }
    }
    public void ShopNotify (String Title, String Text, Item item, int identifier) {
        Notification.Builder mBuilder =
                new Notification.Builder(activity.getApplicationContext())
                        .setSmallIcon(R.mipmap.mini_icon)
                        .setColor(activity.getResources().getColor(R.color.colorAccent))
                        .setContentTitle(Title)
                        .setAutoCancel(true)
                        .setStyle(new Notification.BigTextStyle()
                        .bigText(item.getDescription()))
                        .setContentText(Text);

        mBuilder.getNotification().defaults |= Notification.DEFAULT_SOUND;

        Intent buttonIntent = new Intent(activity.getApplicationContext(), WeatherIntentReciever.class);
        buttonIntent.setAction("action");
        buttonIntent.putExtra("item",item);
        PendingIntent piButton = PendingIntent.getService(activity.getApplicationContext(),0,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.addAction(R.drawable.ic_shopping_cart,"Add to cart!",piButton);

        Intent resultIntent = new Intent(activity.getApplicationContext(),LoginActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                       activity.getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotifyMgr =
                (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(identifier, mBuilder.build());
        }

    }
}
