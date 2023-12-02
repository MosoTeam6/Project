package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.Adapter.FoodInfoAdapter;
import com.example.project.Adapter.FoodInfoAdapter.fooditemclick;
import com.example.project.databinding.ActivityMyMeelBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MyMeelActivity extends AppCompatActivity implements fooditemclick {

    private static final String TAG = "MyMeelActivity";
    private ActivityMyMeelBinding binding;

    private Long loadtime = null;
    private FoodInfoAdapter foodAdapter;
    private final Set<CalendarDay> selectedDates = new HashSet<>();
    private final ActivityResultLauncher<Intent> UpdatedFood = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    boolean isUpdated = result.getData().getBooleanExtra("Updated", false);
                    if (isUpdated) {
                        Thread updateThread = new Thread(this::updateFoodInfo);
                        updateThread.start();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyMeelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addFoodButton.setOnClickListener(view -> UpdatedFood.launch(new Intent(getApplicationContext(), AddFoodActivity.class)));

        binding.calendar.setOnDateChangedListener((widget, date, selected) -> {
            clearCustomSelectorForSelectedDates();
            if (selected) {
                selectedDates.add(date);
                updateDecorators();

                String formattedDate = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA).format(date.getDate().getTime());
                Log.d(TAG, formattedDate);
                long loaddata = convertStringToMillis(formattedDate);
                loadtime = loaddata;
                Log.d(TAG, String.valueOf(loaddata));

                Thread thread = new Thread(() -> {
                    List<Food> foodinfo = AppDataBase.getInstance(this).foodDao().getFoodByName(String.valueOf(loaddata));
                    Log.d(TAG, foodinfo.toString());
                    runOnUiThread(() -> {
                        foodAdapter.setList(foodinfo);
                        foodAdapter.notifyDataSetChanged();
                    });
                });
                thread.start();
            }
        });

        initView();
    }

    private void initView() {
        foodAdapter = new FoodInfoAdapter(new ArrayList<>(), this); // Pass an empty list initially
        binding.myMealRecyclerView.setAdapter(foodAdapter);
        binding.myMealRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    private void updateFoodInfo() {
        if (loadtime != null) {
            List<Food> updatefoodinfo = AppDataBase.getInstance(this).foodDao().getFoodByName(String.valueOf(loadtime));
            runOnUiThread(() -> {
                foodAdapter.setList(updatefoodinfo);
                foodAdapter.notifyDataSetChanged();
            });
        }
    }

    private void clearCustomSelectorForSelectedDates() {
        for (CalendarDay selectedDate : selectedDates) {
            // Remove custom_selector for selected dates
            binding.calendar.removeDecorators();
        }
        // Clear the selected dates list
        selectedDates.clear();
    }

    private void updateDecorators() {
        // Set decorators only for selected dates
        DayViewDecorator selectedDecorator = new SelectedDecorator(selectedDates, this);
        binding.calendar.addDecorator(selectedDecorator);
    }

    private long convertStringToMillis(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
            Date date = format.parse(dateString);

            // Convert date to milliseconds
            return date != null ? date.getTime() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onClick(Food item) {
        Intent intent = new Intent(this, SeeFoodActivity.class);
        intent.putExtra("id", item.getId());
        startActivity(intent);
        Log.d(TAG, item.toString());
    }

    static class SelectedDecorator implements DayViewDecorator {
        private final Set<CalendarDay> dates;
        private final Context context;

        public SelectedDecorator(Set<CalendarDay> dates, Context context) {
            this.dates = dates;
            this.context = context;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.customday)));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.startColor)));
        }
    }
}
