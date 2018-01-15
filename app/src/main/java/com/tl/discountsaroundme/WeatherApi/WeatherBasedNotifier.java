package com.tl.discountsaroundme.WeatherApi;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.entities.Item;

import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 8/1/2018.
 */

public class WeatherBasedNotifier {
    static Item itemFound;
    public static Calendar repeatingNotificationTime;
    public static Calendar currentTime = Calendar.getInstance();


    public static void RepeatingNotification() {
        repeatingNotificationTime = Calendar.getInstance();
        repeatingNotificationTime.set(Calendar.HOUR_OF_DAY, 16);
        repeatingNotificationTime.set(Calendar.MINUTE, 00);
        repeatingNotificationTime.set(Calendar.SECOND, 1);
        Intent intent = new Intent(getApplicationContext(), WeatherNotificationPublisher.class);
        intent.putExtra(WeatherNotificationPublisher.NOTIFICATION_ID, 4);
        intent.putExtra(WeatherNotificationPublisher.NOTIFICATION, GetNotification(calculateNotification(), "Daily Repeating"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, repeatingNotificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        scheduleNotification(GetNotification(calculateNotification(),"Title"),10800000);
    }

    public static void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(getApplicationContext(), WeatherNotificationPublisher.class);
        notificationIntent.putExtra(WeatherNotificationPublisher.NOTIFICATION_ID, 3);
        notificationIntent.putExtra(WeatherNotificationPublisher.NOTIFICATION, GetNotification(calculateNotification(), "3Hour Repeat Notification"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static Notification GetNotification(int forecastTime, String Title) {
        StringBuilder sb = new StringBuilder("For these weather conditions we suggest\n");
        sb.append(String.format("%s \nNow %s\u20ac %.0f %%off! \nat %s", WeatherItemCalc.suggestionPerDayList.get(forecastTime).getItemCalculated().getName(), itemFound.getFinalPrice(), itemFound.getDiscount(), itemFound.getStore()));
        Notification.Builder mBuilder =
                new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_sun_day_weather_symbol)
                        .setContentTitle(Title + " " + WeatherItemCalc.suggestionPerDayList.get(forecastTime).getWeatherCondition())
                        .setAutoCancel(true)
                        .setContentText("Check this out! " + WeatherItemCalc.suggestionPerDayList.get(forecastTime).getItemSuggestion())
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(sb.toString()))
                        .setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));


        Intent buttonIntent = new Intent(getApplicationContext(), WeatherIntentReciever.class);
        buttonIntent.setAction("action");
        buttonIntent.putExtra("item",WeatherItemCalc.suggestionPerDayList.get(forecastTime).getItemCalculated());
        PendingIntent piButton = PendingIntent.getService(getApplicationContext(), 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.addAction(R.drawable.ic_sun_day_weather_symbol, "Add to cart", piButton);

        Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder.build();
    }

    public static int calculateNotification() {
        Date currentTimeFormated = currentTime.getTime();
        int forecast = 0;
        for (int i = 0; i <= WeatherItemCalc.suggestionPerDayList.size() - 1; i++) {
            Date listDateFormated = WeatherItemCalc.suggestionPerDayList.get(i).getDateTime();
            int dateComparison = currentTimeFormated.compareTo(listDateFormated);
            if (dateComparison < 0) {
                forecast = i;
                break;
            }
        }
        return forecast;

    }
}
