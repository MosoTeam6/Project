package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MealInputActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText locationEditText;
    private EditText foodNameEditText;
    private EditText sideDishEditText;
    private EditText impressionEditText;
    private EditText timeEditText;
    private EditText costEditText;
    private EditText dateEditText;
    private EditText imagePathEditText;

    private ImageView mealImageView;
    private Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_input);

        locationEditText = findViewById(R.id.editTextLocation);
        foodNameEditText = findViewById(R.id.editTextFoodName);
        sideDishEditText = findViewById(R.id.editTextSideDish);
        impressionEditText = findViewById(R.id.editTextImpression);
        timeEditText = findViewById(R.id.editTextTime);
        costEditText = findViewById(R.id.editTextCost);
        dateEditText = findViewById(R.id.editTextDate);
        imagePathEditText = findViewById(R.id.editTextImagePath);

        mealImageView = findViewById(R.id.imageViewMeal);

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
            }
        });

        Button takePhotoButton = findViewById(R.id.buttonTakePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        Button pickPhotoButton = findViewById(R.id.buttonPickPhoto);
        pickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhotoFromGallery();
            }
        });

        meal = new Meal();
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickPhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mealImageView.setImageBitmap(imageBitmap);

                // Save image to file and set file path
                String imagePath = saveImageToFile(imageBitmap);
                meal.setImagePath(imagePath);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                mealImageView.setImageURI(selectedImageUri);

                String imagePath = saveImageToFile(selectedImageUri);
                meal.setImagePath(imagePath);
            }
        }
    }

    private String saveImageToFile(Bitmap imageBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/MealApp");
        myDir.mkdirs();

        String fileName = "meal_image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String saveImageToFile(Uri selectedImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);

            String fileName = "meal_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getFilesDir(), fileName);

            FileOutputStream out = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveMeal() {
        String location = locationEditText.getText().toString();
        String foodName = foodNameEditText.getText().toString();
        String sideDish = sideDishEditText.getText().toString();
        String impression = impressionEditText.getText().toString();
        String time = timeEditText.getText().toString();
        double cost = Double.parseDouble(costEditText.getText().toString());
        String date = dateEditText.getText().toString();
        String imagePath = imagePathEditText.getText().toString();

        meal.setLocation(location);
        meal.setFoodName(foodName);
        meal.setSideDish(sideDish);
        meal.setImpression(impression);
        meal.setTime(time);
        meal.setCost(cost);
        meal.setDate(date);
        meal.setImagePath(imagePath);

        MealDatabaseHelper databaseHelper = new MealDatabaseHelper(this);
        databaseHelper.addMeal(meal);
    }
}
