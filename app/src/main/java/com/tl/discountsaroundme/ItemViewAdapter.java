package com.tl.discountsaroundme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tl.discountsaroundme.Entities.Item;

import java.util.ArrayList;


public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ItemView> {

    private Context context;
    private ArrayList<Item> items;


    public ItemViewAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemView(layoutView);
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.tvItemName.setText(items.get(position).getName());
        holder.tvItemDetails.setText(items.get(position).getDescription());
        // holder.tvStoreName.setText(storeList[position]);
        holder.tvPrice.setText(Double.toString(items.get(position).getPrice()));
        RequestOptions options = new RequestOptions();
        Glide.with(context)
                .load(items.get(position).getPicture())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ItemView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvItemName;
        TextView tvItemDetails;
        TextView tvStoreName;
        TextView tvPrice;

        public ItemView(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetail);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
        }
    }
}

