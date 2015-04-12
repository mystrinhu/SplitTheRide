package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.entities.Route;

public class RouteHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public RouteHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public RouteHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertRoute(String name){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.ROUTE_NAME,name);
				
		return db.insertOrThrow(DataBaseHelper.ROUTE_TABLE_NAME, null, content);
		
	}
	
	public boolean editRoute(int id, String name){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.ROUTE_NAME, name);
		
		return db.update(DataBaseHelper.ROUTE_TABLE_NAME, content, DataBaseHelper.ROUTE_ID+"=?", new String[]{""+id})>0;
	}
	
	public boolean routeNameExists(String name){
		
		Cursor cursor = db.query(DataBaseHelper.ROUTE_TABLE_NAME, 
								new String[]{DataBaseHelper.ROUTE_ID, DataBaseHelper.ROUTE_NAME}, 
								DataBaseHelper.ROUTE_NAME + " =?", 
								new String[]{name}, 
								null, null, null);
		
		return cursor.getCount()>0;
				
	}

    public Route getRoute(int id){

        Route r = null;

        Cursor c = db.query(DataBaseHelper.ROUTE_TABLE_NAME,
                            new String[]{DataBaseHelper.ROUTE_ID, DataBaseHelper.ROUTE_NAME},
                            DataBaseHelper.ROUTE_ID + " =?",
                            new String[]{""+id},
                            null, null, null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            r = new Route(c.getInt(0), c.getString(1));

            c.moveToNext();
        }

        return r;

    }
	
	public Cursor getRouteID (String name){
		
		return db.query(DataBaseHelper.ROUTE_TABLE_NAME, 
						new String[]{DataBaseHelper.ROUTE_ID}, 
						DataBaseHelper.ROUTE_NAME + " =?", 
						new String[]{name}, 
						null, null, null);
	}
	
	public boolean removeRoute(int id){
		
		return db.delete(DataBaseHelper.ROUTE_TABLE_NAME, DataBaseHelper.ROUTE_ID + " =?", new String[]{""+id}) > 0;
		
	}
	
	public Cursor returnRoutes(){
		
		return db.query(DataBaseHelper.ROUTE_TABLE_NAME, new String[]{DataBaseHelper.ROUTE_ID, DataBaseHelper.ROUTE_NAME}, null, null, null, null, null);
	}	
	
}
