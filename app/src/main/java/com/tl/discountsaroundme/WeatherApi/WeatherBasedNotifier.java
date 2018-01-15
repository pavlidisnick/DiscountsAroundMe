package com.tl.discountsaroundme.WeatherApi;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.entities.Item;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 8/1/2018.
 */

public class WeatherBasedNotifier {
    static Item itemFound;
    public static void Notify(String condition, String suggestion) {
        int identifier = 0;
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.mini_icon)
                .setColor(getApplicationContext().getResources().getColor(R.color.colorAccent))
                .setContentTitle("Current Weather Notification! " + condition)
                .setContentText("Check this out!" + suggestion)
                .build();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        if (notificationManager != null) {
            notificationManager.notify(identifier, notification);
        }
        identifier++;
    }

    public static void RepeatingNotification (){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,1);
        Intent intent = new Intent(getApplicationContext(),WeatherNotificationPublisher.class);
        intent.putExtra(WeatherNotificationPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(WeatherNotificationPublisher.NOTIFICATION,getNotification(6,"Daily Weather Notification"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager =(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
    public static void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(getApplicationContext(), WeatherNotificationPublisher.class);
        notificationIntent.putExtra(WeatherNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(WeatherNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static Notification getNotification(int ThreeHourInterval,String Title) {
        StringBuilder sb = new StringBuilder("For these weather conditions we suggest\n");
        sb.append(String.format("%s \nNow %s\u20ac %.0f %%off! \nat %s",itemFound.getName(),itemFound.getFinalPrice(),itemFound.getDiscount(),itemFound.getStore()));
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle(Title + " " + WeatherItemCalc.suggestionPerDayList.get(ThreeHourInterval).getWeatherCondition());
        builder.setContentText("Check this out! "+ WeatherItemCalc.suggestionPerDayList.get(ThreeHourInterval).getItemSuggestion());
        builder.setSmallIcon(R.drawable.ic_sun_day_weather_symbol);
        builder.setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        builder.setStyle(new Notification.BigTextStyle()
                .bigText(sb.toString()));
        Intent actionIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getService(getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.mipmap.mini_icon,"Save to Cart!",actionPendingIntent);
        return builder.build();
    }
    public static void NotificationTestin(){
        Notification.Builder mBuilder =
                new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.mini_icon)
                .setContentTitle("this is a title")
                .setAutoCancel(true)
                .setContentText("contnent TExt");

        Intent buttonIntent = new Intent(getApplicationContext(), WeatherIntentReciever.class);
        buttonIntent.setAction("action");
        buttonIntent.putExtra("item",itemFound);
        PendingIntent piButton = PendingIntent.getService(getApplicationContext(),0,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.addAction(R.drawable.ic_sun_day_weather_symbol,"CART",piButton);

        Intent resultIntent = new Intent(getApplicationContext(),LoginActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationID = 002;
        NotificationManager mNotifyMgr =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationID,mBuilder.build());
    }

}
