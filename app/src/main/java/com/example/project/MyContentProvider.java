package com.example.project;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.project.MyContentProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/meals";
    static final String _ID = "_id";
    static final String LOCATION = "location";
    static final String FOOD_NAME = "foodName";
    static final String SIDE_DISH = "sideDish";
    static final String IMPRESSION = "impression";
    static final String TIME = "time";
    static final String COST = "cost";
    static final String DATE = "date";
    static final String IMAGE_PATH = "imagePath";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private MealDatabaseHelper dbManager;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return dbManager.delete(selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long rowID = dbManager.insert(values);
        return Uri.parse(URL + "/" + rowID);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbManager = MealDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        return dbManager.query(projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
