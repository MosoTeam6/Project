package com.example.project.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Food;
import com.example.project.databinding.ItemfoodinfoBinding;

import java.util.List;

public class FoodInfoAdapter extends RecyclerView.Adapter<FoodInfoAdapter.FoodInfoViewHolder> {

    private final List<Food> list;
    private final fooditemclick listener;

    public FoodInfoAdapter(List<Food> list, fooditemclick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<Food> updatefoodinfo) {
        list.clear();
        list.addAll(updatefoodinfo);
        notifyDataSetChanged();
    }

    public static class FoodInfoViewHolder extends RecyclerView.ViewHolder {

        private final ItemfoodinfoBinding binding;

        public FoodInfoViewHolder(ItemfoodinfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public FoodInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemfoodinfoBinding binding = ItemfoodinfoBinding.inflate(inflater, parent, false);
        return new FoodInfoViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull FoodInfoViewHolder holder, int position) {
        Food food = list.get(position);
        ItemfoodinfoBinding binding = holder.binding;

        // Decode byte array to Bitmap
        byte[] image = food.getImageuri();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        // Set data to UI components
        binding.recyclerviewtypeMeal.setText(food.getTypemeal());
        binding.recyclerviewfoodName.setText(food.getFoodname());
        binding.recyclerviewKcal.setText(food.getCal() + " Kcal");
        binding.recyclerviewfoodImage.setImageBitmap(bitmap);

        // Set click listener
        binding.worditem.setOnClickListener(view -> listener.onClick(food));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface fooditemclick {
        void onClick(Food item);
    }
}
