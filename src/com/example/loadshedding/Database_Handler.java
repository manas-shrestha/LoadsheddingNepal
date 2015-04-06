package com.example.loadshedding;

import com.google.android.gms.internal.ky;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database_Handler extends Activity {
	String KEY_ROWID = "row_id";
	String KEY_DAY = "day";
	String KEY_SHIFT = "shift";
	String KEY_FIRST_TIMESTART = "timestart";
	String KEY_FIRST_TIMESTOP = "timestop";
	String KEY_SECOND_TIMESTART = "timestarttwo";
	String KEY_SECOND_TIMESTOP = "timestoptwo";
	String KEY_NAME = "name";
	String KEY_GROUP = "groupw";
	String KEY_LATITUDE = "latitude";
	String KEY_LONGITUDE = "longitude";
	String KEY_LAST_TIME_WEATHER_UPDATED = "updatetime";
	String KEY_USER_GROUP = "usrgrp";
	String db_name = "loadshedding";
	String db_table1 = "group1";
	String db_table2 = "group2";
	String db_table3 = "group3";
	String db_table4 = "group4";
	String db_table5 = "group5";
	String db_table6 = "group6";
	String db_table7 = "group7";
	String db_tablegroup = "place";
	String db_essentials = "essentials";
	private static final int DATABASE_VERSION = 1;

	private databasehelper ourdatabaseHelper;
	private final Context ourdbContext;
	private SQLiteDatabase ourReportsDatabase;

	private class databasehelper extends SQLiteOpenHelper {

		public databasehelper(Context context) {
			super(context, db_name, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub

		}

		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + db_table1 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_table2 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_table3 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_table4 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_table5 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_table6 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + db_tablegroup + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
					+ " TEXT NOT NULL, " + KEY_GROUP
					+ " TEXT NOT NULL, " + KEY_LATITUDE
					+ " TEXT NOT NULL, " + KEY_LONGITUDE
					+ " TEXT NOT NULL);");

			db.execSQL("CREATE TABLE " + db_essentials + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_USER_GROUP
					+ " TEXT NOT NULL, " + KEY_LAST_TIME_WEATHER_UPDATED
					+ " TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE " + db_table7 + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTART
					+ " TEXT NOT NULL, " + KEY_FIRST_TIMESTOP
				 	+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTART
					+ " TEXT NOT NULL, " + KEY_SECOND_TIMESTOP
					+ " TEXT NOT NULL);");
		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + db_table1);
			onCreate(db);
		}

	}

	public Database_Handler(Context c) {
		ourdbContext = c;
	}
	

	public Database_Handler() {
		ourdbContext = getApplicationContext();
	}

	public Database_Handler open() throws SQLException {
		ourdatabaseHelper = new databasehelper(ourdbContext);
		ourReportsDatabase = ourdatabaseHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourdatabaseHelper.close();

	}

	public String getData() {
		// TODO Auto-generated method stub

		String[] columns = new String[] { KEY_ROWID, KEY_DAY,
				KEY_FIRST_TIMESTART, KEY_FIRST_TIMESTOP, KEY_SECOND_TIMESTART,
				KEY_SECOND_TIMESTOP };
		Cursor c = ourReportsDatabase.query("group1", columns, null, null,
				null, null, null);
		String result = "";

		int iRow = c.getColumnIndex(KEY_ROWID);
		int iDay = c.getColumnIndex(KEY_DAY);
		int iStart = c.getColumnIndex(KEY_FIRST_TIMESTART);
		int iStop = c.getColumnIndex(KEY_FIRST_TIMESTOP);
		int iStart2 = c.getColumnIndex(KEY_SECOND_TIMESTART);
		int iStop2 = c.getColumnIndex(KEY_SECOND_TIMESTOP);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iDay) + " "
					+ c.getString(iStart) + " " + c.getString(iStop) + " "+ c.getString(iStart2) + " "+ c.getString(iStop2) +"\n";
			Log.e("Yo get data",c.getString(iStart) );
		}
		return result;
	}

	public long CreateEntry(String group, String Day, 
			String timestart, String timestop, String timestart2,
			String timestop2) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put(KEY_DAY, Day);
		cv.put(KEY_FIRST_TIMESTART, timestart);
		cv.put(KEY_FIRST_TIMESTOP, timestop);
		cv.put(KEY_SECOND_TIMESTART, timestart2);
		cv.put(KEY_SECOND_TIMESTOP, timestop2);
		Log.e("create afgadi",timestart);
		String table = "group" + group;
		return ourReportsDatabase.insert(table, null, cv);
	}
	public long createEntryForEssentials(String tablename,String Group, String lastupdated) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put(KEY_USER_GROUP, Group);
		cv.put(KEY_LAST_TIME_WEATHER_UPDATED, lastupdated);
		return ourReportsDatabase.insert(tablename, null, cv);
	}
	public long CreateEntryForPlacesAndGroups(String tablename, String name, 
			String group, String latitude, String longitude
			) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_GROUP, group);
		cv.put(KEY_LATITUDE, latitude);
		cv.put(KEY_LONGITUDE, longitude);
			
		return ourReportsDatabase.insert(tablename, null, cv);
	}
	
	public String getnameandplace(int no,String tablename) {
		String[] columns = new String[] { KEY_NAME, KEY_GROUP };
		Cursor c = ourReportsDatabase.query(tablename, columns, null,
				null, null, null, null);
		String result = "";
		int place = c.getColumnIndex(KEY_NAME);
		int group = c.getColumnIndex(KEY_GROUP);
		c.moveToPosition(no);
		result = c.getString(place)+ "\nGroup:" + c.getString(group);
		return result;
	}
	
	
	public String getday(int no, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_DAY };
		Cursor c = ourReportsDatabase.query("group" + group, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_DAY);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	public String firststart(int no, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_FIRST_TIMESTART };
		Cursor c = ourReportsDatabase.query("group" + group, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_FIRST_TIMESTART);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	
	public String firststop(int no, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_FIRST_TIMESTOP };
		Cursor c = ourReportsDatabase.query("group" + group, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_FIRST_TIMESTOP);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	public String secondstart(int no, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_SECOND_TIMESTART };
		Cursor c = ourReportsDatabase.query("group" + group, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_SECOND_TIMESTART);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	public String secondstop(int no, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_SECOND_TIMESTOP };
		Cursor c = ourReportsDatabase.query("group" + group, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_SECOND_TIMESTOP);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	
	public String selectbydaystop2(String day, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_SECOND_TIMESTOP };
		Cursor c = ourReportsDatabase.query("group"+group, columns, 
                "day like " + "'"+day+"'", null, null, null, null);
		String result = "";
		c.moveToNext();
		int day1 = c.getColumnIndex(KEY_SECOND_TIMESTOP);
		
		result = c.getString(day1);
		return result;
	}
	public String selectbydaystop1(String day, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_FIRST_TIMESTOP };
		Cursor c = ourReportsDatabase.query("group"+group, columns, 
                "day like " + "'"+day+"'", null, null, null, null);
		String result = "";
		c.moveToNext();
		int day1 = c.getColumnIndex(KEY_FIRST_TIMESTOP);
		
		result = c.getString(day1);
		return result;
	}
	
	
	public String selectbydaystart1(String day, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_FIRST_TIMESTART };
		Cursor c = ourReportsDatabase.query("group"+group, columns, 
                "day like " + "'"+day+"'", null, null, null, null);
		String result = "";
		c.moveToNext();
		int day1 = c.getColumnIndex(KEY_FIRST_TIMESTART);
		
		result = c.getString(day1);
		return result;
	}
	
	public String selectbydaystart2(String day, String group) {
		String[] columns = new String[] { KEY_ROWID, KEY_SECOND_TIMESTART };
		Cursor c = ourReportsDatabase.query("group"+group, columns, 
                "day like " + "'"+day+"'", null, null, null, null);
		String result = "";
		c.moveToNext();
		int day1 = c.getColumnIndex(KEY_SECOND_TIMESTART);
		
		result = c.getString(day1);
		return result;
	}
	
	public int noOfRecords(String Tablename){
		String[] columns = new String[]{KEY_ROWID};
		Cursor c = ourReportsDatabase.query(Tablename, columns , null, null, null, null, null);
		
		
		
		return c.getCount();
		
	}
	
	public void editentry(String tablename , String Group, String TimeUpdated) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_USER_GROUP, Group);
		cvUpdate.put(KEY_LAST_TIME_WEATHER_UPDATED, TimeUpdated);
		ourReportsDatabase.update(tablename, cvUpdate, KEY_ROWID + "="
				+ 1, null);
	}
	
	public String getUserGroup(int no) {
		String[] columns = new String[] { KEY_ROWID, KEY_USER_GROUP };
		Cursor c = ourReportsDatabase.query(db_essentials, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_USER_GROUP);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	public String getLastUpdated(int no) {
		String[] columns = new String[] { KEY_ROWID, KEY_LAST_TIME_WEATHER_UPDATED };
		Cursor c = ourReportsDatabase.query(db_essentials, columns, null,
				null, null, null, null);
		String result = "";
		int day = c.getColumnIndex(KEY_LAST_TIME_WEATHER_UPDATED);
		c.moveToPosition(no);
		result = c.getString(day);
		return result;
	}
	
	
}
