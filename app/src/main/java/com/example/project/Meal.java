package com.example.project;

import android.content.ContentValues;

import java.util.Random;

public class Meal {
    private String location;
    private String foodName;
    private String sideDish;
    private String impression;
    private String time;
    private double cost;
    private String date;
    private int calorie;

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setSideDish(String sideDish) {
        this.sideDish = sideDish;
    }

    public String getSideDish() {
        return sideDish;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getImpression() {
        return impression;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }


    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyContentProvider.LOCATION, location);
        contentValues.put(MyContentProvider.FOOD_NAME, foodName);
        contentValues.put(MyContentProvider.SIDE_DISH, sideDish);
        contentValues.put(MyContentProvider.IMPRESSION, impression);
        contentValues.put(MyContentProvider.TIME, time);
        contentValues.put(MyContentProvider.COST, cost);
        contentValues.put(MyContentProvider.DATE, date);
        contentValues.put(MyContentProvider.IMAGE_PATH, imagePath);
        contentValues.put(MyContentProvider.CALORIE, calorie);

        return contentValues;
    }

}

