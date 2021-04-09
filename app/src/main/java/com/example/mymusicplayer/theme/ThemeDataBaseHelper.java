package com.example.mymusicplayer.theme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThemeDataBaseHelper extends SQLiteOpenHelper {

    public  static final String DATABASE_NAME="THEME.db";
    public  static final int DATABASE_VERSION=8;
    public  static final String THEME_TABLE_NAME="THEME";

    public  static final String COLUMN_THEME_ID="theme_id";
    public  static final String COLUMN_THEME="Theme_Status";
    public static final String CREATE_THEME_TABLE  = " CREATE TABLE "+THEME_TABLE_NAME+"("
            +COLUMN_THEME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_THEME+" TEXT "
            +")";
    Context context;
    public ThemeDataBaseHelper(@Nullable Context context) {
        super(context, THEME_TABLE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_THEME_TABLE);
        Toast.makeText(context, "Theme OnCreate is Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+THEME_TABLE_NAME);
        onCreate(db);
        Toast.makeText(context, "Theme onUpgrade is Called",Toast.LENGTH_SHORT).show();
    }

    public int insertData(ThemeNote themeNote){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_THEME,themeNote.getThemeStatus());
        int id= (int) sqLiteDatabase.insert(THEME_TABLE_NAME,null,contentValues);
        return id;
    }
    public List<ThemeNote> getAllNotes(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<ThemeNote> dateList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+THEME_TABLE_NAME,null);
        if (cursor.moveToFirst()){
            do {
                ThemeNote note = new ThemeNote(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_THEME_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_THEME))
                );
                dateList.add(note);
            }while (cursor.moveToNext());
        }
        return dateList;
    }


    public int updateThemeData(ThemeNote themeNote){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValue=new ContentValues();

        contentValue.put(COLUMN_THEME,themeNote.getThemeStatus());
        int status1 = sqLiteDatabase.update(THEME_TABLE_NAME,contentValue,"theme_id=?",new String[]{String.valueOf(themeNote.getId())});
        return status1;
    }

}
