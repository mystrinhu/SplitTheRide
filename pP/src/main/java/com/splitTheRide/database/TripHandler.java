package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

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

	public Cursor tripsInDate(String date) {

		Cursor cursor = db.query(DataBaseHelper.TRIP_TABLE_NAME,
				new String[]{DataBaseHelper.TRIP_ID, DataBaseHelper.TRIP_DRIVER_ID, DataBaseHelper.TRIP_VEHICLE_ID, DataBaseHelper.TRIP_ROUND_TRIP},
				DataBaseHelper.TRIP_DATE + " =?",
				new String[]{date},
				null, null, null);

		return cursor;
	}

	public int getLastTripID() {

		int lastTrip = 0;

		Cursor c = db.rawQuery("SELECT MAX(" + DataBaseHelper.TRIP_ID + ") FROM " + DataBaseHelper.TRIP_TABLE_NAME, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {
			lastTrip = c.getInt(0);
			c.moveToNext();
		}

		return lastTrip;

	}

	public long insertTrip(String date, int driver_id, int vehicle_id, int round_trip) {
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.TRIP_DATE,date);
		content.put(DataBaseHelper.TRIP_DRIVER_ID, driver_id);
		content.put(DataBaseHelper.TRIP_VEHICLE_ID, vehicle_id);
		content.put(DataBaseHelper.TRIP_ROUND_TRIP, round_trip);
				
		return db.insertOrThrow(DataBaseHelper.TRIP_TABLE_NAME, null, content);
		
	}

	public boolean editTrip(int tripID, int driverID, int vehicleID, int roundTrip) {

		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.TRIP_DRIVER_ID, driverID);
		content.put(DataBaseHelper.TRIP_VEHICLE_ID, vehicleID);
		content.put(DataBaseHelper.TRIP_ROUND_TRIP, roundTrip);


		return db.update(DataBaseHelper.TRIP_TABLE_NAME, content, DataBaseHelper.TRIP_ID + "=?", new String[]{"" + tripID}) > 0;
	}

	public void removeTrip(int trip_id) {

		db.delete(DataBaseHelper.TRIP_TABLE_NAME,
				DataBaseHelper.TRIP_ID + "=?",
				new String[]{"" + trip_id});
	}

	public Cursor returnTrips() {

		return db.query(DataBaseHelper.TRIP_TABLE_NAME, new String[]{DataBaseHelper.TRIP_DATE, DataBaseHelper.TRIP_DRIVER_ID, DataBaseHelper.TRIP_VEHICLE_ID, DataBaseHelper.TRIP_ROUND_TRIP}, null, null, null, null, null);
	}

}

