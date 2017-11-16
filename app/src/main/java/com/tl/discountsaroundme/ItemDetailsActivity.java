package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

public class ItemDetailsActivity extends Activity {

    String dataprice,dataitemName,dataitemDetails,datastoreName,dataImg;

    TextView Price;
    TextView ItemName;
    TextView ItemDetails;
    TextView StoreName;
    ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();


        dataprice = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_PRICE);
        dataitemName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_NAME);
        datastoreName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_STORE);
        dataitemDetails = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_DETAILS);
        dataImg = intent.getStringExtra(ItemViewAdapter.DATA_IMAGE);




        Price =findViewById(R.id.price);
        ItemDetails = findViewById(R.id.description);
        ItemName = findViewById(R.id.item);
        StoreName = findViewById(R.id.store);
        imageView = findViewById(R.id.img);

        Price.setText(dataprice);
        ItemDetails.append(dataitemDetails);
        ItemName.setText(dataitemName);
        StoreName.append(datastoreName);

//        byte[] decodeStringImg = Base64.decode(dataImg,Base64.DEFAULT);
//        Bitmap decoded = BitmapFactory.decodeByteArray(decodeStringImg,0, decodeStringImg.length);
//        imageView.setImageBitmap(decoded);



    }
}
