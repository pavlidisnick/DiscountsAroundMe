package com.tl.discountsaroundme.ui_controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ShoppingCart;
import com.tl.discountsaroundme.activities.ItemDetailsActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.entities.Item;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        Item item = items.get(position);

        holder.tvItemName.setText(item.getName());
        holder.tvItemDetails.setText(item.getDescription());
        holder.tvStoreName.setText(item.getStore());
        holder.imgString.setText(item.getPicture());
        holder.type.setText(item.getType());

        String priceString = "$" + item.getPrice();
        holder.tvPrice.setText(priceString);

        String discount = String.valueOf(item.getDiscount());
        holder.itemDiscount.setText(discount);

        GlideApp.with(context)
                .load(item.getPicture())
                .encodeQuality(10)
                .into(holder.imageView);

        GlideApp.with(context)
                .load("https://grandmall-varna.com/pictures/original_1373.jpg")
                .encodeQuality(5)
                .circleCrop()
                .into(holder.shopImage);

        holder.idTextView.setText(item.getId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView tvItemName;
        TextView tvItemDetails;
        TextView tvStoreName;
        TextView tvPrice;
        TextView imgString;
        TextView type;
        TextView itemDiscount;
        ImageView shopImage;
        TextView idTextView;

        ItemView(final View itemView) {
            super(itemView);
            setViews();

            ImageView moreOptions = itemView.findViewById(R.id.item_options_view);
            moreOptions.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        private void setViews() {
            imageView = itemView.findViewById(R.id.img);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetail);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            imgString = itemView.findViewById(R.id.imgString);
            type = itemView.findViewById(R.id.type);
            itemDiscount = itemView.findViewById(R.id.itemDiscount);
            shopImage = itemView.findViewById(R.id.shop_image);
            idTextView = itemView.findViewById(R.id.item_id_text_view);
        }

        private Item getItemById(String id) {
            for (Item item : items) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_options_view)
                moreOptions(v);
            else
                itemClick();
        }

        private void itemClick() {
            String itemId = idTextView.getText().toString();

            Intent itemDetailsActivity = new Intent(context, ItemDetailsActivity.class);
            itemDetailsActivity.putExtra("ID", getItemById(itemId));
            context.startActivity(itemDetailsActivity);
        }

        private void moreOptions(View v) {
            final ShoppingCart shoppingCart = new ShoppingCart(FirebaseDatabase.getInstance(), MainActivity.USER_ID);

            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.item_options_menu);

            String itemId = idTextView.getText().toString();
            final Item discountItem = getItemById(itemId);
            final boolean isInCart = discountItem != null && discountItem.isInCart();

            if (isInCart) {
                popupMenu.getMenu().getItem(0).setTitle("Remove from Cart");
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add_to_cart_item:
                            if (isInCart) {
                                shoppingCart.removeFromCart(discountItem);
                            } else {
                                shoppingCart.addToCart(discountItem);
                            }
                            return true;
                        case R.id.add_to_favorites_item:
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}

