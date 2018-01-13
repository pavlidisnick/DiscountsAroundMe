package com.tl.discountsaroundme.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ShoppingCart;
import com.tl.discountsaroundme.ui_controllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    // TODO if there are no items in the cart set this visible
    private LinearLayout emptyCartMessage;
    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        new StatusBar(this);

        String userId = MainActivity.USER_ID;
        shoppingCart = new ShoppingCart(FirebaseDatabase.getInstance(), userId);
        shoppingCart.retrieveCartItems();

        RecyclerView mRecyclerView = findViewById(R.id.shopping_cart_items);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        ItemViewAdapter adapter = new ItemViewAdapter(this, shoppingCart.getCartItems());
        shoppingCart.setAdapter(adapter);
        mRecyclerView.setAdapter(adapter);

        //ItemDecoration for spacing between items
        decorate(mRecyclerView);

        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        ImageView moreImage = findViewById(R.id.cart_more_options);
        moreImage.setOnClickListener(this);

        emptyCartMessage = findViewById(R.id.empty_cart_message);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.shopping_cart_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shoppingCart.retrieveCartItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void decorate(RecyclerView recyclerView) {
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(16);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.cart_more_options:
                showPopupMenu(v);
                break;
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.shopping_cart_menu);

        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        shoppingCart.removeAllCartItems();
        return true;
    }
}
