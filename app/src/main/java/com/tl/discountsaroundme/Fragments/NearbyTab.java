package com.tl.discountsaroundme.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tl.discountsaroundme.R;


import static android.content.Context.NOTIFICATION_SERVICE;

public class NearbyTab extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_nearby, container, false);
    }

    public void createNotification(View view) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent myIntent = new Intent(NearbyTab.this.getActivity(), NearbyTab.class);
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), myIntent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(getContext())
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(R.drawable.ic_woman_shoe)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_woman_shoe, "And more", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }
}