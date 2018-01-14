package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class ItemsReviewOptionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECTED_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "aa";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/shops");
    DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference("items");


    private static final int EXPORT_DATE = 9999;

    ImageView imageView;

    TextView editName,editDescription,editPrice,editDiscount,editCategory;
    Button submit,editExportDate,camera,gallery;

    Uri imageUri;
    UploadTask uploadTask;

    String name, deScription, category, link, shopName;
    double price, discount;
    Date expirationDate;
    Item item = new Item();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_review_option);

       imageView = findViewById(R.id.editImageView);
       editName = findViewById(R.id.editItemName);
       editCategory = findViewById(R.id.editItemCategory);
       editDescription = findViewById(R.id.editItemDescription);
       editPrice = findViewById(R.id.editItemPrice);
       editDiscount = findViewById(R.id.editItemDiscount);
       submit = findViewById(R.id.submitButton);
       editExportDate = findViewById(R.id.editItemExportButton);
       camera = findViewById(R.id.editItemCamera);
       gallery = findViewById(R.id.editItemGallery);


       getShopName();
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
       setItemsBeforeEdit();




        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        editExportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpirationDateDialogActivity.class);
                startActivityForResult(intent, EXPORT_DATE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        submit.setOnClickListener(this);
    }

    private void setItemsBeforeEdit() {
        String id = databaseItem.push().getKey();

        Log.i(TAG,"LOOKKKKK" +item.getId());
          editName.setText(item.getName());
          expirationDate = item.getExpirationDate();
          editPrice.setText(String.valueOf(item.getPrice()));
          editDiscount.setText(String.valueOf(item.getDiscount()));
          editDescription.setText(item.getDescription());
          editCategory.setText(item.getType());
        Glide.with(getApplicationContext()).load(item.getPicture()).into(imageView);


    }

    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }

    public void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXPORT_DATE && resultCode == RESULT_OK) {
            expirationDate = (Date) data.getSerializableExtra("date");
        }

        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            if (photo != null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            }

            File destination = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

            FileOutputStream fo;

            try {
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageUri = Uri.fromFile(new File(destination.getAbsolutePath()));
            imageView.setImageURI(imageUri);
        }
    }

    public void getShopName() {

        if (user != null) {
            final String uid = user.getUid();

            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String ID = itemSnapshot.child("ownerUID").getValue(String.class);
                        if (ID != null && ID.matches(uid)) {
                            shopName = itemSnapshot.child("name").getValue(String.class);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(submit) ){

        }
        else if (view.equals(findViewById(R.id.back_button)))
            this.finish();
    }
}
