package com.example.project;

import androidx.room.TypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @TypeConverter
    public static Date fromString(String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }
}
