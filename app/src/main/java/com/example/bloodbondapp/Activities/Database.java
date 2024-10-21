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
        String qry1="create table users(name text, city text, blood_group text, mobile text, password text)";
        sqLiteDatabase.execSQL(qry1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void register(String name, String city, String blood_type, String mobile, String password){
        ContentValues cv =new ContentValues();
        cv.put("name",name);
        cv.put("city",city);
        cv.put("blood_type",blood_type);
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
        }
        return result;
    }
}
