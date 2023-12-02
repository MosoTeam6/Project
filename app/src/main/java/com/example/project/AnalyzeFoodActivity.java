package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivityAnalyzeFoodBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalyzeFoodActivity extends AppCompatActivity {
    private static final String TAG = "AnalyzeFoodActivity";
    private ActivityAnalyzeFoodBinding binding;
    private int nowyear = 2023;
    private int nowmonth = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyzeFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.prebox.setOnClickListener(view -> previousmonth());
        binding.nextbox.setOnClickListener(view -> nextmonth());

        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        String nowdate = nowyear + "년" + nowmonth + "월";
        long nowmill = MillisMonth(nowdate);
        Log.d(TAG, String.valueOf(nowmill));

        new Thread(() -> {
            List<Food> updatefoodinfo = (List<Food>) AppDataBase.getInstance(AnalyzeFoodActivity.this).foodDao().getFoodByMonth(String.valueOf(nowmill));
            List<Food> breakfastList = filterListByType(updatefoodinfo, "조식");
            List<Food> lunchList = filterListByType(updatefoodinfo, "중식");
            List<Food> dinnerList = filterListByType(updatefoodinfo, "석식");
            List<Food> drinkList = filterListByType(updatefoodinfo, "음료");

            runOnUiThread(() -> {
                binding.typemeal1count.setText("총 " + breakfastList.size() + "회");
                binding.typemealprice.setText(breakfastList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal.setText(breakfastList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal2count.setText("총 " + lunchList.size() + "회");
                binding.typemealprice2.setText(lunchList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal2.setText(lunchList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal3count.setText("총 " + dinnerList.size() + "회");
                binding.typemealprice3.setText(dinnerList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal3.setText(dinnerList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal4count.setText("총 " + drinkList.size() + "회");
                binding.typemealprice4.setText(drinkList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal4.setText(drinkList.stream().mapToInt(Food::getCal).sum() + "Kcal");
            });
        }).start();

        binding.nowdate.setText(nowdate);
    }

    @SuppressLint("SetTextI18n")
    private void previousmonth() {
        nowmonth--;
        if (nowmonth == 0) {
            nowyear--;
            nowmonth = 12;
        }
        String nowdate = nowyear + "년" + nowmonth + "월";
        long nowmill = MillisMonth(nowdate);
        binding.nowdate.setText(nowdate);

        new Thread(() -> {
            List<Food> updatefoodinfo = (List<Food>) AppDataBase.getInstance(AnalyzeFoodActivity.this).foodDao().getFoodByMonth(String.valueOf(nowmill));
            List<Food> breakfastList = filterListByType(updatefoodinfo, "조식");
            List<Food> lunchList = filterListByType(updatefoodinfo, "중식");
            List<Food> dinnerList = filterListByType(updatefoodinfo, "석식");
            List<Food> drinkList = filterListByType(updatefoodinfo, "음료");

            runOnUiThread(() -> {
                binding.typemeal1count.setText("총 " + breakfastList.size() + "회");
                binding.typemealprice.setText(breakfastList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal.setText(breakfastList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal2count.setText("총 " + lunchList.size() + "회");
                binding.typemealprice2.setText(lunchList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal2.setText(lunchList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal3count.setText("총 " + dinnerList.size() + "회");
                binding.typemealprice3.setText(dinnerList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal3.setText(dinnerList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal4count.setText("총 " + drinkList.size() + "회");
                binding.typemealprice4.setText(drinkList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal4.setText(drinkList.stream().mapToInt(Food::getCal).sum() + "Kcal");
            });
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void nextmonth() {
        nowmonth++;
        if (nowmonth >= 13) {
            nowyear++;
            nowmonth = 1;
        }
        String nowdate = nowyear + "년" + nowmonth + "월";
        long nowmill = MillisMonth(nowdate);
        binding.nowdate.setText(nowdate);

        new Thread(() -> {
            List<Food> updatefoodinfo = (List<Food>) AppDataBase.getInstance(AnalyzeFoodActivity.this).foodDao().getFoodByMonth(String.valueOf(nowmill));
            List<Food> breakfastList = filterListByType(updatefoodinfo, "조식");
            List<Food> lunchList = filterListByType(updatefoodinfo, "중식");
            List<Food> dinnerList = filterListByType(updatefoodinfo, "석식");
            List<Food> drinkList = filterListByType(updatefoodinfo, "음료");

            runOnUiThread(() -> {
                binding.typemeal1count.setText("총 " + breakfastList.size() + "회");
                binding.typemealprice.setText(breakfastList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal.setText(breakfastList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal2count.setText("총 " + lunchList.size() + "회");
                binding.typemealprice2.setText(lunchList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal2.setText(lunchList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal3count.setText("총 " + dinnerList.size() + "회");
                binding.typemealprice3.setText(dinnerList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal3.setText(dinnerList.stream().mapToInt(Food::getCal).sum() + "Kcal");

                binding.typemeal4count.setText("총 " + drinkList.size() + "회");
                binding.typemealprice4.setText(drinkList.stream().mapToInt(Food::getPrice).sum() + "원");
                binding.typemealcal4.setText(drinkList.stream().mapToInt(Food::getCal).sum() + "Kcal");
            });
        }).start();
    }

    private long MillisMonth(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월", Locale.KOREA);
            Date date = format.parse(dateString);

            // date를 밀리초로 변환
            return date != null ? date.getTime() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<Food> filterListByType(List<Food> food, String type) {
        List<Food> filteredList = new ArrayList<>();
        for (Food foodinfo : food) {
            if (foodinfo.getTypemeal().equals(type)) {
                filteredList.add(foodinfo);
            }
        }
        return filteredList;
    }

}
