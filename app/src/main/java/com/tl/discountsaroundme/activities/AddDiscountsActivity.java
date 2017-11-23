package com.tl.discountsaroundme.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class AddDiscountsActivity extends AppCompatActivity {

    private static final int SELECTED_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    Button selectImg, addItem, camera;

    Uri imageUri;
    UploadTask uploadTask;

    String name, description, category, link;
    double price, discount;

    Boolean image = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount);

        imageView = findViewById(R.id.imageView);
        selectImg = findViewById(R.id.buttonSelectImage);
        addItem = findViewById(R.id.buttonAddItem);
        camera = findViewById(R.id.buttonCamera);

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


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    insertToDatabase();
                }
            }
        });
    }

    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);

    }

    public void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            image = true;
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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
            image = true;
        }
    }


    public boolean checkData() {
        Boolean correctData = true;

        EditText nam, des, cat, pr, disc;

        nam = findViewById(R.id.editTextName);
        des = findViewById(R.id.editTextDescription);
        cat = findViewById(R.id.editTextCategory);
        pr = findViewById(R.id.editTextPrice);
        disc = findViewById(R.id.editTextDiscount);

        name = nam.getText().toString();
        description = des.getText().toString();
        category = cat.getText().toString();

        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Complete correct the informations", Toast.LENGTH_LONG).show();
            correctData = false;
        }

        try {
            price = Double.parseDouble(pr.getText().toString());
            discount = Double.parseDouble(disc.getText().toString());
            if (image == false && correctData == true) {
                Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_LONG).show();
                System.out.println("Select Image");
                correctData = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Price and discount must be numbers", Toast.LENGTH_LONG).show();
            correctData = false;
        }

        return correctData; //return true if data completed correct
    }

    public void insertToDatabase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final ProgressDialog pd = ProgressDialog.show(this, "", "Uploading...");

        final DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference("items");

        StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
        uploadTask = imageRef.putFile(imageUri);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                link = downloadUrl.toString();

                pd.dismiss();

                Item item = new Item(name, category, price, discount, description, link, "temp");

                String id = databaseItem.push().getKey();
                databaseItem.child(id).setValue(item);

                Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Error while uploading", Toast.LENGTH_LONG).show();
            }
        });

    }
}
