package com.example.project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDAO {

    @Query("SELECT * FROM foodinfo ORDER BY id DESC")
    List<Food> getAll();

    @Query("SELECT * FROM foodinfo ORDER BY id DESC LIMIT 1")
    Food getLatestWord();

    @Query("SELECT * FROM foodinfo WHERE date = :findDate")
    List<Food> getFoodByName(String findDate);

    @Query("SELECT * FROM foodinfo WHERE datemonth = :findDate")
    List<Food> getFoodByMonth(String findDate);

    @Query("SELECT * FROM foodinfo WHERE id = :findDate")
    Food getFoodid(int findDate);

    @Query("SELECT * FROM foodinfo WHERE typemeal = :foodType AND datemonth = :monthMillis")
    List<Food> getFoodsByTypeAndMonth(String foodType, long monthMillis);

    @Insert
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);

    @Query("DELETE FROM foodinfo")
    void deleteAll();

}
