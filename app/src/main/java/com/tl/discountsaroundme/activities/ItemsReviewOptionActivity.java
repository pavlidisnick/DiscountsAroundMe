package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private static final int EXPORT_DATE = 9999;

    DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference("items");

    ImageView imageView;

    TextView editName,editDescription,editPrice,editDiscount,editCategory;
    Button submit,editExportDate,camera,gallery;

    Uri imageUri;
    UploadTask uploadTask;

    String name, deScription, category, link, shopName,priceBeforeChange,discountBeforeChange;

    double price, discount;
    Date expirationDate;
    Item item = new Item();
    int MaxUploadTime = 40000; //set Max time for uploading to 40 seconds


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

        gallery.setOnClickListener(new View.OnClickListener() {
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
        databaseItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child(item.getId()).child("name").getValue();
                deScription = (String) dataSnapshot.child(item.getId()).child("description").getValue();
                link = (String) dataSnapshot.child(item.getId()).child("picture").getValue();
                category = (String) dataSnapshot.child(item.getId()).child("type").getValue();
                discountBeforeChange = String.valueOf( dataSnapshot.child(item.getId()).child("discount").getValue());
                priceBeforeChange = String.valueOf(dataSnapshot.child(item.getId()).child("price").getValue());
                Log.i(TAG," LOOK : " +name+deScription+link+category+discountBeforeChange+priceBeforeChange);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


    @Override
    public void onClick(View view) {
        if (view.equals(submit) && dataChange() ){
            uploadData();
        }
        else if (view.equals(findViewById(R.id.back_button)))
            this.finish();
    }

    public void toast(String messageToDisplay) {
        Toast.makeText(getApplicationContext(), messageToDisplay, Toast.LENGTH_SHORT).show();
    }

    private void uploadData() {
        if(!checkYourConnection()){
            toast("Check your internet connection");
    }
    else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(MaxUploadTime);
            final StorageReference storageRef = storage.getReference();

            final ProgressDialog pd = ProgressDialog.show(this, "", "Uploading...");

            StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
            uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    link = downloadUrl != null ? downloadUrl.toString() : null;

                    try {
                        price = Double.parseDouble(priceBeforeChange);
                        discount = Double.parseDouble(discountBeforeChange);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    databaseItem.child(item.getId()).child("name").setValue(editName.getText().toString());
                    databaseItem.child(item.getId()).child("description").setValue(editDescription.getText().toString());
                    databaseItem.child(item.getId()).child("picture").setValue(link);
                    databaseItem.child(item.getId()).child("discount").setValue(discount);
                    databaseItem.child(item.getId()).child("price").setValue(price);
                    databaseItem.child(item.getId()).child("type").setValue(editCategory.getText().toString());
                    databaseItem.child(item.getId()).child("expirationDate").setValue(expirationDate);

                    pd.dismiss();
                    toast("Item added successfully");
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    toast("Error while uploading");
                }
            });
    }
    }

    private boolean checkYourConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean dataChange() {
        if(!link.isEmpty()){
            return true;
        }
        else if(!editName.getText().toString().isEmpty() ){
            return true;
        }else if(!editDescription.getText().toString().isEmpty() ){
                return true;
        }else if(!editCategory.getText().toString().isEmpty() ){
            return true;
        }else if(!editPrice.getText().toString().isEmpty() ){
            return true;
        }else if(!editDiscount.getText().toString().isEmpty() ){
            return true;
        }

        return false;
    }
}
