package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_DATE, 
      MySQLiteHelper.COLUMN_HIGH,
      MySQLiteHelper.COLUMN_LOW,
      MySQLiteHelper.COLUMN_POP,
      MySQLiteHelper.COLUMN_PRIMARY };

  public WeatherDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public WeatherData createWeather(String date, String high, String low, String pop, String primary) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_DATE, date);
    values.put(MySQLiteHelper.COLUMN_HIGH, high);
    values.put(MySQLiteHelper.COLUMN_LOW, low);
    values.put(MySQLiteHelper.COLUMN_POP, pop);
    values.put(MySQLiteHelper.COLUMN_PRIMARY, primary);
    
    long insertId = database.insert(MySQLiteHelper.TABLE_WEATHER, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_WEATHER,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    WeatherData newComment = cursorToWeather(cursor);
    cursor.close();
    return newComment;
  }

  public void deleteComment(WeatherData comment) {
    long id = comment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_WEATHER, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<WeatherData> getAllComments() {
    List<WeatherData> weatherDays = new ArrayList<WeatherData>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_WEATHER,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	WeatherData weather = cursorToWeather(cursor);
    	weatherDays.add(weather);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return weatherDays;
  }

  private WeatherData cursorToWeather(Cursor cursor) {
	WeatherData weather = new WeatherData();
	weather.setId(cursor.getLong(0));
	weather.setDate(cursor.getString(1));
	weather.setHigh(cursor.getString(2));
	weather.setLow(cursor.getString(3));
	weather.setPOP(cursor.getString(4));
	weather.setPrimary(cursor.getString(5));
	return weather;
  }
} 