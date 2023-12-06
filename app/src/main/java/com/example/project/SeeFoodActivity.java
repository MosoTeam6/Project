package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivitySeeFoodBinding;

import java.sql.SQLData;
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

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getIntent().getIntExtra("id", 0);
                new Thread(()->{
                    AppDataBase.getInstance(SeeFoodActivity.this).foodDao().deleteUserById(id);
                }).start();

                Intent intent = new Intent(getApplicationContext(), MyMeelActivity.class);
                startActivity(intent);	//intent 에 명시된 액티비티로 이동

                finish();	//현재 액티비티 종료
            }
        });
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
