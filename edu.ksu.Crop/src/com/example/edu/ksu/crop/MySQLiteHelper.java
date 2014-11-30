package com.example.edu.ksu.crop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_WEATHER = "weather";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_HIGH = "high";
  public static final String COLUMN_LOW = "low";
  public static final String COLUMN_POP = "pop";
  public static final String COLUMN_PRIMARY = "primary";

  private static final String DATABASE_NAME = "weather.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_WEATHER + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + 
      COLUMN_DATE + " text not null," +
      COLUMN_HIGH + "text not null" +
      COLUMN_LOW + "text not null" +
      COLUMN_POP + "text not null" +    
      COLUMN_PRIMARY + "text not null" + 
      ");";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
    onCreate(db);
  }

} 