package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "foodinfo")
public class Food {

    private final String address;
    private final String typemeal;
    private final String date;
    private final String datetime;
    private final String datemonth;
    private final String foodname;
    private final byte[] imageuri;
    private final String review;
    private final int price;
    private final int cal;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public Food(String address, String typemeal, String date, String datetime, String datemonth,
                String foodname, byte[] imageuri, String review, int price, int cal, int id) {
        this.address = address;
        this.typemeal = typemeal;
        this.date = date;
        this.datetime = datetime;
        this.datemonth = datemonth;
        this.foodname = foodname;
        this.imageuri = imageuri;
        this.review = review;
        this.price = price;
        this.cal = cal;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getTypemeal() {
        return typemeal;
    }

    public String getDate() {
        return date;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getDatemonth() {
        return datemonth;
    }

    public String getFoodname() {
        return foodname;
    }

    public byte[] getImageuri() {
        return imageuri;
    }

    public String getReview() {
        return review;
    }

    public int getPrice() {
        return price;
    }

    public int getCal() {
        return cal;
    }

    public int getId() {
        return id;
    }

    // Bitmap -> ByteArray 변환
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // ByteArray -> Bitmap 변환
    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
