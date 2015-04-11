package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SegmentHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public SegmentHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public SegmentHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertSegment(String name, int distance, double cost){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.SEGMENT_NAME, name);
		content.put(DataBaseHelper.SEGMENT_DISTANCE, distance);
		content.put(DataBaseHelper.SEGMENT_COST, cost);
				
		return db.insertOrThrow(DataBaseHelper.SEGMENT_TABLE_NAME, null, content);
		
	}
	
	public boolean editSegment(int id, String name, int distance, double cost){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.SEGMENT_NAME, name);
		content.put(DataBaseHelper.SEGMENT_DISTANCE, distance);
		content.put(DataBaseHelper.SEGMENT_COST, cost);
		
		return db.update(DataBaseHelper.SEGMENT_TABLE_NAME, content, DataBaseHelper.SEGMENT_ID+"=?", new String[]{""+id})>0;
	}
	
	public boolean removeSegment(int id){
		
		return db.delete(DataBaseHelper.SEGMENT_TABLE_NAME, DataBaseHelper.SEGMENT_ID + " =?", new String[]{""+id}) > 0;
		
	}
	
	public Cursor getSegmentID(String name){
		
		return db.query(DataBaseHelper.SEGMENT_TABLE_NAME, 
						new String[]{DataBaseHelper.SEGMENT_ID}, 
						DataBaseHelper.SEGMENT_NAME + " =?", 
						new String[]{name}, 
						null, null, null);
	}
	
	public Cursor getSegmentName(int id){
		
		return db.query(DataBaseHelper.SEGMENT_TABLE_NAME, 
				new String[]{DataBaseHelper.SEGMENT_NAME}, 
				DataBaseHelper.SEGMENT_ID + " =?", 
				new String[]{""+id}, 
				null, null, null);
	}
	
	public Cursor returnSegments(){
		
		return db.query(DataBaseHelper.SEGMENT_TABLE_NAME, 
						new String[]{DataBaseHelper.SEGMENT_ID, DataBaseHelper.SEGMENT_NAME,  DataBaseHelper.SEGMENT_DISTANCE, DataBaseHelper.SEGMENT_COST}, 
						null, null, null, null, null);
	}	
	
}
