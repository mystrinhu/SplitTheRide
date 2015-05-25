package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TripHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public TripHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public TripHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public int tripsInDate(String date){
		
		Cursor cursor = db.query(DataBaseHelper.TRIP_TABLE_NAME, 
						new String[]{DataBaseHelper.TRIP_ID, DataBaseHelper.TRIP_DRIVER_ID, DataBaseHelper.TRIP_VEHICLE_ID},
						DataBaseHelper.TRIP_DATE + " =?", 
						new String[]{date}, 
						null, null, null);

		return cursor.getCount();
	}

	public Cursor getLastTripID(){

		return db.rawQuery("SELECT MAX("+DataBaseHelper.TRIP_ID+") FROM "+DataBaseHelper.TRIP_TABLE_NAME, null);
	}
	
	public long insertTrip(String date, int driver_id, int vehicle_id){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.TRIP_DATE,date);
		content.put(DataBaseHelper.TRIP_DRIVER_ID, driver_id);
		content.put(DataBaseHelper.TRIP_VEHICLE_ID, vehicle_id);
				
		return db.insertOrThrow(DataBaseHelper.TRIP_TABLE_NAME, null, content);
		
	}
	
	public Cursor returnData(){
		
		return db.query(DataBaseHelper.TRIP_TABLE_NAME, new String[]{DataBaseHelper.TRIP_DATE, DataBaseHelper.TRIP_DRIVER_ID, DataBaseHelper.TRIP_VEHICLE_ID}, null, null, null, null, null);
	}
	
	
		
	
}
