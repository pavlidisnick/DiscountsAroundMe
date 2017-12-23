package com.tl.discountsaroundme.ui_controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;

import java.util.ArrayList;


public class DiscountsAdapter extends BaseAdapter {

    private ImageView imageView;
    private Context mContext;
    private ArrayList<Item> sDiscounts;

    public DiscountsAdapter(Context mContext, ArrayList<Item> sDiscounts) {
        this.mContext = mContext;
        this.sDiscounts = sDiscounts;
    }

    @Override
    public int getCount() {
        return sDiscounts.size();
    }

    @Override
    public Object getItem(int position) {
        return sDiscounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View customView = View.inflate(mContext, R.layout.adapter_custom_row, null);

        TextView textView = customView.findViewById(R.id.textViewDiscounts);
        imageView = customView.findViewById(R.id.imageViewDiscounts);

        textView.setText(sDiscounts.get(i).getName());
        loadImageFromUrl(sDiscounts.get(i).getPicture());

        return customView;
    }

    private void loadImageFromUrl(String url) {
        GlideApp.with(mContext).load(url).into(imageView);
    }
}