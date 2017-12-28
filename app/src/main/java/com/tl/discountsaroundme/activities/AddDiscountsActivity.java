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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.tl.discountsaroundme.entities.ItemValidator;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class AddDiscountsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECTED_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1888;

    private static final int EXPORT_DATE = 9999;

    ImageView imageView;
    Button selectImg, addItem, camera;


    Uri imageUri;
    UploadTask uploadTask;

    String name, description, category, link, shopName;
    TextView exportDateText;

    Button exportDayButton;
    Date expirationDate;

    double price, discount;

    int MaxUploadTime = 40000; //set Max time for uploading to 40 seconds

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        exportDayButton = findViewById(R.id.exportButton);
        exportDateText = findViewById(R.id.exportText);

        new StatusBar(this);

        getShopName();

        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        selectImg = findViewById(R.id.buttonSelectImage);
        addItem = findViewById(R.id.buttonAddItem);
        camera = findViewById(R.id.buttonCamera);

        exportDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpirationDateDialogActivity.class);
                startActivityForResult(intent, EXPORT_DATE);
            }
        });

        selectImg.setOnClickListener(new View.OnClickListener() {
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

        addItem.setOnClickListener(this);
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
            exportDateText.setText("Export Date: " + String.valueOf(expirationDate));
            Log.d("DATE Transfer DONE", "DATE of export :  " + String.valueOf(expirationDate));
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

    public boolean areDataReady() {
        ItemValidator itemValidator = new ItemValidator();

        EditText nam = findViewById(R.id.editTextName);
        EditText des = findViewById(R.id.editTextDescription);
        EditText cat = findViewById(R.id.editTextCategory);
        EditText pr = findViewById(R.id.editTextPrice);
        EditText disc = findViewById(R.id.editTextDiscount);

        name = nam.getText().toString();
        description = des.getText().toString();
        category = cat.getText().toString();

        String priceString = pr.getText().toString();
        String discountString = disc.getText().toString();

        try {
            price = Double.parseDouble(priceString);
            discount = Double.parseDouble(discountString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (itemValidator.areStringsEmpty(name, description, category, priceString, discountString)) {
            toast("Please fill the fields");
        } else if (!itemValidator.isDiscountInRange(discount)) {
            toast("Discount must be number 1 to 100");
        } else if (imageUri == null) {
            toast("Please add image");
        } else if (!isConnectedToInternet()) {
            toast("Check your internet connection");
        } else {
            return true;
        }

        return false;
    }

    public void toast(String messageToDisplay) {
        Toast.makeText(getApplicationContext(), messageToDisplay, Toast.LENGTH_SHORT).show();
    }

    public void insertToDatabase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(MaxUploadTime);
        final StorageReference storageRef = storage.getReference();

        final ProgressDialog pd = ProgressDialog.show(this, "", "Uploading...");

        final DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference("items");

        StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
        uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                link = downloadUrl != null ? downloadUrl.toString() : null;

                String id = databaseItem.push().getKey();
                Item item = new Item(id, name, category, price, discount, description, link, shopName, expirationDate);

                databaseItem.child(id).setValue(item);

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

    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void getShopName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/shops");
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
    public void onClick(View v) {
        if (v.equals(addItem) && areDataReady())
            insertToDatabase();
        else if (v.equals(findViewById(R.id.back_button)))
            this.finish();
    }
}
