package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

public class ItemDetailsActivity extends Activity {

    String dataprice,dataitemName,dataitemDetails,datastoreName,dataImg,dataType,dataDiscount;

    TextView Price;
    TextView ItemName;
    TextView ItemDetails;
    TextView StoreName;
    ImageView imageView;
    TextView Type;
    TextView Discount;




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
        dataType = intent.getStringExtra(ItemViewAdapter.DATA_TYPE);
        dataDiscount = intent.getStringExtra(ItemViewAdapter.DATA_DISCOUNT);


       // String correctPrice = DiscountFix(dataDiscount,dataprice);

        Price = findViewById(R.id.price);
        ItemDetails = findViewById(R.id.description);
        ItemName = findViewById(R.id.item);
        StoreName = findViewById(R.id.store);
        imageView = findViewById(R.id.img);
        Type = findViewById(R.id.type);
        Discount = findViewById(R.id.correctPrice);


        Price.setText(" "+dataprice);
        ItemDetails.append(dataitemDetails);
        ItemName.setText(dataitemName);
        StoreName.append(datastoreName);
        Type.append(dataType);
        Discount.setText("30%");


//        byte[] decodeStringImg = Base64.decode(dataImg,Base64.DEFAULT);
//        Bitmap decoded = BitmapFactory.decodeByteArray(decodeStringImg,0, decodeStringImg.length);
//        imageView.setImageBitmap(decoded);



    }

    private String DiscountFix(String dataDiscount,String dataprice) {
        String discount;
        dataDiscount = "0."+dataDiscount;
        float price = Float.parseFloat(dataprice);
        float disc = Float.parseFloat(dataDiscount);
        float afterDiscount = price-(price*disc);
        discount = Float.toString(afterDiscount);
        return discount;
    }
}
