package com.tl.discountsaroundme.ui_controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;

import java.util.ArrayList;


public class ManageMyDiscountsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Item> sDiscounts;

    public ManageMyDiscountsAdapter(Context mContext, ArrayList<Item> sDiscounts) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, final View view, ViewGroup viewGroup) {
        final View customView = View.inflate(mContext, R.layout.adapter_custom_row, null);

        Item item = sDiscounts.get(i);

        ImageView picture = customView.findViewById(R.id.manage_picture);
        TextView name = customView.findViewById(R.id.manage_name);
        TextView description = customView.findViewById(R.id.manage_description);
        TextView price = customView.findViewById(R.id.manage_price);
        TextView discount = customView.findViewById(R.id.manage_discount);

        name.setText(item.getName());
        description.setText(item.getDescription());
        price.setText("$" + item.getPrice());
        discount.setText("- " + item.getDiscount() + "% off");
        loadImageFromUrl(picture, item.getPicture());

        return customView;
    }

    private void loadImageFromUrl(ImageView imageView, String url) {
        GlideApp.with(mContext).load(url).into(imageView);
    }
}