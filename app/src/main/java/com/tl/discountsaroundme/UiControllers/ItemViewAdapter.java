package com.tl.discountsaroundme.UiControllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tl.discountsaroundme.Entities.Item;
import com.tl.discountsaroundme.ItemDetailsActivity;
import com.tl.discountsaroundme.R;

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
        /*holder.imageView.setImageResource(imgList[position]);
        holder.tvItemName.setText(nameList[position]);
        holder.tvItemDetails.setText(detailsList[position]);
        holder.tvStoreName.setText(storeList[position]);
        holder.tvPrice.setText(Integer.toString(priceList[position]));*/

        holder.tvItemName.setText(items.get(position).getName());
        holder.tvItemDetails.setText(items.get(position).getDescription());
        holder.tvStoreName.setText(items.get(position).getStore());
        // holder.tvStoreName.setText(storeList[position]);
        holder.tvPrice.setText(Double.toString(items.get(position).getPrice()) + " $");
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

        public ItemView(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,tvItemName.getText(), Toast.LENGTH_LONG).show();
                    ItemDetails((ImageView) itemView,tvItemName,tvStoreName,tvPrice,tvItemDetails);

                }
            });
            imageView = itemView.findViewById(R.id.img);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetail);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
        }
    }
    public void ItemDetails(ImageView imageView, TextView tvItemName, TextView tvStoreName, TextView tvPrice, TextView tvItemDetails){


        Intent ItemDetailsActivity=new Intent(context, ItemDetailsActivity.class);
        ItemDetailsActivity.putExtra("ItemName",tvItemName.toString());
        ItemDetailsActivity.putExtra("ItemDetails",tvItemDetails.toString());
        ItemDetailsActivity.putExtra("StoreName",tvStoreName.toString());
        ItemDetailsActivity.putExtra("Price",tvPrice.toString());
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ItemDetailsActivity.putExtra("img",bitmap);
        context.startActivity(ItemDetailsActivity);
    }
}

