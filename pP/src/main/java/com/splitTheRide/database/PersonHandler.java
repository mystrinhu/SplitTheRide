package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public PersonHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public PersonHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}

	public long insertPerson(String name, int route) {
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.PERSON_NAME,name);
        content.put(DataBaseHelper.PERSON_USUAL_ROUTE, route);
		
		return db.insertOrThrow(DataBaseHelper.PERSON_TABLE_NAME, null, content);
		
	}

	public boolean editPerson(int id, String name, int route) {
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.PERSON_NAME, name);
        content.put(DataBaseHelper.PERSON_USUAL_ROUTE, route);
		
		
		return db.update(DataBaseHelper.PERSON_TABLE_NAME, content, DataBaseHelper.PERSON_ID+"=?", new String[]{""+id})>0;
	}
	
	public boolean removePerson(int id){
		
		return db.delete(DataBaseHelper.PERSON_TABLE_NAME, DataBaseHelper.PERSON_ID + " =?", new String[]{"" + id}) > 0;
		
	}

	public boolean nameExists(String short_name) {
		
		Cursor cursor = db.query(DataBaseHelper.PERSON_TABLE_NAME,
				new String[]{DataBaseHelper.PERSON_ID, DataBaseHelper.PERSON_NAME},
				DataBaseHelper.PERSON_NAME + " =?",
								new String[]{short_name}, 
								null, null, null);
		
		return cursor.getCount()>0;
				
	}
	
	public Cursor getAllDrivers(){

		return db.rawQuery("SELECT p." + DataBaseHelper.PERSON_ID + ", p." + DataBaseHelper.PERSON_NAME + ", p." + DataBaseHelper.PERSON_USUAL_ROUTE +
						   " FROM "+DataBaseHelper.PERSON_TABLE_NAME+" p, "+DataBaseHelper.VEHICLE_TABLE_NAME+" v "+
						   "WHERE p."+DataBaseHelper.PERSON_ID+" = v."+DataBaseHelper.VEHICLE_PERSON_ID, null);
	}
	
    public String getNamefromID(int id){

        Cursor c = db.query(DataBaseHelper.PERSON_TABLE_NAME,
                            new String[]{DataBaseHelper.PERSON_NAME},
                            DataBaseHelper.PERSON_ID + " =?",
                            new String[]{""+id},
                            null, null, null);
        String name = "";

        c.moveToFirst();
        name = c.getString(0);

        return name;
    }

	public Cursor getIDfromName(String name){

        return db.query(DataBaseHelper.PERSON_TABLE_NAME,
                new String[]{DataBaseHelper.PERSON_ID},
                DataBaseHelper.PERSON_NAME + " =?",
                new String[]{name},
                null, null, null);
    }

	public Cursor returnAllPersonsName() {
		
		return db.query(DataBaseHelper.PERSON_TABLE_NAME,
				new String[]{DataBaseHelper.PERSON_NAME},
						null, null, null, null, null);
	}
	
	public Cursor returnAllPersonsData(){

		return db.query(DataBaseHelper.PERSON_TABLE_NAME, new String[]{DataBaseHelper.PERSON_ID, DataBaseHelper.PERSON_NAME, DataBaseHelper.PERSON_USUAL_ROUTE}, null, null, null, null, null);
	}
	
	
		
	
}
