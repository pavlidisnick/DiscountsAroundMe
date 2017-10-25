package com.tl.discountsaroundme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tl.discountsaroundme.Entities.Item;

import java.util.ArrayList;

/**
 * Created by rezu on 25/10/2017.
 */

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> item;
    Context context;
    LayoutInflater mInflater;

    public ItemArrayAdapter(ArrayList<Item> items,Context c, int textViewid){
        super(c, textViewid, items);

        item = items;
        context = c;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
                TextView tvname = (TextView)convertView.findViewById(android.R.id.text1);
                //TODO create custom layout xml file for the custom adapter
                tvname.setText(item.get(position).getName() + " "+item.get(position).getPrice());
            }

        return convertView;
    }
}
