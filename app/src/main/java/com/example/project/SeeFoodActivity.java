package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivitySeeFoodBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SeeFoodActivity extends AppCompatActivity {

    private ActivitySeeFoodBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initview();
    }

    private void initview() {
        int id = getIntent().getIntExtra("id", 0);
        new Thread(() -> {
            Food updatefoodinfo = AppDataBase.getInstance(this).foodDao().getFoodid(id);
            long test = Long.parseLong(updatefoodinfo.getDatetime());
            String good = convertMillisToString(test);
            runOnUiThread(() -> {
                byte[] image = updatefoodinfo.getImageuri();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                binding.seeFoodImage.setImageBitmap(bitmap);
                binding.seeFoodAddress.setText(updatefoodinfo.getAddress());
                binding.seeFoodKcal.setText(updatefoodinfo.getCal() + " Kcal");
                binding.seeFoodExplainText.setText(updatefoodinfo.getReview());
                binding.seeFoodPrice.setText(updatefoodinfo.getPrice() + "원");
                binding.seeFoodTypeMeal.setText(updatefoodinfo.getTypemeal());
                binding.seeFoodName.setText(updatefoodinfo.getFoodname());
                binding.seedateText.setText(good);
            });
        }).start();
    }

    private String convertMillisToString(long millis) {
        SimpleDateFormat convert = new SimpleDateFormat("yyyy년MM월dd일HH시mm분", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return convert.format(calendar.getTime());
    }
}
