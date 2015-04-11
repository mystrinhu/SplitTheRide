package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VehicleHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public VehicleHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public VehicleHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertVehicle(String name, double consumption, int person_id){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.VEHICLE_NAME,name);
		content.put(DataBaseHelper.VEHICLE_CONSUMPTION, consumption);
		content.put(DataBaseHelper.VEHICLE_PERSON_ID, person_id);
		
		return db.insertOrThrow(DataBaseHelper.VEHICLE_TABLE_NAME, null, content);
		
	}
	
	public boolean editVehicle(int id, String name, double consump, int person_id){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.VEHICLE_NAME, name);
		content.put(DataBaseHelper.VEHICLE_CONSUMPTION, consump);
		content.put(DataBaseHelper.VEHICLE_PERSON_ID, person_id);
		
		
		return db.update(DataBaseHelper.VEHICLE_TABLE_NAME, content, DataBaseHelper.VEHICLE_ID+"=?", new String[]{""+id})>0;
	}
	
	public boolean removeVehicle(int id){
		
		return db.delete(DataBaseHelper.VEHICLE_TABLE_NAME, DataBaseHelper.VEHICLE_ID + " =?", new String[]{""+id}) > 0;
		
	}
	
	
	public Cursor returnData(){
		
		return db.query(DataBaseHelper.VEHICLE_TABLE_NAME, new String[]{DataBaseHelper.VEHICLE_ID, DataBaseHelper.VEHICLE_NAME, DataBaseHelper.VEHICLE_CONSUMPTION, DataBaseHelper.VEHICLE_PERSON_ID}, null, null, null, null, null);
	}
	
	
		
	
}
