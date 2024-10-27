package com.example.bloodbondapp.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "CREATE TABLE users(name TEXT, city TEXT, blood_group TEXT, mobile TEXT, password TEXT)";
        String qry2 = "CREATE TABLE donations(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, city TEXT, blood_group TEXT, mobile TEXT)";
        sqLiteDatabase.execSQL(qry1);
        sqLiteDatabase.execSQL(qry2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS donations");
        onCreate(sqLiteDatabase);
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
    public void postDonnation(String name, String city, String blood_group, String mobile){

    }
//    public int login(String mobile, String password) {
//        int result = 0;
//        String[] str = new String[2];
//        str[0] = mobile;
//        str[1] = password;
//
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor c = null;
//
//        try {
//            c = db.rawQuery("SELECT * FROM users WHERE mobile=? AND password=?", str);
//
//            if (c.moveToFirst()) {
//                result = 1;  // Login successful
//            }
//        } finally {
//            // Ensure the Cursor is closed
//            if (c != null) {
//                c.close();
//            }
//            // Optionally close the database connection if not reused
//             db.close();  // Uncomment this if you're not reusing the database connection
//        }
//
//        return result;
//    }

}
