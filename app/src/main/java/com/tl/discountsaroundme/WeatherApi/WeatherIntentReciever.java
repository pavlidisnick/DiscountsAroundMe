package com.tl.discountsaroundme.WeatherApi;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.ShoppingCart;
import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.activities.ShoppingCartActivity;
import com.tl.discountsaroundme.entities.Item;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 14/1/2018.
 */

public class WeatherIntentReciever extends IntentService {

    private static final String name = "WeatherIntentREciever";
    android.os.Handler mMainThreadHandler = null;

    public WeatherIntentReciever() {
        super(name);
        mMainThreadHandler = new android.os.Handler();
    }

    public Item item;

    @Override
    public void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();
        if (action.equals("action")) {
            item = (Item) intent.getSerializableExtra("item");
            Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent1);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (MainActivity.USER_ID != null) {
                ShoppingCart shoppingCart = new ShoppingCart(FirebaseDatabase.getInstance(), MainActivity.USER_ID);
                shoppingCart.addToCart(item);
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        toast("Successfully added to cart " + item.getName());
                    }
                });
            }else{
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        toast("Something went wrong.\nBe sure you are logged in and please try again.");
                    }
                });
            }
        } else {
            throw new IllegalArgumentException("no support action" + action);
        }

    }

    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }
}
