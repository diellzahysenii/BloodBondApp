package com.example.bloodbondapp.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
//    public Database(@Nullable Context context) {
//        super(context, "bloodbondapp", null, 2); // Ensure this version is incremented from 1 to 2
//    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, city TEXT, blood_group TEXT, mobile TEXT, password TEXT)";
        String qry2 = "CREATE TABLE donations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT, " +
                "city TEXT, " +
                "blood_group TEXT, " +
                "mobile TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        String qry3 = "CREATE TABLE requests(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT, " +
                "city TEXT, " +
                "blood_group TEXT, " +
                "mobile TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        sqLiteDatabase.execSQL(qry1);
        sqLiteDatabase.execSQL(qry2);
        sqLiteDatabase.execSQL(qry3);
    }

//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS donations");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS requests");
//        onCreate(sqLiteDatabase);
//    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create the 'requests' table
            db.execSQL("CREATE TABLE requests(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "name TEXT, " +
                    "city TEXT, " +
                    "blood_group TEXT, " +
                    "mobile TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
        }
    }




    public void register(String name, String city, String blood_group, String mobile, String password){
        ContentValues cv =new ContentValues();
        cv.put("name",name);
        cv.put("city",city);
        cv.put("blood_group", blood_group);
        cv.put("mobile",mobile);
        cv.put("password",password);
        SQLiteDatabase db =getWritableDatabase();
        db.insert("users",null,cv);
        db.close();
    }
    public int login(String mobile, String password){
        int result=0;
        String[] str =new String[2];
        str[0]=mobile;
        str[1]=password;
        SQLiteDatabase db =getReadableDatabase();
        Cursor c= db.rawQuery("select * from users where mobile=? and password=?",str);
        if(c.moveToFirst()){
            result=1;
            db.close();
            c.close();
        }

        return result;
    }
    public int getUserId(String mobile, String password) {
        int userId = -1; // Default to -1 if login fails
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM users WHERE mobile=? AND password=?", new String[]{mobile, password});
        if (c.moveToFirst()) {
            userId = c.getInt(c.getColumnIndexOrThrow("id"));
        }
        c.close();
        db.close();
        return userId;
    }
    public boolean insertDonation(int userId, String name, String city, String bloodGroup, String mobile) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the data
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("name", name);
        contentValues.put("city", city);
        contentValues.put("blood_group", bloodGroup);
        contentValues.put("mobile", mobile);

        // Insert the data into the donations table
        long result = db.insert("donations", null, contentValues);
        db.close();

        // Return true if insertion was successful, otherwise false
        return result != -1;
    }
    public boolean checkDuplicateDonation(int userId, String userName, String userCity, String userBloodGroup, String userMobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "donations", // Table name
                null,        // All columns
                "user_id = ? AND name = ? AND city = ? AND blood_group = ? AND mobile = ?", // WHERE clause
                new String[]{String.valueOf(userId), userName, userCity, userBloodGroup, userMobile}, // Arguments
                null,        // Group by
                null,        // Having
                null         // Order by
        );

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    public Cursor getUserData(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE id=?", new String[]{String.valueOf(userId)});
    }
    public void getAllDonationsAsync(OnDonationsFetchedListener listener) {
        // Use a background thread
        new Thread(() -> {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT name, city, blood_group, mobile FROM donations", null);

            // Create a list to hold the data
            ArrayList<HashMap<String, String>> donationList = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HashMap<String, String> donation = new HashMap<>();
                    donation.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    donation.put("city", cursor.getString(cursor.getColumnIndexOrThrow("city")));
                    donation.put("blood_group", cursor.getString(cursor.getColumnIndexOrThrow("blood_group")));
                    donation.put("mobile", cursor.getString(cursor.getColumnIndexOrThrow("mobile")));
                    donationList.add(donation);
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();

            // Pass the result to the callback on the UI thread
            if (listener != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> listener.onDonationsFetched(donationList));
            }
        }).start();
    }

    // Callback interface for donations
    public interface OnDonationsFetchedListener {
        void onDonationsFetched(ArrayList<HashMap<String, String>> donations);
    }


    public boolean deleteDonation(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("donations", "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0; // Return true if at least one row was deleted
    }





    public boolean insertRequest(int userId, String name, String city, String bloodGroup, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("name", name);
        contentValues.put("city", city);
        contentValues.put("blood_group", bloodGroup);
        contentValues.put("mobile", mobile);

        long result = db.insert("requests", null, contentValues);
        db.close();

        return result != -1; // Return true if insertion was successful
    }


    public boolean checkDuplicateRequest(int userId, String userName, String userCity, String userBloodGroup, String userMobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "requests",
                null,
                "user_id = ? AND name = ? AND city = ? AND blood_group = ? AND mobile = ?",
                new String[]{String.valueOf(userId), userName, userCity, userBloodGroup, userMobile},
                null,
                null,
                null
        );

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }
    public Cursor getRequestData(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM requests WHERE user_id=?", new String[]{String.valueOf(userId)});
    }
    public boolean deleteRequest(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("requests", "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0; // Return true if at least one row was deleted
    }


}
