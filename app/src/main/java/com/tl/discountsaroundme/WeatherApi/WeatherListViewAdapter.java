package com.tl.discountsaroundme.WeatherApi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.WeatherApi.WeatherAPIModel.SuggestionPerDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class WeatherListViewAdapter extends ArrayAdapter<SuggestionPerDay> {
    public WeatherListViewAdapter(@NonNull Context context, ArrayList<SuggestionPerDay> suggestionPerDayList) {
        super(context, 0,suggestionPerDayList);
    }
    DateFormat timeDisplayFormat = new SimpleDateFormat("EEE dd.MM.yyyy 'at' HH:mm z");

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SuggestionPerDay list = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_listviewadapter, parent, false);
        }
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvCondition = convertView.findViewById(R.id.tvCondition);
        tvDate.setText(timeDisplayFormat.format(list.getDateTime()));
        tvCondition.setText(list.getWeatherCondition());
        ImageView ivicon = convertView.findViewById(R.id.IvIcon);
        Glide.with(getApplicationContext()).load(WeatherApiCommon.getImage(WeatherActivity.openWeatherMapActivity.getList().get(position).getWeather().get(0).getIcon()))
                .into(ivicon);
        TextView tvItem = convertView.findViewById(R.id.tvItem);
        tvItem.setText(WeatherItemCalc.suggestionPerDayList.get(position).getItemCalculated().getName());
        return convertView;
    }
}
