/*need this class later */

package com.tl.discountsaroundme.services.Notifications;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.services.GPSTracker;

public class NotificationReceiverActivity extends Activity {

    private StoreManager storeManager = new StoreManager();
    private GPSTracker gps;
    private double distance = 1; // in km




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.result);*/
        TextView tvNearbyStores = findViewById(R.id.textViewNearbyStores);
        tvNearbyStores.setText((CharSequence) storeManager.getNearbyStores(gps.getLatitude(),gps.getLongitude(),distance*1000));






    }


}
