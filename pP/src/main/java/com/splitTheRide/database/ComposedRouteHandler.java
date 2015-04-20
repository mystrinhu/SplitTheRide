package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ComposedRouteHandler {

	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	
	
	public ComposedRouteHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	
	public ComposedRouteHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertComposedRoute(int route_id, int segment_id){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.CR_ROUTE_ID, route_id);
		content.put(DataBaseHelper.CR_SEGMENT_ID, segment_id);
		
		
		return db.insertOrThrow(DataBaseHelper.CR_TABLE_NAME, null, content);
	}
	
		
	public boolean removeComposedRoute(int route_id){
		
		return db.delete(DataBaseHelper.CR_TABLE_NAME, 
						DataBaseHelper.CR_ROUTE_ID + " =?" , 
						new String[]{""+route_id}) > 0;
		
	}
	
	public Cursor getAllRouteSegments(int route_id){
		
		return db.rawQuery("select segment."+DataBaseHelper.SEGMENT_NAME+" from "+DataBaseHelper.SEGMENT_TABLE_NAME+" segment, "+DataBaseHelper.CR_TABLE_NAME+" cr "+
							"where segment."+DataBaseHelper.SEGMENT_ID+" = cr."+DataBaseHelper.CR_SEGMENT_ID+" and cr."+DataBaseHelper.CR_ROUTE_ID+" = "+route_id, null);
	}
	
	
}
