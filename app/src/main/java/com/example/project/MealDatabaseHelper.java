package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MealDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meal_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MEALS = "meals";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_FOOD_NAME = "foodName";
    private static final String COLUMN_SIDE_DISH = "sideDish";
    private static final String COLUMN_IMPRESSION = "impression";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGE_PATH = "imagePath";
    private static final String COLUMN_CALORIE = "calorie";

    Context context = null;

    private static final String CREATE_TABLE_MEALS =
            "CREATE TABLE " + TABLE_MEALS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LOCATION + " TEXT," +
                    COLUMN_FOOD_NAME + " TEXT," +
                    COLUMN_SIDE_DISH + " TEXT," +
                    COLUMN_IMPRESSION + " TEXT," +
                    COLUMN_TIME + " TEXT," +
                    COLUMN_COST + " REAL," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_IMAGE_PATH + " TEXT," +
                    COLUMN_CALORIE + " INTEGER" +
                    ")";

    public static MealDatabaseHelper getInstance(Context context) {
        return new MealDatabaseHelper(context);
    }
    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    public long insert(ContentValues meal) {
        return getWritableDatabase().insert(
                TABLE_MEALS, null, meal);
    }

    public int delete(String selection, String[] selectionArgs) {
        return getWritableDatabase().delete(TABLE_MEALS, selection, selectionArgs);
    }

    public int update(Meal meal, String selection, String[] selectionArgs) {
        return getWritableDatabase().update(
                TABLE_MEALS, meal.getContentValues(), selection, selectionArgs);
    }

    public Cursor query(String[] projection, String selection,
                        String[] selectionArgs, String groupBy, String having, String orderBy) {
        return getReadableDatabase().query(
                TABLE_MEALS, projection, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void addMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, meal.getLocation());
        values.put(COLUMN_FOOD_NAME, meal.getFoodName());
        values.put(COLUMN_SIDE_DISH, meal.getSideDish());
        values.put(COLUMN_IMPRESSION, meal.getImpression());
        values.put(COLUMN_TIME, meal.getTime());
        values.put(COLUMN_COST, meal.getCost());
        values.put(COLUMN_DATE, meal.getDate());
        values.put(COLUMN_IMAGE_PATH, meal.getImagePath());
        values.put(COLUMN_CALORIE, meal.getCalorie());
        db.insert(TABLE_MEALS, null, values);
        db.close();
    }

}
